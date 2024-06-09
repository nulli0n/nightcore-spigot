package su.nightexpress.nightcore.integration;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Plugins;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultHook {

    private static Economy    economy;
    private static Permission permission;
    private static Chat       chat;

    public static void setup() {
        setPermission();
        setEconomy();
        setChat();
    }

    public static void shutdown() {
        economy = null;
        permission = null;
        chat = null;
    }

    @Nullable
    private static <T> T getProvider(@NotNull Class<T> clazz) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(clazz);
        return provider == null ? null : provider.getProvider();
    }

    private static void setPermission() {
        permission = getProvider(Permission.class);
        if (permission != null) {
            Plugins.CORE.info("Found permissions provider: " + permission.getName());
        }
    }

    private static void setEconomy() {
        economy = getProvider(Economy.class);
        if (economy != null) {
            Plugins.CORE.info("Found economy provider: " + economy.getName());
        }
    }

    private static void setChat() {
        chat = getProvider(Chat.class);
        if (chat != null) {
            Plugins.CORE.info("Found chat provider: " + chat.getName());
        }
    }

    public static void onServiceRegisterEvent(@NotNull ServiceRegisterEvent event) {
        Object provider = event.getProvider().getProvider();

        if (economy == null && provider instanceof Economy) {
            setEconomy();
        }
        else if (permission == null && provider instanceof Permission) {
            setPermission();
        }
        else if (chat == null && provider instanceof Chat) {
            setChat();
        }
    }

    public static boolean hasPermissions() {
        return getPermissions() != null;
    }

    @Nullable
    public static Permission getPermissions() {
        return permission;
    }

    public static boolean hasChat() {
        return getChat() != null;
    }

    @Nullable
    public static Chat getChat() {
        return chat;
    }

    public static boolean hasEconomy() {
        return getEconomy() != null;
    }

    @Nullable
    public static Economy getEconomy() {
        return economy;
    }

    @NotNull
    public static String getEconomyName() {
        return hasEconomy() ? economy.getName() : "null";
    }

    @NotNull
    public static String getPermissionGroup(@NotNull Player player) {
        if (!hasPermissions() || !permission.hasGroupSupport()) return "";

        String group = permission.getPrimaryGroup(player);
        return group == null ? "" : group.toLowerCase();
    }

    @NotNull
    public static Set<String> getPermissionGroups(@NotNull Player player) {
        if (!hasPermissions() || !permission.hasGroupSupport()) return Collections.emptySet();

        String[] groups = permission.getPlayerGroups(player);
        if (groups == null) groups = new String[] {getPermissionGroup(player)};

        return Stream.of(groups).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @NotNull
    public static String getPrefix(@NotNull Player player) {
        return hasChat() ? chat.getPlayerPrefix(player) : "";
    }

    @NotNull
    public static String getSuffix(@NotNull Player player) {
        return hasChat() ? chat.getPlayerSuffix(player) : "";
    }

    public static double getBalance(@NotNull Player player) {
        return economy.getBalance(player);
    }

    public static double getBalance(@NotNull OfflinePlayer player) {
        return economy.getBalance(player);
    }

    public static boolean addMoney(@NotNull Player player, double amount) {
        return addMoney((OfflinePlayer) player, amount);
    }

    public static boolean addMoney(@NotNull OfflinePlayer player, double amount) {
        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    public static boolean takeMoney(@NotNull Player player, double amount) {
        return takeMoney((OfflinePlayer) player, amount);
    }

    public static boolean takeMoney(@NotNull OfflinePlayer player, double amount) {
        return economy.withdrawPlayer(player, Math.abs(amount)).transactionSuccess();
    }
}
