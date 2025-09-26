package su.nightexpress.nightcore.integration.currency.impl;

import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.UUID;

public class BeastTokensCurrency extends IncompleteCurrency {

    public BeastTokensCurrency() {
        super(CurrencyId.BEAST_TOKENS);
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
    public @NotNull CurrencySettings getDefaultSettings() {
        return CurrencySettings.createDefault("Beast Tokens", NightItem.fromType(Material.SUNFLOWER));
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return BeastTokensAPI.getTokensManager().getTokens(player);
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return BeastTokensAPI.getTokensManager().getTokens(Bukkit.getOfflinePlayer(playerId));
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        BeastTokensAPI.getTokensManager().addTokens(player, amount);
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        BeastTokensAPI.getTokensManager().addTokens(Bukkit.getOfflinePlayer(playerId), amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        BeastTokensAPI.getTokensManager().removeTokens(player, amount);
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        BeastTokensAPI.getTokensManager().removeTokens(Bukkit.getOfflinePlayer(playerId), amount);
    }
}
