package su.nightexpress.nightcore.db.statement.template;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
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
import java.util.UUID;

public abstract class UpsertStatement<T> implements BatchStatement<T> {

    protected final List<String>              columns;
    protected final List<ColumnMapping<T, ?>> columnMappings;

    protected UpsertStatement(@NonNull List<String> columns, @NonNull List<ColumnMapping<T, ?>> columnMappings) {
        this.columns = columns;
        this.columnMappings = columnMappings;
    }

    @Override
    public void prepare(@NonNull PreparedStatement statement, @NonNull T entity, @Nullable Wheres<T> where) throws SQLException {
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
        protected final List<String>              autoDetectedConflictKeys;

        AbstractBuilder() {
            this.columns = new ArrayList<>();
            this.columnMappings = new ArrayList<>();
            this.autoDetectedConflictKeys = new ArrayList<>();
        }

        @NonNull
        public abstract S build();

        @NonNull
        protected abstract B getThis();

        @NonNull
        public B setInt(@NonNull Column<?> column, @NonNull PropertyAccessor<T, Integer> accessor) {
            return this.set(column, accessor, ParameterBinder.INT);
        }

        @NonNull
        public B setInt(@NonNull String column, @NonNull PropertyAccessor<T, Integer> accessor) {
            return this.set(column, accessor, ParameterBinder.INT);
        }

        @NonNull
        public B setLong(@NonNull Column<?> column, @NonNull PropertyAccessor<T, Long> accessor) {
            return this.set(column, accessor, ParameterBinder.LONG);
        }

        @NonNull
        public B setLong(@NonNull String column, @NonNull PropertyAccessor<T, Long> accessor) {
            return this.set(column, accessor, ParameterBinder.LONG);
        }

        @NonNull
        public B setDouble(@NonNull Column<?> column, @NonNull PropertyAccessor<T, Double> accessor) {
            return this.set(column, accessor, ParameterBinder.DOUBLE);
        }

        @NonNull
        public B setDouble(@NonNull String column, @NonNull PropertyAccessor<T, Double> accessor) {
            return this.set(column, accessor, ParameterBinder.DOUBLE);
        }

        @NonNull
        public B setFloat(@NonNull Column<?> column, @NonNull PropertyAccessor<T, Float> accessor) {
            return this.set(column, accessor, ParameterBinder.FLOAT);
        }

        @NonNull
        public B setFloat(@NonNull String column, @NonNull PropertyAccessor<T, Float> accessor) {
            return this.set(column, accessor, ParameterBinder.FLOAT);
        }

        @NonNull
        public B setBoolean(@NonNull Column<?> column, @NonNull PropertyAccessor<T, Boolean> accessor) {
            return this.set(column, accessor, ParameterBinder.BOOLEAN);
        }

        @NonNull
        public B setBoolean(@NonNull String column, @NonNull PropertyAccessor<T, Boolean> accessor) {
            return this.set(column, accessor, ParameterBinder.BOOLEAN);
        }

        @NonNull
        public B setString(@NonNull Column<?> column, @NonNull PropertyAccessor<T, String> accessor) {
            return this.set(column, accessor, ParameterBinder.STRING);
        }

        @NonNull
        public B setString(@NonNull String column, @NonNull PropertyAccessor<T, String> accessor) {
            return this.set(column, accessor, ParameterBinder.STRING);
        }

        @NonNull
        public B setUUID(@NonNull Column<?> column, @NonNull PropertyAccessor<T, UUID> accessor) {
            return this.set(column, accessor, ParameterBinder.UUID);
        }

        @NonNull
        public B setUUID(@NonNull String column, @NonNull PropertyAccessor<T, UUID> accessor) {
            return this.set(column, accessor, ParameterBinder.UUID);
        }

        @NonNull
        public B setObject(@NonNull Column<?> column, @NonNull PropertyAccessor<T, Object> accessor) {
            return this.set(column, accessor, ParameterBinder.GENERIC);
        }

        @NonNull
        public B setObject(@NonNull String column, @NonNull PropertyAccessor<T, Object> accessor) {
            return this.set(column, accessor, ParameterBinder.GENERIC);
        }

        @NonNull
        public <R> B set(@NonNull Column<?> column, @NonNull PropertyAccessor<T, R> accessor, @NonNull ParameterBinder<R> binder) {
            if (column.isPrimaryKey() || column.isUnique()) {
                this.autoDetectedConflictKeys.add(column.getQuotedName());
            }
            return this.set(column.getQuotedName(), accessor, binder);
        }

        @NonNull
        private <R> B set(@NonNull String column, @NonNull PropertyAccessor<T, R> accessor, @NonNull ParameterBinder<R> binder) {
            this.columns.add(column);
            this.columnMappings.add(new ColumnMapping<>(accessor, binder));
            return this.getThis();
        }
    }
}
