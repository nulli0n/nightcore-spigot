package su.nightexpress.nightcore.integration.currency.impl;

import com.magmaguy.elitemobs.economy.EconomyHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;

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
    @NotNull
    public CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("Elite Tokens", NightItem.fromType(Material.EMERALD));
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return this.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return EconomyHandler.checkCurrency(playerId, true);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        this.give(player.getUniqueId(), amount);
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        EconomyHandler.addCurrency(playerId, amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        this.take(player.getUniqueId(), amount);
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        EconomyHandler.subtractCurrency(playerId, amount);
    }
}
