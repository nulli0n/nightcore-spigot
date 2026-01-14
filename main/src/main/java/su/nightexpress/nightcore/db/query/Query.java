package su.nightexpress.nightcore.db.query;

import org.jetbrains.annotations.NotNull;

@Deprecated
public interface Query {

    @NotNull String toSQL(@NotNull String table);

    boolean isEmpty();
}
