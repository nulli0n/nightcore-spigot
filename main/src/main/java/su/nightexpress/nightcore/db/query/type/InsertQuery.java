package su.nightexpress.nightcore.db.query.type;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.db.query.TypedQuery;
import su.nightexpress.nightcore.db.query.data.Values;
import su.nightexpress.nightcore.db.query.data.Wheres;
import su.nightexpress.nightcore.db.sql.column.Column;

import java.util.function.Function;

@Deprecated
public class InsertQuery<T> extends TypedQuery<T> {

    private final Values<T> values;

    public InsertQuery() {
        this.values = Values.forInsert();
    }

    @Override
    @NonNull
    public String toSQL(@NonNull String table) {
        String columns = this.values.toSQL();

        StringBuilder values = new StringBuilder();
        for (int i = 0; i < this.values.count(); i++) {
            if (!values.isEmpty()) values.append(",");
            values.append("?");
        }

        return "INSERT INTO " + table + " (" + columns + ")" + " VALUES(" + values + ")";
    }

    @Override
    public boolean isEmpty() {
        return this.values.count() == 0;
    }

    @Override
    @Nullable
    protected Wheres<T> statementWheres() {
        return null;
    }

    @Override
    @Nullable
    protected Values<T> statementValues() {
        return this.values;
    }

    @NonNull
    public InsertQuery<T> setValue(@NonNull Column column, @NonNull Function<T, String> function) {
        this.values.setValue(column, function);
        return this;
    }
}
