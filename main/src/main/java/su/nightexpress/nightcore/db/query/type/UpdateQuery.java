package su.nightexpress.nightcore.db.query.type;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.db.query.TypedQuery;
import su.nightexpress.nightcore.db.query.data.Values;
import su.nightexpress.nightcore.db.query.data.Wheres;
import su.nightexpress.nightcore.db.sql.column.Column;
import su.nightexpress.nightcore.db.sql.util.WhereOperator;

import java.util.function.Function;

@Deprecated
public class UpdateQuery<T> extends TypedQuery<T> {

    private final Values<T> values;
    private final Wheres<T> wheres;

    public UpdateQuery() {
        this.values = Values.forUpdate();
        this.wheres = new Wheres<>();
    }

    @Override
    @NonNull
    public String toSQL(@NonNull String table) {
        String columns = this.values.toSQL();

        return "UPDATE " + table + " SET " + columns + " " + this.wheres.toSQL();
    }

    @Override
    public boolean isEmpty() {
        return this.values.count() == 0;
    }

    @Override
    @Nullable
    protected Values<T> statementValues() {
        return this.values;
    }

    @Override
    @Nullable
    protected Wheres<T> statementWheres() {
        return this.wheres;
    }

    @NonNull
    public String getDataValue(@NonNull T entity, int index) {
        return this.values.getValue(entity, index);
    }

    @NonNull
    public UpdateQuery<T> setValue(@NonNull Column column, @NonNull Function<T, String> function) {
        this.values.setValue(column, function);
        return this;
    }

    @NonNull
    public UpdateQuery<T> where(@NonNull Column column, @NonNull WhereOperator operator, @NonNull String string) {
        return this.where(column, operator, o -> string);
    }

    @NonNull
    public UpdateQuery<T> where(@NonNull Column column, @NonNull WhereOperator operator,
                                @NonNull Function<T, String> function) {
        this.wheres.where(column, operator, function);
        return this;
    }

    @NonNull
    public UpdateQuery<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                                          @NonNull String string) {
        return this.whereIgnoreCase(column, operator, o -> string);
    }

    @NonNull
    public UpdateQuery<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                                          @NonNull Function<T, String> function) {
        this.wheres.whereIgnoreCase(column, operator, function);
        return this;
    }
}
