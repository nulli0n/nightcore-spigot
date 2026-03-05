package su.nightexpress.nightcore.integration.currency.impl;

import com.bencodez.votingplugin.VotingPluginHooks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VotingPluginCurrency extends IncompleteCurrency {

    public VotingPluginCurrency() {
        super(CurrencyId.VOTING_PLUGIN);
    }

    @Override
    public boolean canHandleDecimals() {
        return false;
    }

    @Override
    public boolean canHandleOffline() {
        return true;
    }

    @Override
    @NonNull
    public CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("Voting Points", NightItem.fromType(Material.SUNFLOWER));
    }

    @NonNull
    private VotingPluginHooks getAPI() {
        return VotingPluginHooks.getInstance();
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return this.queryBalanceAsync(player.getUniqueId());
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> (double) getAPI().getUserManager().getVotingPluginUser(playerId).getPoints());
    }

    @Override
    protected double queryBalanceDirect(@NonNull UUID playerId) {
        return getAPI().getUserManager().getVotingPluginUser(playerId).getPoints();
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return this.depositAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        this.getAPI().getUserManager().getVotingPluginUser(playerId).addPoints((int) amount, true);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    protected void depositDirect(@NonNull UUID playerId, double amount) {
        this.getAPI().getUserManager().getVotingPluginUser(playerId).addPoints((int) amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        return this.withdrawAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        this.getAPI().getUserManager().getVotingPluginUser(playerId).removePoints((int) amount, true);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    protected void withdrawDirect(@NonNull UUID playerId, double amount) {
        this.getAPI().getUserManager().getVotingPluginUser(playerId).removePoints((int) amount);
    }
}
