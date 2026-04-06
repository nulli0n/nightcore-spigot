package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.statement.ColumnMapping;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.util.List;
import java.util.stream.Collectors;

public final class UpdateStatement<T> extends UpsertStatement<T> {

    private UpdateStatement(@NonNull List<String> columns, @NonNull List<ColumnMapping<T, ?>> columnMappings) {
        super(columns, columnMappings);
    }

    public static <T> UpdateStatement.@NonNull Builder<T> builder(@NonNull Class<T> type) {
        return new Builder<>();
    }

    public static <T> UpdateStatement.@NonNull Builder<T> builder() {
        return new UpdateStatement.Builder<>();
    }

    @Override
    @NonNull
    public String toSql(@NonNull DatabaseType type, @NonNull String table, @Nullable Wheres<T> where) {
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

        @NonNull
        public UpdateStatement<T> build() {
            return new UpdateStatement<>(this.columns, this.columnMappings);
        }

        @Override
        @NonNull
        protected Builder<T> getThis() {
            return this;
        }
    }
}
