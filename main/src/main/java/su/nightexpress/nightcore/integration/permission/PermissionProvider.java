package su.nightexpress.nightcore.integration.permission;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PermissionProvider {

    //void setup();

    @NonNull
    String getName();

    @Nullable
    String getPrimaryGroup(@NonNull Player player);

    @NonNull
    Set<String> getPermissionGroups(@NonNull Player player);

    @Nullable
    String getPrefix(@NonNull Player player);

    @Nullable
    String getSuffix(@NonNull Player player);

    @NonNull
    CompletableFuture<String> getPrimaryGroup(@NonNull UUID playerId);

    @NonNull
    CompletableFuture<Set<String>> getPermissionGroups(@NonNull UUID playerId);

    @NonNull
    CompletableFuture<String> getPrefix(@NonNull UUID playerId);

    @NonNull
    CompletableFuture<String> getSuffix(@NonNull UUID playerId);
}
