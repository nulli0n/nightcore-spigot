package su.nightexpress.nightcore.db.query;

import org.jspecify.annotations.NonNull;

@Deprecated
public interface Query {

    @NonNull
    String toSQL(@NonNull String table);

    boolean isEmpty();
}
