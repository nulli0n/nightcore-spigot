package su.nightexpress.nightcore.integration.permission.impl;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.permission.PermissionPlugins;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LuckPermissionProvider implements PermissionProvider {

    @Override
    @NotNull
    public String getName() {
        return PermissionPlugins.LUCK_PERMS;
    }

    private LuckPerms api() {
        return LuckPermsProvider.get();
    }

    @Nullable
    private User getUser(@NotNull Player player) {
        return this.api().getUserManager().getUser(player.getUniqueId());
    }

    @NotNull
    private CompletableFuture<User> loadUser(@NotNull UUID playerId) {
        return api().getUserManager().loadUser(playerId);
    }

    @Override
    @Nullable
    public String getPrimaryGroup(@NotNull Player player) {
        return this.getPrimaryGroup(this.getUser(player));
    }

    @Override
    @NotNull
    public CompletableFuture<String> getPrimaryGroup(@NotNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getPrimaryGroup);
    }

    @Nullable
    private String getPrimaryGroup(@Nullable User user) {
        if (user == null) return null;

        String group = user.getPrimaryGroup();
        return LowerCase.USER_LOCALE.apply(group);
    }

    @Override
    @NotNull
    public Set<String> getPermissionGroups(@NotNull Player player) {
        return this.getPermissionGroups(this.getUser(player));
    }

    @Override
    @NotNull
    public CompletableFuture<Set<String>> getPermissionGroups(@NotNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getPermissionGroups);
    }

    @NotNull
    private Set<String> getPermissionGroups(@Nullable User user) {
        if (user == null) return Collections.emptySet();

        return user.getNodes(NodeType.INHERITANCE).stream().map(InheritanceNode::getGroupName).map(LowerCase.USER_LOCALE::apply).collect(Collectors.toSet());
    }

    @Override
    @Nullable
    public String getPrefix(@NotNull Player player) {
        return this.getPrefix(this.getUser(player));
    }

    @Override
    @NotNull
    public CompletableFuture<String> getPrefix(@NotNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getPrefix);
    }

    @Nullable
    public String getPrefix(@Nullable User user) {
        return user == null ? null : user.getCachedData().getMetaData().getPrefix();
    }

    @Override
    @Nullable
    public String getSuffix(@NotNull Player player) {
        return this.getSuffix(this.getUser(player));
    }

    @Override
    @NotNull
    public CompletableFuture<String> getSuffix(@NotNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getSuffix);
    }

    @Nullable
    public String getSuffix(@Nullable User user) {
        return user == null ? null : user.getCachedData().getMetaData().getSuffix();
    }
}
