package su.nightexpress.nightcore.integration;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.util.Players;

import java.util.Set;

@Deprecated
public class VaultHook {

    //private static NightCore  core;
    private static Economy    economy;
    //private static Permission permission;
    //private static Chat       chat;

    public static void load() {
        //VaultHook.core = core;

        //setPermission();
        setEconomy();
        //setChat();
    }

    public static void shutdown() {
        economy = null;
        //permission = null;
        //chat = null;
        //core = null;
    }

    @Nullable
    private static <T> T getProvider(@NotNull Class<T> clazz) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(clazz);
        return provider == null ? null : provider.getProvider();
    }

//    private static void setPermission() {
//        permission = getProvider(Permission.class);
//        if (permission != null) {
//            core.info("Found permissions provider: " + permission.getName());
//        }
//    }

    @Deprecated
    private static void setEconomy() {
        economy = getProvider(Economy.class);
        if (economy != null) {
            Engine.core().info("Found economy provider: " + economy.getName());
        }
    }

//    private static void setChat() {
//        chat = getProvider(Chat.class);
//        if (chat != null) {
//            core.info("Found chat provider: " + chat.getName());
//        }
//    }

    public static void onServiceRegisterEvent(@NotNull ServiceRegisterEvent event) {
        Object provider = event.getProvider().getProvider();

        if (economy == null && provider instanceof Economy) {
            setEconomy();
        }
//        else if (permission == null && provider instanceof Permission) {
//            setPermission();
//        }
//        else if (chat == null && provider instanceof Chat) {
//            setChat();
//        }
    }

    public static boolean hasPermissions() {
        return Engine.hasPermissions();
    }

//    @Nullable
//    public static Permission getPermissions() {
//        return permission;
//    }
//
//    public static boolean hasChat() {
//        return chat != null;
//    }
//
//    @Nullable
//    public static Chat getChat() {
//        return chat;
//    }

    @Deprecated
    public static boolean hasEconomy() {
        return economy != null;
    }

    @Nullable
    @Deprecated
    public static Economy getEconomy() {
        return economy;
    }

    @NotNull
    @Deprecated
    public static String getEconomyName() {
        return hasEconomy() ? economy.getName() : "null";
    }

    @NotNull
    public static String getPermissionGroup(@NotNull Player player) {
        return Players.getPrimaryGroup(player, "");
//        if (!hasPermissions() || !permission.hasGroupSupport()) return "";
//
//        String group = permission.getPrimaryGroup(player);
//        return group == null ? "" : group.toLowerCase();
    }

    @NotNull
    public static Set<String> getPermissionGroups(@NotNull Player player) {
        return Players.getInheritanceGroups(player);
//        if (!hasPermissions() || !permission.hasGroupSupport()) return Collections.emptySet();
//
//        String[] groups = permission.getPlayerGroups(player);
//        if (groups == null) groups = new String[] {getPermissionGroup(player)};
//
//        return Stream.of(groups).map(String::toLowerCase).collect(Collectors.toSet());
    }

    @NotNull
    public static String getPrefix(@NotNull Player player) {
        return Players.getPrefixOrEmpty(player);// hasChat() ? chat.getPlayerPrefix(player) : "";
    }

    @NotNull
    public static String getSuffix(@NotNull Player player) {
        return Players.getSuffixOrEmpty(player);// hasChat() ? chat.getPlayerSuffix(player) : "";
    }

    @Deprecated
    public static double getBalance(@NotNull Player player) {
        return getBalance((OfflinePlayer) player);
    }

    @Deprecated
    public static double getBalance(@NotNull OfflinePlayer player) {
        return economy.getBalance(player);
    }

    @Deprecated
    public static boolean addMoney(@NotNull Player player, double amount) {
        return deposit((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean addMoney(@NotNull OfflinePlayer player, double amount) {
        return deposit(player, amount);
    }

    @Deprecated
    public static boolean deposit(@NotNull Player player, double amount) {
        return deposit((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean deposit(@NotNull OfflinePlayer player, double amount) {
        return economy.depositPlayer(player, Math.abs(amount)).transactionSuccess();
    }

    @Deprecated
    public static boolean takeMoney(@NotNull Player player, double amount) {
        return withdraw((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean takeMoney(@NotNull OfflinePlayer player, double amount) {
        return withdraw(player, Math.abs(amount));
    }

    @Deprecated
    public static boolean withdraw(@NotNull Player player, double amount) {
        return withdraw((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean withdraw(@NotNull OfflinePlayer player, double amount) {
        return economy.withdrawPlayer(player, Math.abs(amount)).transactionSuccess();
    }
}
