package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.type.QueryStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SelectStatement<R> implements QueryStatement<R> {

    private final RowMapper<R> rowMapper;
    private final List<String> columns;

    public SelectStatement(@NotNull RowMapper<R> rowMapper, @NotNull List<String> columns) {
        this.rowMapper = rowMapper;
        this.columns = columns;
    }

    @NotNull
    public static <R> Builder<R> builder(@NotNull RowMapper<R> mapper) {
        return new Builder<>(mapper);
    }

    @Override
    @NotNull
    public String toSql(@NotNull String table, @Nullable Wheres where, @Nullable Integer limit) {
        String columns = this.columns.isEmpty() ? "*" : String.join(",", this.columns);
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT ").append(columns).append(" FROM ").append(table);
        if (where != null && !where.isEmpty()) {
            builder.append(" WHERE ").append(where.toSql());
        }
        if (limit != null) {
            builder.append(" LIMIT ").append(limit);
        }
        return builder.toString();
    }

    @Override
    @Nullable
    public R map(@NotNull ResultSet resultSet) throws SQLException {
        return this.rowMapper.map(resultSet);
    }

    public static class Builder<R> {

        private final RowMapper<R> rowMapper;
        private final List<String> columns;

        public Builder(@NotNull RowMapper<R> rowMapper) {
            this.rowMapper = rowMapper;
            this.columns = new ArrayList<>();
        }

        @NotNull
        public SelectStatement<R> build() {
            return new SelectStatement<>(this.rowMapper, this.columns);
        }

        @NotNull
        public Builder<R> column(@NotNull Column<?>... columns) {
            for (Column<?> column : columns) {
                this.columns.add(column.getQuotedName());
            }
            return this;
        }

        @NotNull
        public Builder<R> column(@NotNull String... columns) {
            this.columns.addAll(Arrays.asList(columns));
            return this;
        }
    }
}
