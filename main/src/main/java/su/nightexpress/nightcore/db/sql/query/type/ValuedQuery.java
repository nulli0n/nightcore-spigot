package su.nightexpress.nightcore.db.sql.query.type;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.db.sql.column.Column;

import java.util.function.Function;

@Deprecated
public interface ValuedQuery<Q extends ValuedQuery<Q, T>, T> {

    @NonNull
    Q setValue(@NonNull Column column, @NonNull Function<T, String> function);
}
