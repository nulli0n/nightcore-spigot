package su.nightexpress.nightcore.integration.currency.impl;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.ServerUtils;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VaultEconomyCurrency extends IncompleteCurrency {

    public VaultEconomyCurrency() {
        super(CurrencyId.VAULT);
    }

    @NonNull
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
    @NonNull
    public CurrencySettings getDefaultSettings() {
        return new CurrencySettings("Money", "$" + Placeholders.GENERIC_AMOUNT, NightItem.fromType(Material.GOLD_NUGGET));
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return CompletableFuture.supplyAsync(() -> this.economy().map(economy -> economy.getBalance(player)).orElse(0D));
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> this.economy()
            .map(economy -> economy.getBalance(Bukkit.getOfflinePlayer(playerId)))
            .orElse(0D));
    }

    @Override
    protected double queryBalanceDirect(@NonNull Player player) {
        return this.economy().map(economy -> economy.getBalance(player)).orElse(0D);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return CompletableFuture.supplyAsync(() -> this.economy()
            .map(economy -> economy.depositPlayer(player, amount).transactionSuccess())
            .orElse(false));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.supplyAsync(() -> this.economy()
            .map(economy -> economy.depositPlayer(Bukkit.getOfflinePlayer(playerId), amount).transactionSuccess())
            .orElse(false));
    }

    @Override
    protected void depositDirect(@NonNull Player player, double amount) {
        this.economy().ifPresent(economy -> economy.depositPlayer(player, amount));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        return CompletableFuture.supplyAsync(() -> this.economy()
            .map(economy -> economy.withdrawPlayer(player, amount).transactionSuccess())
            .orElse(false));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.supplyAsync(() -> this.economy()
            .map(economy -> economy.withdrawPlayer(Bukkit.getOfflinePlayer(playerId), amount).transactionSuccess())
            .orElse(false));
    }

    @Override
    protected void withdrawDirect(@NonNull Player player, double amount) {
        this.economy().ifPresent(economy -> economy.withdrawPlayer(player, amount));
    }
}
