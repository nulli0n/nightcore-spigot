package su.nightexpress.nightcore.db.sql.query.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.sql.column.Column;

import java.util.function.Function;

@Deprecated
public interface ValuedQuery<Q extends ValuedQuery<Q, T>, T> {

    @NotNull Q setValue(@NotNull Column column, @NotNull Function<T, String> function);
}
