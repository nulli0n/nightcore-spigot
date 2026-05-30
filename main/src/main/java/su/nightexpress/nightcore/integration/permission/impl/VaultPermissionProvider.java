package su.nightexpress.nightcore.integration.permission.impl;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.integration.permission.PermissionPlugins;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.ServerUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultPermissionProvider implements PermissionProvider {

    @Override
    @NonNull
    public String getName() {
        return PermissionPlugins.VAULT;
    }

    @NonNull
    private Optional<Permission> permission() {
        return ServerUtils.serviceProvider(Permission.class);
    }

    @NonNull
    private Optional<Chat> chat() {
        return ServerUtils.serviceProvider(Chat.class);
    }

    @NonNull
    private Optional<Player> onlinePlayer(@NonNull UUID playerId) {
        return Optional.ofNullable(Players.getPlayer(playerId));
    }

    @Override
    @Nullable
    public String getPrimaryGroup(@NonNull Player player) {
        Permission permission = this.permission().orElse(null);
        if (permission == null || !permission.hasGroupSupport()) return null;

        String group = permission.getPrimaryGroup(player);
        return group == null ? null : LowerCase.USER_LOCALE.apply(group);
    }

    @Override
    @NonNull
    public CompletableFuture<String> getPrimaryGroup(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getPrimaryGroup).orElse(null));
    }

    @Override
    @NonNull
    public Set<String> getPermissionGroups(@NonNull Player player) {
        Permission permission = this.permission().orElse(null);
        if (permission == null || !permission.hasGroupSupport()) return Collections.emptySet();

        String[] groups = permission.getPlayerGroups(player);
        if (groups == null) groups = new String[]{this.getPrimaryGroup(player)};

        return Stream.of(groups).map(LowerCase.USER_LOCALE::apply).collect(Collectors.toSet());
    }

    @Override
    @NonNull
    public CompletableFuture<Set<String>> getPermissionGroups(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getPermissionGroups).orElse(
            Collections.emptySet()));
    }

    @Override
    @Nullable
    public String getPrefix(@NonNull Player player) {
        return this.chat().map(chat -> chat.getPlayerPrefix(player)).orElse(null);
    }

    @Override
    @NonNull
    public CompletableFuture<String> getPrefix(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getPrefix).orElse(null));
    }

    @Override
    @Nullable
    public String getSuffix(@NonNull Player player) {
        return this.chat().map(chat -> chat.getPlayerSuffix(player)).orElse(null);
    }

    @Override
    @NonNull
    public CompletableFuture<String> getSuffix(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getSuffix).orElse(null));
    }
}
