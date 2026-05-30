package su.nightexpress.nightcore.db.statement.template;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    public SelectStatement(@NonNull RowMapper<R> rowMapper, @NonNull List<String> columns) {
        this.rowMapper = rowMapper;
        this.columns = columns;
    }

    @NonNull
    public static <R> Builder<R> builder(@NonNull RowMapper<R> mapper) {
        return new Builder<>(mapper);
    }

    @Override
    @NonNull
    public String toSql(@NonNull String table, @Nullable Wheres where, @Nullable Integer limit) {
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
    public R map(@NonNull ResultSet resultSet) throws SQLException {
        return this.rowMapper.map(resultSet);
    }

    public static class Builder<R> {

        private final RowMapper<R> rowMapper;
        private final List<String> columns;

        public Builder(@NonNull RowMapper<R> rowMapper) {
            this.rowMapper = rowMapper;
            this.columns = new ArrayList<>();
        }

        @NonNull
        public SelectStatement<R> build() {
            return new SelectStatement<>(this.rowMapper, this.columns);
        }

        @NonNull
        public Builder<R> column(@NonNull Column<?>... columns) {
            for (Column<?> column : columns) {
                this.columns.add(column.getQuotedName());
            }
            return this;
        }

        @NonNull
        public Builder<R> column(@NonNull String... columns) {
            this.columns.addAll(Arrays.asList(columns));
            return this;
        }
    }
}
