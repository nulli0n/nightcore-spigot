package su.nightexpress.nightcore.integration.currency.impl;

import com.magmaguy.elitemobs.economy.EconomyHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EliteMobsCurrency extends IncompleteCurrency {

    public EliteMobsCurrency() {
        super(CurrencyId.ELITE_MOBS);
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
        return CurrencySettings.createDefault("Elite Tokens", NightItem.fromType(Material.EMERALD));
    }


    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return this.queryBalanceAsync(player.getUniqueId());
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> EconomyHandler.checkCurrency(playerId, true));
    }

    @Override
    protected double queryBalanceDirect(@NonNull UUID playerId) {
        return EconomyHandler.checkCurrency(playerId, false);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return this.depositAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        this.depositDirect(playerId, amount);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    protected void depositDirect(@NonNull UUID playerId, double amount) {
        EconomyHandler.addCurrency(playerId, amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        return this.withdrawAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        this.withdrawDirect(playerId, amount);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    protected void withdrawDirect(@NonNull UUID playerId, double amount) {
        EconomyHandler.subtractCurrency(playerId, amount);
    }
}
