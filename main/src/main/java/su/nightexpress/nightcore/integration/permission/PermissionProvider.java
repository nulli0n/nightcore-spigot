package su.nightexpress.nightcore.integration.permission;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface PermissionProvider {

    //void setup();

    @NotNull String getName();

    @Nullable String getPrimaryGroup(@NotNull Player player);

    @NotNull Set<String> getPermissionGroups(@NotNull Player player);

    @Nullable String getPrefix(@NotNull Player player);

    @Nullable String getSuffix(@NotNull Player player);
}
