package su.nightexpress.nightcore.db.statement;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PropertyAccessor<T, R> {

    @NotNull R access(@NotNull T entity);
}
