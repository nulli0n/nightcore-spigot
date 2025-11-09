package su.nightexpress.nightcore.integration.currency.impl;

import com.bencodez.votingplugin.VotingPluginHooks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;

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
    @NotNull
    public CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("Voting Points", NightItem.fromType(Material.SUNFLOWER));
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return VotingPluginHooks.getInstance().getUserManager().getVotingPluginUser(player).getPoints();
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return VotingPluginHooks.getInstance().getUserManager().getVotingPluginUser(playerId).getPoints();
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        NightCore.get().runTaskAsync(() -> VotingPluginHooks.getInstance().getUserManager().getVotingPluginUser(player).addPoints((int) amount));
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        NightCore.get().runTaskAsync(() -> VotingPluginHooks.getInstance().getUserManager().getVotingPluginUser(playerId).addPoints((int) amount));
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        VotingPluginHooks.getInstance().getUserManager().getVotingPluginUser(player).removePoints((int) amount);
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        VotingPluginHooks.getInstance().getUserManager().getVotingPluginUser(playerId).removePoints((int) amount);
    }
}
