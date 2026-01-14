package su.nightexpress.nightcore.db.query.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    @NotNull
    public String toSQL(@NotNull String table) {
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

    @NotNull
    public DeleteQuery<T> where(@NotNull Column column, @NotNull WhereOperator operator, @NotNull String string) {
        return this.where(column, operator, o -> string);
    }

    @NotNull
    public DeleteQuery<T> where(@NotNull Column column, @NotNull WhereOperator operator, @NotNull Function<T, String> function) {
        this.wheres.where(column, operator, function);
        return this;
    }

    @NotNull
    public DeleteQuery<T> whereIgnoreCase(@NotNull Column column, @NotNull WhereOperator operator, @NotNull String string) {
        return this.whereIgnoreCase(column, operator, o -> string);
    }

    @NotNull
    public DeleteQuery<T> whereIgnoreCase(@NotNull Column column, @NotNull WhereOperator operator, @NotNull Function<T, String> function) {
        this.wheres.whereIgnoreCase(column, operator, function);
        return this;
    }
}
