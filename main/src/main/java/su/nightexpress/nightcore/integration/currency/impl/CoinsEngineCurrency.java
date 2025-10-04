package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.type.AbstractCurrency;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class CoinsEngineCurrency extends AbstractCurrency {

    public CoinsEngineCurrency(@NotNull String id) {
        super(id, CurrencyId.forCoinsEngine(id));
    }

    @NotNull
    public static Set<CoinsEngineCurrency> getCurrencies() {
        Set<CoinsEngineCurrency> currencies = new HashSet<>();
        CoinsEngineAPI.getCurrencyManager().getCurrencies().forEach(currency -> {
            if (!currency.isVaultEconomy()) {
                currencies.add(new CoinsEngineCurrency(currency.getId()));
            }
        });
        return currencies;
    }

    @NotNull
    private Optional<Currency> currency() {
        return Optional.ofNullable(CoinsEngineAPI.getCurrency(this.originalId));
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return this.currency().map(Currency::replacePlaceholders).orElse(str -> str);
    }

    @Override
    public boolean canHandleDecimals() {
        return this.currency().map(Currency::isDecimal).orElse(true);
    }

    @Override
    public boolean canHandleOffline() {
        return true;
    }

    @Override
    public double floorIfNeeded(double amount) {
        return this.currency().map(currency -> currency.floorIfNeeded(amount)).orElse(amount);
    }

    @Override
    @NotNull
    public String formatValue(double amount) {
        return this.currency().map(currency -> currency.formatValue(amount)).orElse(String.valueOf(amount));
    }

    @Override
    @NotNull
    public String getName() {
        return this.currency().map(Currency::getName).orElse(this.getOriginalId());
    }

    @Override
    @NotNull
    public String getFormat() {
        return this.currency().map(Currency::getFormat).orElse(Placeholders.GENERIC_AMOUNT);
    }

    @Override
    @NotNull
    public ItemStack getIcon() {
        return this.currency().map(Currency::icon).orElse(NightItem.fromType(Material.GOLD_NUGGET)).getItemStack();
    }



    @Override
    public double getBalance(@NotNull Player player) {
        return this.currency().map(currency -> CoinsEngineAPI.getBalance(player, currency)).orElse(0D);
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return CoinsEngineAPI.getBalance(playerId, this.originalId);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        this.currency().ifPresent(currency -> CoinsEngineAPI.addBalance(player, currency, amount));
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        CoinsEngineAPI.addBalance(playerId, this.originalId, amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        this.currency().ifPresent(currency -> CoinsEngineAPI.removeBalance(player, currency, amount));
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        CoinsEngineAPI.removeBalance(playerId, this.originalId, amount);
    }
}
