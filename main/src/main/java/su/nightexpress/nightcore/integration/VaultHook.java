package su.nightexpress.nightcore.integration;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.ServerUtils;

import java.util.Set;

@Deprecated
public class VaultHook {

    public static boolean hasPermissions() {
        return Engine.hasPermissions();
    }

    @Deprecated
    public static boolean hasEconomy() {
        return ServerUtils.serviceProvider(Economy.class).isPresent();
    }

    @Nullable
    @Deprecated
    public static Economy getEconomy() {
        return ServerUtils.serviceProvider(Economy.class).orElse(null);
    }

    @NonNull
    @Deprecated
    public static String getEconomyName() {
        return getEconomy() != null ? getEconomy().getName() : "null";
    }

    @NonNull
    public static String getPermissionGroup(@NonNull Player player) {
        return Players.getPrimaryGroup(player, "");
    }

    @NonNull
    public static Set<String> getPermissionGroups(@NonNull Player player) {
        return Players.getInheritanceGroups(player);
    }

    @NonNull
    public static String getPrefix(@NonNull Player player) {
        return Players.getPrefixOrEmpty(player);
    }

    @NonNull
    public static String getSuffix(@NonNull Player player) {
        return Players.getSuffixOrEmpty(player);
    }

    @Deprecated
    public static double getBalance(@NonNull Player player) {
        return getBalance((OfflinePlayer) player);
    }

    @Deprecated
    public static double getBalance(@NonNull OfflinePlayer player) {
        return EconomyBridge.getEconomyBalance(player.getUniqueId());
    }

    @Deprecated
    public static boolean addMoney(@NonNull Player player, double amount) {
        return EconomyBridge.depositEconomy(player, amount);
    }

    @Deprecated
    public static boolean addMoney(@NonNull OfflinePlayer player, double amount) {
        return EconomyBridge.depositEconomy(player.getUniqueId(), amount);
    }

    @Deprecated
    public static boolean deposit(@NonNull Player player, double amount) {
        return deposit((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean deposit(@NonNull OfflinePlayer player, double amount) {
        return EconomyBridge.depositEconomy(player.getUniqueId(), amount);
    }

    @Deprecated
    public static boolean takeMoney(@NonNull Player player, double amount) {
        return withdraw((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean takeMoney(@NonNull OfflinePlayer player, double amount) {
        return withdraw(player, Math.abs(amount));
    }

    @Deprecated
    public static boolean withdraw(@NonNull Player player, double amount) {
        return withdraw((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean withdraw(@NonNull OfflinePlayer player, double amount) {
        return EconomyBridge.withdrawEconomy(player.getUniqueId(), amount);
    }
}
