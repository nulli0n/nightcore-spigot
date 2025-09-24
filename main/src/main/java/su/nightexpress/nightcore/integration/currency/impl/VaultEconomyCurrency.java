package su.nightexpress.nightcore.integration.currency.impl;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.ServerUtils;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Optional;
import java.util.UUID;

public class VaultEconomyCurrency extends IncompleteCurrency {

    public VaultEconomyCurrency() {
        super(CurrencyId.VAULT);
    }

    @NotNull
    private Optional<Economy> economy() {
        return ServerUtils.serviceProvider(Economy.class);
    }

    @Override
    public boolean canHandleDecimals() {
        return true;
    }

    @Override
    public boolean canHandleOffline() {
        return true;
    }

    @Override
    @NotNull
    public CurrencySettings getDefaultSettings() {
        return new CurrencySettings("Money", "$" + Placeholders.GENERIC_AMOUNT, NightItem.fromType(Material.GOLD_NUGGET));
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return this.economy().map(economy -> economy.getBalance(player)).orElse(0D);
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);

        return this.economy().map(economy -> economy.getBalance(offlinePlayer)).orElse(0D);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        this.economy().ifPresent(economy -> economy.depositPlayer(player, amount));
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);

        this.economy().ifPresent(economy -> economy.depositPlayer(offlinePlayer, amount));
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        this.economy().ifPresent(economy -> economy.withdrawPlayer(player, amount));
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);

        this.economy().ifPresent(economy -> economy.withdrawPlayer(offlinePlayer, amount));
    }
}
