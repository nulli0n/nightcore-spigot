package su.nightexpress.nightcore.integration.permission.impl;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    @NotNull
    public String getName() {
        return PermissionPlugins.VAULT;
    }

    @NotNull
    private Optional<Permission> permission() {
        return ServerUtils.serviceProvider(Permission.class);
    }

    @NotNull
    private Optional<Chat> chat() {
        return ServerUtils.serviceProvider(Chat.class);
    }

    @NotNull
    private Optional<Player> onlinePlayer(@NotNull UUID playerId) {
        return Optional.ofNullable(Players.getPlayer(playerId));
    }

    @Override
    @Nullable
    public String getPrimaryGroup(@NotNull Player player) {
        Permission permission = this.permission().orElse(null);
        if (permission == null || !permission.hasGroupSupport()) return null;

        String group = permission.getPrimaryGroup(player);
        return group == null ? null : LowerCase.USER_LOCALE.apply(group);
    }

    @Override
    @NotNull
    public CompletableFuture<String> getPrimaryGroup(@NotNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getPrimaryGroup).orElse(null));
    }

    @Override
    @NotNull
    public Set<String> getPermissionGroups(@NotNull Player player) {
        Permission permission = this.permission().orElse(null);
        if (permission == null || !permission.hasGroupSupport()) return Collections.emptySet();

        String[] groups = permission.getPlayerGroups(player);
        if (groups == null) groups = new String[] {this.getPrimaryGroup(player)};

        return Stream.of(groups).map(LowerCase.USER_LOCALE::apply).collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public CompletableFuture<Set<String>> getPermissionGroups(@NotNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getPermissionGroups).orElse(Collections.emptySet()));
    }

    @Override
    @Nullable
    public String getPrefix(@NotNull Player player) {
        return this.chat().map(chat -> chat.getPlayerPrefix(player)).orElse(null);
    }

    @Override
    @NotNull
    public CompletableFuture<String> getPrefix(@NotNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getPrefix).orElse(null));
    }

    @Override
    @Nullable
    public String getSuffix(@NotNull Player player) {
        return this.chat().map(chat -> chat.getPlayerSuffix(player)).orElse(null);
    }

    @Override
    @NotNull
    public CompletableFuture<String> getSuffix(@NotNull UUID playerId) {
        return CompletableFuture.completedFuture(this.onlinePlayer(playerId).map(this::getSuffix).orElse(null));
    }
}
