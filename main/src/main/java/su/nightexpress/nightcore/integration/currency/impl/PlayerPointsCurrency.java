package su.nightexpress.nightcore.integration.currency.impl;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;

public class PlayerPointsCurrency extends IncompleteCurrency {

    private final PlayerPointsAPI api;

    public PlayerPointsCurrency() {
        super(CurrencyId.PLAYER_POINTS);
        this.api = PlayerPoints.getInstance().getAPI();
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
    public @NotNull CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("Points", NightItem.fromType(Material.SUNFLOWER));
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return this.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return this.api.look(playerId);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        this.give(player.getUniqueId(), amount);
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        this.api.give(playerId, (int) amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        this.take(player.getUniqueId(), amount);
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        this.api.take(playerId, (int) amount);
    }
}
