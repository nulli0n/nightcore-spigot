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
public class DeleteQuery<T> extends TypedQuery<T> {

    private final Wheres<T> wheres;

    public DeleteQuery() {
        this.wheres = new Wheres<>();
    }

    @Override
    @NonNull
    public String toSQL(@NonNull String table) {
        return "DELETE FROM " + table + " " + this.wheres.toSQL();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @Nullable
    protected Wheres<T> statementWheres() {
        return this.wheres;
    }

    @Override
    @Nullable
    protected Values<T> statementValues() {
        return null;
    }

    @NonNull
    public DeleteQuery<T> where(@NonNull Column column, @NonNull WhereOperator operator, @NonNull String string) {
        return this.where(column, operator, o -> string);
    }

    @NonNull
    public DeleteQuery<T> where(@NonNull Column column, @NonNull WhereOperator operator,
                                @NonNull Function<T, String> function) {
        this.wheres.where(column, operator, function);
        return this;
    }

    @NonNull
    public DeleteQuery<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                                          @NonNull String string) {
        return this.whereIgnoreCase(column, operator, o -> string);
    }

    @NonNull
    public DeleteQuery<T> whereIgnoreCase(@NonNull Column column, @NonNull WhereOperator operator,
                                          @NonNull Function<T, String> function) {
        this.wheres.whereIgnoreCase(column, operator, function);
        return this;
    }
}
