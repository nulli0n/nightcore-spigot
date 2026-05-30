package su.nightexpress.nightcore.db.statement;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface PropertyAccessor<T, R> {

    @Nullable
    R access(@NonNull T entity);
}
