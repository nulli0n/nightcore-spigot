package su.nightexpress.nightcore.db.query.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.query.Query;
import su.nightexpress.nightcore.db.query.data.Wheres;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Deprecated
public class SelectQuery<T> implements Query {

    private final List<String>           columns;
    private final Wheres<Object>         wheres;
    private final Function<ResultSet, T> rowMapper;

    private int amount;

    public SelectQuery(@NotNull Function<ResultSet, T> rowMapper) {
        this.columns = new ArrayList<>();
        this.wheres = new Wheres<>();
        this.rowMapper = rowMapper;
        this.amount = -1;
    }
    
    @Override
    @NotNull
    public String toSQL(@NotNull String table) {
        String columns = String.join(",", this.columns);

        return "SELECT " + columns + " FROM " + table + " " + this.wheres.toSQL();
    }

    @Override
    public boolean isEmpty() {
        return this.columns.isEmpty();
    }

    @NotNull
    public List<T> execute(@NotNull AbstractConnector connector, @NotNull String table) throws SQLException {
        if (this.isEmpty()) return Collections.emptyList();

        String sql = this.toSQL(table).trim();

        try (Connection connection = connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int paramCount = 1;

            for (int index = 0; index < this.wheres.count(); index++) {
                statement.setString(paramCount++, this.wheres.getValue(new Object(), index));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> list = new ArrayList<>();
                while (resultSet.next() && (this.amount < 0 || list.size() < this.amount)) {
                    T object = this.rowMapper.apply(resultSet);
                    if (object == null) continue;

                    list.add(object);
                }
                return list;
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Collections.emptyList();
    }

    @NotNull
    public SelectQuery<T> limit(int amount) {
        this.amount = Math.abs(amount);
        return this;
    }

    @NotNull
    public SelectQuery<T> all() {
        this.columns.clear();
        this.columns.add("*");
        return this;
    }

    @NotNull
    public SelectQuery<T> column(@NotNull Column column) {
        this.columns.add(column.getNameEscaped());
        return this;
    }

    @NotNull
    public SelectQuery<T> where(@NotNull Column column, @NotNull WhereOperator operator, @NotNull String string) {
        this.wheres.where(column, operator, string);
        return this;
    }

    @NotNull
    public SelectQuery<T> whereIgnoreCase(@NotNull Column column, @NotNull WhereOperator operator, @NotNull String string) {
        this.wheres.whereIgnoreCase(column, operator, string);
        return this;
    }
}
