package su.nightexpress.nightcore.integration;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @NotNull
    @Deprecated
    public static String getEconomyName() {
        return getEconomy() != null ? getEconomy().getName() : "null";
    }

    @NotNull
    public static String getPermissionGroup(@NotNull Player player) {
        return Players.getPrimaryGroup(player, "");
    }

    @NotNull
    public static Set<String> getPermissionGroups(@NotNull Player player) {
        return Players.getInheritanceGroups(player);
    }

    @NotNull
    public static String getPrefix(@NotNull Player player) {
        return Players.getPrefixOrEmpty(player);
    }

    @NotNull
    public static String getSuffix(@NotNull Player player) {
        return Players.getSuffixOrEmpty(player);
    }

    @Deprecated
    public static double getBalance(@NotNull Player player) {
        return getBalance((OfflinePlayer) player);
    }

    @Deprecated
    public static double getBalance(@NotNull OfflinePlayer player) {
        return EconomyBridge.getEconomyBalance(player.getUniqueId());
    }

    @Deprecated
    public static boolean addMoney(@NotNull Player player, double amount) {
        return EconomyBridge.depositEconomy(player, amount);
    }

    @Deprecated
    public static boolean addMoney(@NotNull OfflinePlayer player, double amount) {
        return EconomyBridge.depositEconomy(player.getUniqueId(), amount);
    }

    @Deprecated
    public static boolean deposit(@NotNull Player player, double amount) {
        return deposit((OfflinePlayer) player, amount);
    }

    @Deprecated
    public static boolean deposit(@NotNull OfflinePlayer player, double amount) {
        return EconomyBridge.depositEconomy(player.getUniqueId(), amount);
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
        return EconomyBridge.withdrawEconomy(player.getUniqueId(), amount);
    }
}
