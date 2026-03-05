package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class XPLevelsCurrency extends IncompleteCurrency {

    public XPLevelsCurrency() {
        super(CurrencyId.XP_LEVELS);
    }

    @Override
    public boolean canHandleDecimals() {
        return false;
    }

    @Override
    public boolean canHandleOffline() {
        return false;
    }

    @Override
    @NonNull
    public CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("XP Levels", NightItem.fromType(Material.EXPERIENCE_BOTTLE));
    }


    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return CompletableFuture.completedFuture(this.queryLevel(player));
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(Players.findById(playerId).map(this::queryLevel).orElse(0D));
    }

    @Override
    protected double queryBalanceDirect(@NonNull UUID playerId) {
        return Players.findById(playerId).map(this::queryLevel).orElse(0D);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return CompletableFuture.completedFuture(this.depositLevel(player, amount));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.completedFuture(Players.findById(playerId).map(player -> this.depositLevel(player, amount)).orElse(false));
    }

    @Override
    protected void depositDirect(@NonNull UUID playerId, double amount) {
        Players.findById(playerId).ifPresent(player -> this.depositLevel(player, amount));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        return CompletableFuture.completedFuture(this.withdrawLevel(player, amount));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.completedFuture(Players.findById(playerId).map(player -> this.withdrawLevel(player, amount)).orElse(false));
    }

    @Override
    protected void withdrawDirect(@NonNull UUID playerId, double amount) {
        Players.findById(playerId).ifPresent(player -> this.withdrawLevel(player, amount));
    }

    private double queryLevel(@NonNull Player player) {
        return player.getLevel();
    }

    private boolean depositLevel(@NonNull Player player, double amount) {
        player.setLevel(player.getLevel() + (int) amount);
        return true;
    }

    private boolean withdrawLevel(@NonNull Player player, double amount) {
        player.setLevel(Math.max(0, player.getLevel() - (int) amount));
        return true;
    }
}
