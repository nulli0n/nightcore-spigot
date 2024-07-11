/*
 * Copyright 2022-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package su.nightexpress.nightcore.database;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCursor;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.types.Decimal128;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MongoResultSet implements ResultSet {
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    // dateFormat cannot be static due to a threading bug in the library.
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // The current row
    protected BsonDocument current;
    // cursor over all rows
    protected MongoCursor<BsonDocument> cursor;

    // The one-indexed number of the current row. Will be zero until
    // next() is called for the first time.
    protected int rowNum = 0;

    protected boolean closed = false;
    protected boolean wasNull = false;
    protected boolean extJsonMode;

    /**
     * Constructor for a MongoResultset tied to a connection and statement.
     *
     * @param cursor The resultset cursor.
     * @throws SQLException
     */
    public MongoResultSet(
            MongoCursor<BsonDocument> cursor,
            List<List<String>> selectOrder,
            boolean extJsonMode)
            throws SQLException {
        this.extJsonMode = extJsonMode;
        setUpResultset(
                cursor,
                selectOrder,
                true, null);
    }

    /**
     * Constructor for a MongoResultSet not tied to a statement used for MongoDatabaseMetaData.
     *
     * @param cursor The resultset cursor.
     * @throws SQLException
     */
    public MongoResultSet(MongoCursor<BsonDocument> cursor)
            throws SQLException {
        setUpResultset(cursor, null, false, null);
    }

    protected static ByteArrayInputStream getNewByteArrayInputStream(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }

    private void setUpResultset(
            MongoCursor<BsonDocument> cursor,
            List<List<String>> selectOrder,
            boolean sortFieldsAlphabetically,
            Integer statementId)
            throws SQLException {
        Preconditions.checkNotNull(cursor);
        // dateFormat is not thread safe, so we do not want to make it a static field.
        dateFormat.setTimeZone(UTC);
        // Only sort the columns alphabetically for SQL statement result sets and not for database metadata result sets.
        // The JDBC specification provides the order for each database metadata result set.
        // Because a lot BI tools will access database metadata columns by index, the specification order must be respected.
        this.cursor = cursor;
        try {
            this.current = cursor.next();
        } catch (NoSuchElementException e) {
            this.current = null;
            throw new SQLException("No current row in the result set.");
        }
    }

    // This is only used for testing, and that is why it has package level access, and the
    // tests have been moved into this package.
    BsonDocument getCurrent() {
        return current;
    }

    private void checkBounds(int i) throws SQLException {
        checkClosed();
        if (current == null) {
            throw new SQLException("No current row in the result set. Make sure to call next().");
        }
        if (i < 1 || i > current.size()) {
            throw new SQLException("Column index " + i + " is out of bounds.");
        }
    }

    @Override
    public boolean next() throws SQLException {
        checkClosed();

        boolean result;
        result = cursor.hasNext();
        if (result) {
            current = cursor.next();
            ++rowNum;
        }
        return result;
    }

    @Override
    public void close() throws SQLException {
        if (closed) {
            return;
        }
        cursor.close();
        closed = true;
    }

    @Override
    public boolean isLast() throws SQLException {
        checkClosed();
        return !cursor.hasNext();
    }

    private BsonValue getBsonValue(int columnIndex) throws SQLException {
        checkBounds(columnIndex);
        return current.get(current.keySet().toArray()[columnIndex]);
    }

    private BsonValue getBsonValue(String columnLabel) throws SQLException {
        return current.get(columnLabel);
    }

    private void checkClosed() throws SQLException {
        if (closed) throw new SQLException("MongoResultSet is closed.");
    }

    @Override
    public boolean wasNull() throws SQLException {
        checkClosed();
        return wasNull;
    }

    /**
     * Returns true if the Object obj is null. Crucially, it also must set the value of `wasNull`,
     * since that is part of the JDBC API.
     *
     * @param obj the object to check.
     * @return true if the object is Null, False otherwise.
     */
    private boolean checkNull(BsonValue obj) throws SQLException {
        // reset wasNull from previous check.
        wasNull = false;
        if (obj == null) {
            wasNull = true;
            return true;
        }
        if (obj.getBsonType() == BsonType.NULL || obj.getBsonType() == BsonType.UNDEFINED) {
            wasNull = true;
            return true;
        }
        return false;
    }

    @Deprecated
    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    private byte[] handleBytesConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to blob.");
    }

    private byte[] getBytes(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return null;
        }
        // we only allow getting Strings and Binaries as Bytes so that
        // we can conveniently ignore Endianess issues. Null and undefined
        // are still supported because Bytes's can be null.
        return switch (o.getBsonType()) {
            case BINARY -> o.asBinary().getData();
            case STRING -> o.asString().getValue().getBytes();
            case NULL, UNDEFINED -> null;
            default -> handleBytesConversionFailure(o.getBsonType().name());
        };
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getBytes(out);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getBytes(out);
    }

    @Override
    public java.io.InputStream getAsciiStream(int columnIndex) throws SQLException {
        final String encoding = "ASCII";
        try {
            return getNewByteArrayInputStream(getString(columnIndex).getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new SQLException("The JVM claims not to support the encoding: " + encoding + ".");
        }
    }

    @Override
    public java.io.InputStream getAsciiStream(String columnLabel) throws SQLException {
        final String encoding = "ASCII";
        try {
            return getNewByteArrayInputStream(getString(columnLabel).getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new SQLException("The JVM claims not to support the encoding: " + encoding + ".");
        }
    }

    @Deprecated
    @Override
    public java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException {
        final String encoding = "UTF-8";
        try {
            return getNewByteArrayInputStream(getString(columnIndex).getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new SQLException("The JVM claims not to support the encoding: " + encoding + ".");
        }
    }

    @Deprecated
    @Override
    public java.io.InputStream getUnicodeStream(String columnLabel) throws SQLException {
        final String encoding = "UTF-8";
        try {
            return getNewByteArrayInputStream(getString(columnLabel).getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new SQLException("The JVM claims not to support the encoding: " + encoding + ".");
        }
    }

    @Override
    public java.io.InputStream getBinaryStream(int columnIndex) throws SQLException {
        return getNewByteArrayInputStream(getBytes(columnIndex));
    }

    @Override
    public java.io.InputStream getBinaryStream(String columnLabel) throws SQLException {
        return getNewByteArrayInputStream(getBytes(columnLabel));
    }

    private String handleStringConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to string.");
    }

    private String getString(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return null;
        }
        return o.asString().getValue();
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getString(out);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getString(out);
    }

    private boolean handleBooleanConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to boolean.");
    }

    private boolean getBoolean(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return false;
        }
        switch (o.getBsonType()) {
            case BOOLEAN:
                return o.asBoolean().getValue();
            case DECIMAL128: {
                Decimal128 v = o.asDecimal128().getValue();
                return !Objects.equals(v, Decimal128.POSITIVE_ZERO)
                        && !Objects.equals(v, Decimal128.NEGATIVE_ZERO);
            }
            case DOUBLE:
                return o.asDouble().getValue() != 0.0;
            case INT32:
                return o.asInt32().getValue() != 0;
            case INT64:
                return o.asInt64().getValue() != 0;
            case NULL:
            case UNDEFINED:
                // this is consistent with $convert in mongodb insofar as getBoolean
                // returns false for null values.
                return false;
            case STRING:
                // mongodb $convert converts all strings to true, even the empty string.
                return true;
            default:
                return handleBooleanConversionFailure(o.getBsonType().name());
        }
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getBoolean(out);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getBoolean(out);
    }

    protected byte getByte(BsonValue o) throws SQLException {
        // Just be lazy, I doubt this will be called often.
        // HotSpot should inline these, anyway.
        return (byte) getLong(o);
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getByte(out);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getByte(out);
    }

    private short getShort(BsonValue o) throws SQLException {
        // Just be lazy, I doubt this will be called often.
        // HotSpot should inline these, anyway.
        return (short) getLong(o);
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getShort(out);
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getShort(out);
    }

    private int getInt(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return 0;
        }
        return (int) getLong(o);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getInt(out);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getInt(out);
    }

    private long handleLongConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to integral type.");
    }

    private long getLong(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return 0L;
        }
        switch (o.getBsonType()) {
            case BOOLEAN:
                return o.asBoolean().getValue() ? 1 : 0;
            case DATE_TIME:
                // This is what $convert does.
                return o.asDateTime().getValue();
            case DECIMAL128:
                return o.asDecimal128().longValue();
            case DOUBLE:
                return (long) o.asDouble().getValue();
            case INT32:
                return o.asInt32().getValue();
            case INT64:
                return o.asInt64().getValue();
            case NULL:
                return 0L;
            case STRING:
                try {
                    return Long.parseLong(o.asString().getValue());
                } catch (NumberFormatException e) {
                    throw new SQLException(e);
                }
            case UNDEFINED:
                // this is consistent with $convert in mongodb insofar as getLong
                // returns 0.0 for null values.
                return 0L;
            default:
                return handleLongConversionFailure(o.getBsonType().name());
        }
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getLong(out);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getLong(out);
    }

    private float getFloat(BsonValue o) throws SQLException {
        return (float) getDouble(o);
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getFloat(out);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getFloat(out);
    }

    private double handleDoubleConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to double.");
    }

    private double getDouble(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return 0.0;
        }
        switch (o.getBsonType()) {
            case BOOLEAN:
                return o.asBoolean().getValue() ? 1.0 : 0.0;
            case DATE_TIME:
                // This is what $convert does.
                return (double) o.asDateTime().getValue();
            case DECIMAL128:
                return o.asDecimal128().doubleValue();
            case DOUBLE:
                return o.asDouble().getValue();
            case INT32:
                return o.asInt32().getValue();
            case INT64:
                return (double) o.asInt64().getValue();
            case STRING:
                try {
                    return Double.parseDouble(o.asString().getValue());
                } catch (NumberFormatException e) {
                    throw new SQLException(e);
                }
            case NULL:
            case UNDEFINED:
                // this is consistent with $convert in mongodb insofar as getDouble
                // returns 0.0 for null values.
                return 0.0;
            default:
                return handleDoubleConversionFailure(o.getBsonType().name());
        }
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getDouble(out);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getDouble(out);
    }

    @Deprecated
    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    // Advanced features:

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkClosed();
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkClosed();
    }

    @Override
    public String getCursorName() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    private Object getObject(BsonValue o, int columnType) throws SQLException {
        // If the value is an SQL NULL, the driver returns a Java null.
        if (checkNull(o)) {
            return null;
        }
        switch (columnType) {
            case Types.BIGINT:
                return getLong(o);
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                return getInt(o);
            case Types.BINARY:
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
                return o.asBinary().getData();
            case Types.BIT:
            case Types.BOOLEAN:
                return getBoolean(o);
            case Types.DOUBLE:
            case Types.FLOAT:
                return getDouble(o);
            case Types.DECIMAL:
            case Types.NUMERIC:
                return o.asDecimal128().decimal128Value().bigDecimalValue();
            case Types.CHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.VARCHAR:
                return getString(o);
            case Types.REAL:
                return getFloat(o);
            case Types.TIMESTAMP:
                return new Timestamp(o.asDateTime().getValue());
            case Types.NULL:
                return null;

            case Types.OTHER:
                if (o.getBsonType() == BsonType.NULL) {
                    return null;
                }
                // These types are wrapped in MongoBsonValue so that
                // if they are stringified via toString() they will be
                // represented as extended JSON.
                return o;

            case Types.ARRAY:
            case Types.BLOB:
            case Types.CLOB:
            case Types.DATALINK:
            case Types.DATE:
            case Types.DISTINCT:
            case Types.JAVA_OBJECT:
            case Types.NCLOB:
            case Types.REF:
            case Types.REF_CURSOR:
            case Types.ROWID:
            case Types.SQLXML:
            case Types.STRUCT:
            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                // not supported
                break;
        }
        throw new SQLException("getObject not supported for column type " + columnType);
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getObject(out, out.getBsonType().getValue());
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        int columnIndex = findColumn(columnLabel);
        return getObject(columnIndex);
    }

    @Override
    public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map)
            throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        if (checkNull(out)) {
            return null;
        }
        Class<?> type = map.get(out.getBsonType().name());
        if (type == null) {
            return null;
        }
        return type.cast(out);
    }

    @Override
    public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map)
            throws SQLException {
        return getObject(findColumn(columnLabel), map);
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        if (checkNull(out)) {
            return null;
        }
        return type.cast(out);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return getObject(findColumn(columnLabel), type);
    }

    // ----------------------------------------------------------------

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        checkClosed();
        if (!current.containsKey(columnLabel)) {
            throw new SQLException("No such column: '" + columnLabel + "'.");
        }
        try {
            return current.keySet().stream().toList().indexOf(columnLabel) + 1;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    // --------------------------JDBC 2.0-----------------------------------

    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------

    @Override
    public java.io.Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public java.io.Reader getCharacterStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    private BigDecimal handleBigDecimalConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to BigDecimal.");
    }

    private BigDecimal getBigDecimal(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return BigDecimal.ZERO;
        }
        switch (o.getBsonType()) {
            case BOOLEAN:
                return o.asBoolean().getValue() ? BigDecimal.ONE : BigDecimal.ZERO;
            case DATE_TIME:
                // This is what $convert does.
                return new BigDecimal(o.asDateTime().getValue());
            case DECIMAL128:
                return o.asDecimal128().decimal128Value().bigDecimalValue();
            case DOUBLE:
                return BigDecimal.valueOf(o.asDouble().getValue());
            case INT32:
                return new BigDecimal(o.asInt32().getValue());
            case INT64:
                return new BigDecimal(o.asInt64().getValue());
            case NULL:
            case UNDEFINED:
                // this is consistent with $convert in mongodb insofar as getBigDecimal
                // returns 0.0 for null values.
                return BigDecimal.ZERO;
            case STRING:
                try {
                    return new BigDecimal(o.asString().getValue());
                } catch (NumberFormatException | ArithmeticException e) {
                    throw new SQLException(e);
                }
            default:
                return handleBigDecimalConversionFailure(o.getBsonType().name());
        }
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getBigDecimal(out);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getBigDecimal(out);
    }

    // ---------------------------------------------------------------------
    // Traversal/Positioning
    // ---------------------------------------------------------------------

    @Override
    public boolean isBeforeFirst() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean isFirst() throws SQLException {
        checkClosed();
        return rowNum == 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void afterLast() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean first() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean last() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public int getRow() throws SQLException {
        checkClosed();
        return rowNum;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean previous() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    // ---------------------------------------------------------------------
    // Properties
    // ---------------------------------------------------------------------

    @Override
    public int getFetchDirection() throws SQLException {
        checkClosed();
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public int getType() throws SQLException {
        checkClosed();
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        checkClosed();
        return ResultSet.CONCUR_READ_ONLY;
    }

    // ---------------------------------------------------------------------
    // Updates
    // ---------------------------------------------------------------------

    @Override
    public boolean rowUpdated() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateAsciiStream(String columnLabel, java.io.InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBinaryStream(String columnLabel, java.io.InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateCharacterStream(String columnLabel, java.io.Reader reader, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public Statement getStatement() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    protected Blob getNewBlob(byte[] bytes) throws SQLException {
        if (bytes == null) {
            return null;
        }
        try {
            return new SerialBlob(bytes);
        } catch (SerialException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getNewBlob(getBytes(out));
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getNewBlob(getBytes(out));
    }

    protected Clob getClob(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return null;
        }
        return new SerialClob(Objects.requireNonNull(getString(o)).toCharArray());
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getClob(out);
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getClob(out);
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    private java.util.Date handleUtilDateConversionFailure(String from) throws SQLException {
        throw new SQLException("The " + from + " type cannot be converted to java.util.Date");
    }

    private java.util.Date getUtilDate(BsonValue o) throws SQLException {
        if (checkNull(o)) {
            return null;
        }
        switch (o.getBsonType()) {
            case DATE_TIME:
                return new java.util.Date(o.asDateTime().getValue());
            case DECIMAL128:
                return new Date(o.asDecimal128().longValue());
            case DOUBLE:
                return new Date((long) o.asDouble().getValue());
            case INT32:
                return new Date(o.asInt32().getValue());
            case INT64:
                return new Date(o.asInt64().getValue());
            case UNDEFINED:
                // this is consistent with $convert in mongodb.
            case NULL:
                return null;
            case STRING:
                try {
                    return dateFormat.parse(o.asString().getValue());
                } catch (ParseException e) {
                    throw new SQLException(e);
                }
            default:
                return handleUtilDateConversionFailure(o.getBsonType().name());
        }
    }

    private Date getDate(BsonValue o) throws SQLException {
        java.util.Date utilDate = getUtilDate(o);
        return (utilDate == null) ? null : new Date(utilDate.getTime());
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getDate(out);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getDate(out);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        Date d = getDate(columnIndex);
        if (d == null) {
            return null;
        }
        cal.setTime(d);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        Date d = getDate(columnLabel);
        if (d == null) {
            return null;
        }
        cal.setTime(d);
        return new Date(cal.getTime().getTime());
    }

    protected Time getTime(BsonValue o) throws SQLException {
        java.util.Date utilDate = getUtilDate(o);
        return (utilDate == null) ? null : new Time(utilDate.getTime());
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getTime(out);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getTime(out);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        Time d = getTime(columnIndex);
        if (d == null) {
            return null;
        }
        cal.setTime(d);
        return new Time(cal.getTime().getTime());
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        Time d = getTime(columnLabel);
        if (d == null) {
            return null;
        }
        cal.setTime(d);
        return new Time(cal.getTime().getTime());
    }

    protected Timestamp getTimestamp(BsonValue o) throws SQLException {
        java.util.Date utilDate = getUtilDate(o);
        return (utilDate == null) ? null : new Timestamp(utilDate.getTime());
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        BsonValue out = getBsonValue(columnLabel);
        return getTimestamp(out);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        BsonValue out = getBsonValue(columnIndex);
        return getTimestamp(out);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        Timestamp d = getTimestamp(columnIndex);
        if (d == null) {
            return null;
        }
        cal.setTime(d);
        return new Timestamp(cal.getTime().getTime());
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        Timestamp d = getTimestamp(columnLabel);
        if (d == null) {
            return null;
        }
        cal.setTime(d);
        return new Timestamp(cal.getTime().getTime());
    }

    // -------------------------- JDBC 3.0 ----------------------------------------

    @Override
    public java.net.URL getURL(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public java.net.URL getURL(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateRef(int columnIndex, java.sql.Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateRef(String columnLabel, java.sql.Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBlob(int columnIndex, java.sql.Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBlob(String columnLabel, java.sql.Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateClob(int columnIndex, java.sql.Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateClob(String columnLabel, java.sql.Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateArray(int columnIndex, java.sql.Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateArray(String columnLabel, java.sql.Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    // ------------------------- JDBC 4.0 -----------------------------------

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return getString(columnIndex);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        return getString(columnLabel);
    }

    @Override
    public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException {
        return new java.io.StringReader(getString(columnIndex));
    }

    @Override
    public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException {
        return new java.io.StringReader(getString(columnLabel));
    }

    @Override
    public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateAsciiStream(String columnLabel, java.io.InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBinaryStream(String columnLabel, java.io.InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    // ---

    @Override
    public void updateNCharacterStream(int columnIndex, java.io.Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNCharacterStream(String columnLabel, java.io.Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateAsciiStream(String columnLabel, java.io.InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBinaryStream(String columnLabel, java.io.InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateCharacterStream(String columnLabel, java.io.Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    // ------------------------- JDBC 4.2 -----------------------------------

    @Override
    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    @Override
    public void updateObject(String columnLabel, Object x, SQLType targetSqlType)
            throws SQLException {
        throw new SQLFeatureNotSupportedException(
                Thread.currentThread().getStackTrace()[1].toString());
    }

    // java.sql.Wrapper impl
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return (T) this;
    }
}