package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;

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
    @NotNull
    public CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("XP Levels", NightItem.fromType(Material.EXPERIENCE_BOTTLE));
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return player.getLevel();
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return 0;
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        player.setLevel(player.getLevel() + (int) amount);
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {

    }

    @Override
    public void take(@NotNull Player player, double amount) {
        player.setLevel(Math.max(0, player.getLevel() - (int) amount));
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {

    }
}
