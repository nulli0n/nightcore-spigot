package su.nightexpress.nightcore.integration.permission.impl;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.Plugins;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultPermissionProvider implements PermissionProvider {

    private Permission permission;
    private Chat       chat;

    @Override
    public void setup() {
        this.permission = getProvider(Permission.class);
        this.chat = getProvider(Chat.class);
    }

    @Override
    @NotNull
    public String getName() {
        return Plugins.VAULT;
    }

    @Nullable
    private static <T> T getProvider(@NotNull Class<T> clazz) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(clazz);
        return provider == null ? null : provider.getProvider();
    }

    @Override
    @Nullable
    public String getPrimaryGroup(@NotNull Player player) {
        if (this.permission == null || !this.permission.hasGroupSupport()) return null;

        String group = this.permission.getPrimaryGroup(player);
        return group == null ? null : LowerCase.USER_LOCALE.apply(group);
    }

    @Override
    @NotNull
    public Set<String> getPermissionGroups(@NotNull Player player) {
        if (this.permission == null || !this.permission.hasGroupSupport()) return Collections.emptySet();

        String[] groups = this.permission.getPlayerGroups(player);
        if (groups == null) groups = new String[] {this.getPrimaryGroup(player)};

        return Stream.of(groups).map(LowerCase.USER_LOCALE::apply).collect(Collectors.toSet());
    }

    @Override
    @Nullable
    public String getPrefix(@NotNull Player player) {
        return this.chat != null ? this.chat.getPlayerPrefix(player) : null;
    }

    @Override
    @Nullable
    public String getSuffix(@NotNull Player player) {
        return this.chat != null ? this.chat.getPlayerSuffix(player) : null;
    }
}
