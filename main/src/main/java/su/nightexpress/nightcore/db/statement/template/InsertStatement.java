package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.statement.ColumnMapping;
import su.nightexpress.nightcore.db.statement.condition.Wheres;

import java.util.List;
import java.util.stream.Collectors;

public final class InsertStatement<T> extends UpsertStatement<T> {

    private String sqlTemplate;

    private InsertStatement(@NotNull List<String> columns, @NotNull List<ColumnMapping<T, ?>> columnMappings) {
        super(columns, columnMappings);
    }

    @NotNull
    public static <T> Builder<T> builder(@NotNull Class<T> type) {
        return new Builder<>();
    }

    @NotNull
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @Override
    @NotNull
    public String toSql(@NotNull String table, @Nullable Wheres<T> where) {
        if (this.sqlTemplate == null) {
            String columns = String.join(",", this.columns);
            String values = this.columns.stream().map(value -> "?").collect(Collectors.joining(","));

            this.sqlTemplate = "INSERT INTO %s (" + columns + ")" + " VALUES(" + values + ")";
        }
        return this.sqlTemplate.formatted(table);
    }

    public static class Builder<T> extends AbstractBuilder<Builder<T>, InsertStatement<T>, T> {

        Builder() {

        }

        @Override
        @NotNull
        public InsertStatement<T> build() {
            return new InsertStatement<>(this.columns, this.columnMappings);
        }

        @Override
        @NotNull
        protected Builder<T> getThis() {
            return this;
        }
    }
}
