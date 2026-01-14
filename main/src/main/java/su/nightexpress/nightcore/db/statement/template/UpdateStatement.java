package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.statement.ColumnMapping;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.util.List;
import java.util.stream.Collectors;

public final class UpdateStatement<T> extends UpsertStatement<T> {

    private UpdateStatement(@NotNull List<String> columns, @NotNull List<ColumnMapping<T, ?>> columnMappings) {
        super(columns, columnMappings);
    }

    @NotNull
    public static <T> UpdateStatement.Builder<T> builder(@NotNull Class<T> type) {
        return new Builder<>();
    }

    @NotNull
    public static <T> UpdateStatement.Builder<T> builder() {
        return new UpdateStatement.Builder<>();
    }

    @Override
    @NotNull
    public String toSql(@NotNull String table, @Nullable Wheres<T> where) {
        String columns = this.columns.stream().map(col -> col + " = ?").collect(Collectors.joining(","));

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(table).append(" SET ").append(columns);
        if (where != null && !where.isEmpty()) {
            builder.append(" WHERE ").append(where.toSql());
        }
        return builder.toString();
    }

    public static class Builder<T> extends AbstractBuilder<Builder<T>, UpdateStatement<T>, T> {

        Builder() {

        }

        @NotNull
        public UpdateStatement<T> build() {
            return new UpdateStatement<>(this.columns, this.columnMappings);
        }

        @Override
        @NotNull
        protected Builder<T> getThis() {
            return this;
        }
    }
}
