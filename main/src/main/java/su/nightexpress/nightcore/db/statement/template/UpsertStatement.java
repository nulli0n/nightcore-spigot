package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.ColumnMapping;
import su.nightexpress.nightcore.db.statement.ParameterBinder;
import su.nightexpress.nightcore.db.statement.PropertyAccessor;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.type.BatchStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class UpsertStatement<T> implements BatchStatement<T> {

    protected final List<String>              columns;
    protected final List<ColumnMapping<T, ?>> columnMappings;

    protected UpsertStatement(@NotNull List<String> columns, @NotNull List<ColumnMapping<T, ?>> columnMappings) {
        this.columns = columns;
        this.columnMappings = columnMappings;
    }

    @Override
    public void prepare(@NotNull PreparedStatement statement, @NotNull T entity, @Nullable Wheres<T> where) throws SQLException {
        int paramCount = 1;

        for (ColumnMapping<T, ?> columnMapping : this.columnMappings) {
            columnMapping.apply(statement, paramCount++, entity);
        }

        if (where != null && !where.isEmpty()) {
            for (PropertyAccessor<T, Object> parameter : where.getParameters()) {
                statement.setObject(paramCount++, parameter.access(entity));
            }
        }
    }

    public abstract static class AbstractBuilder<B extends AbstractBuilder<B, S, T>, S extends UpsertStatement<T>, T> {

        protected final List<String>              columns;
        protected final List<ColumnMapping<T, ?>> columnMappings;

        AbstractBuilder() {
            this.columns = new ArrayList<>();
            this.columnMappings = new ArrayList<>();
        }

        @NotNull
        public abstract S build();

        @NotNull
        protected abstract B getThis();

        @NotNull
        public B setInt(@NotNull Column<?> column, @NotNull PropertyAccessor<T, Integer> accessor) {
            return this.set(column, accessor, ParameterBinder.INT);
        }

        @NotNull
        public B setInt(@NotNull String column, @NotNull PropertyAccessor<T, Integer> accessor) {
            return this.set(column, accessor, ParameterBinder.INT);
        }

        @NotNull
        public B setLong(@NotNull Column<?> column, @NotNull PropertyAccessor<T, Long> accessor) {
            return this.set(column, accessor, ParameterBinder.LONG);
        }

        @NotNull
        public B setLong(@NotNull String column, @NotNull PropertyAccessor<T, Long> accessor) {
            return this.set(column, accessor, ParameterBinder.LONG);
        }

        @NotNull
        public B setDouble(@NotNull Column<?> column, @NotNull PropertyAccessor<T, Double> accessor) {
            return this.set(column, accessor, ParameterBinder.DOUBLE);
        }

        @NotNull
        public B setDouble(@NotNull String column, @NotNull PropertyAccessor<T, Double> accessor) {
            return this.set(column, accessor, ParameterBinder.DOUBLE);
        }

        @NotNull
        public B setFloat(@NotNull Column<?> column, @NotNull PropertyAccessor<T, Float> accessor) {
            return this.set(column, accessor, ParameterBinder.FLOAT);
        }

        @NotNull
        public B setFloat(@NotNull String column, @NotNull PropertyAccessor<T, Float> accessor) {
            return this.set(column, accessor, ParameterBinder.FLOAT);
        }

        @NotNull
        public B setBoolean(@NotNull Column<?> column, @NotNull PropertyAccessor<T, Boolean> accessor) {
            return this.set(column, accessor, ParameterBinder.BOOLEAN);
        }

        @NotNull
        public B setBoolean(@NotNull String column, @NotNull PropertyAccessor<T, Boolean> accessor) {
            return this.set(column, accessor, ParameterBinder.BOOLEAN);
        }

        @NotNull
        public B setString(@NotNull Column<?> column, @NotNull PropertyAccessor<T, String> accessor) {
            return this.set(column, accessor, ParameterBinder.STRING);
        }

        @NotNull
        public B setString(@NotNull String column, @NotNull PropertyAccessor<T, String> accessor) {
            return this.set(column, accessor, ParameterBinder.STRING);
        }

        @NotNull
        public B setObject(@NotNull Column<?> column, @NotNull PropertyAccessor<T, Object> accessor) {
            return this.set(column, accessor, ParameterBinder.GENERIC);
        }

        @NotNull
        public B setObject(@NotNull String column, @NotNull PropertyAccessor<T, Object> accessor) {
            return this.set(column, accessor, ParameterBinder.GENERIC);
        }

        @NotNull
        public <R> B set(@NotNull Column<?> column, @NotNull PropertyAccessor<T, R> accessor, @NotNull ParameterBinder<R> binder) {
            return this.set(column.getQuotedName(), accessor, binder);
        }

        @NotNull
        public <R> B set(@NotNull String column, @NotNull PropertyAccessor<T, R> accessor, @NotNull ParameterBinder<R> binder) {
            this.columns.add(column);
            this.columnMappings.add(new ColumnMapping<>(accessor, binder));
            return this.getThis();
        }
    }
}
