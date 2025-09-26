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

    @Override
    @Nullable
    public String getPrimaryGroup(@NotNull Player player) {
        User user = getUser(player);
        if (user == null) return null;

        String group = user.getPrimaryGroup();
        return LowerCase.USER_LOCALE.apply(group);
    }

    @Override
    @NotNull
    public Set<String> getPermissionGroups(@NotNull Player player) {
        User user = getUser(player);
        if (user == null) return Collections.emptySet();
        return user.getNodes(NodeType.INHERITANCE)
            .stream()
            .map(InheritanceNode::getGroupName)
            .map(LowerCase.USER_LOCALE::apply)
            .collect(Collectors.toSet());
    }

    @Override
    @Nullable
    public String getPrefix(@NotNull Player player) {
        User user = getUser(player);
        if (user == null) return null;
        return user.getCachedData().getMetaData().getPrefix();
    }

    @Override
    @Nullable
    public String getSuffix(@NotNull Player player) {
        User user = getUser(player);
        if (user == null) return null;
        return user.getCachedData().getMetaData().getSuffix();
    }
}
