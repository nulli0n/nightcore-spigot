package su.nightexpress.nightcore.integration.permission.impl;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
    @NonNull
    public String getName() {
        return PermissionPlugins.LUCK_PERMS;
    }

    private LuckPerms api() {
        return LuckPermsProvider.get();
    }

    @Nullable
    private User getUser(@NonNull Player player) {
        return this.api().getUserManager().getUser(player.getUniqueId());
    }

    @NonNull
    private CompletableFuture<User> loadUser(@NonNull UUID playerId) {
        return api().getUserManager().loadUser(playerId);
    }

    @Override
    @Nullable
    public String getPrimaryGroup(@NonNull Player player) {
        return this.getPrimaryGroup(this.getUser(player));
    }

    @Override
    @NonNull
    public CompletableFuture<String> getPrimaryGroup(@NonNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getPrimaryGroup);
    }

    @Nullable
    private String getPrimaryGroup(@Nullable User user) {
        if (user == null) return null;

        String group = user.getPrimaryGroup();
        return LowerCase.USER_LOCALE.apply(group);
    }

    @Override
    @NonNull
    public Set<String> getPermissionGroups(@NonNull Player player) {
        return this.getPermissionGroups(this.getUser(player));
    }

    @Override
    @NonNull
    public CompletableFuture<Set<String>> getPermissionGroups(@NonNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getPermissionGroups);
    }

    @NonNull
    private Set<String> getPermissionGroups(@Nullable User user) {
        if (user == null) return Collections.emptySet();

        return user.getNodes(NodeType.INHERITANCE).stream().map(InheritanceNode::getGroupName).map(
            LowerCase.USER_LOCALE::apply).collect(Collectors.toSet());
    }

    @Override
    @Nullable
    public String getPrefix(@NonNull Player player) {
        return this.getPrefix(this.getUser(player));
    }

    @Override
    @NonNull
    public CompletableFuture<String> getPrefix(@NonNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getPrefix);
    }

    @Nullable
    public String getPrefix(@Nullable User user) {
        return user == null ? null : user.getCachedData().getMetaData().getPrefix();
    }

    @Override
    @Nullable
    public String getSuffix(@NonNull Player player) {
        return this.getSuffix(this.getUser(player));
    }

    @Override
    @NonNull
    public CompletableFuture<String> getSuffix(@NonNull UUID playerId) {
        return this.loadUser(playerId).thenApplyAsync(this::getSuffix);
    }

    @Nullable
    public String getSuffix(@Nullable User user) {
        return user == null ? null : user.getCachedData().getMetaData().getSuffix();
    }
}
