package su.nightexpress.nightcore.integration.permission;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PermissionProvider {

    //void setup();

    @NotNull String getName();

    @Nullable String getPrimaryGroup(@NotNull Player player);

    @NotNull Set<String> getPermissionGroups(@NotNull Player player);

    @Nullable String getPrefix(@NotNull Player player);

    @Nullable String getSuffix(@NotNull Player player);

    @NotNull CompletableFuture<String> getPrimaryGroup(@NotNull UUID playerId);

    @NotNull CompletableFuture<Set<String>> getPermissionGroups(@NotNull UUID playerId);

    @NotNull CompletableFuture<String> getPrefix(@NotNull UUID playerId);

    @NotNull CompletableFuture<String> getSuffix(@NotNull UUID playerId);
}
