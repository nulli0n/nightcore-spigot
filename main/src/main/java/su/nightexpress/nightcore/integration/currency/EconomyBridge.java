package su.nightexpress.nightcore.integration.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.Registries;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;
import su.nightexpress.nightcore.integration.currency.impl.DummyCurrency;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class EconomyBridge {

    public static void unregisterAll() {
        registry().clear();
    }

    @NotNull
    public static NightRegistry<String, Currency> registry() {
        return Registries.CURRENCY;
    }

    public static boolean hasCurrency(@NotNull String id) {
        return getCurrency(id) != null;
    }

    public static boolean hasEconomy() {
        return hasCurrency(CurrencyId.VAULT);
    }

    public static boolean handle(@NotNull String id, @NotNull Consumer<Currency> consumer) {
        Currency currency = getCurrency(id);
        if (currency == null) return false;

        consumer.accept(currency);
        return true;
    }

    public static boolean hasEnough(@NotNull Player player, @NotNull String id, double amount) {
        return getBalance(player, id) >= amount;
    }

    public static boolean hasEnough(@NotNull UUID playerId, @NotNull String id, double amount) {
        return getBalance(playerId, id) >= amount;
    }

    public static double getBalance(@NotNull Player player, @NotNull String id) {
        Currency currency = getCurrency(id);
        return currency == null ? 0D : currency.getBalance(player);
    }

    public static double getBalance(@NotNull UUID playerId, @NotNull String id) {
        Currency currency = getCurrency(id);
        return currency == null ? 0D : currency.getBalance(playerId);
    }

    public static double getEconomyBalance(@NotNull Player player) {
        return getEconomyBalance(player.getUniqueId());
    }

    public static double getEconomyBalance(@NotNull UUID playerId) {
        return getBalance(playerId, CurrencyId.VAULT);
    }

    public static boolean deposit(@NotNull Player player, @NotNull String id, double amount) {
        return handle(id, currency -> currency.give(player, amount));
    }

    public static boolean deposit(@NotNull UUID playerId, @NotNull String id, double amount) {
        return handle(id, currency -> currency.give(playerId, amount));
    }

    public static boolean depositEconomy(@NotNull Player player, double amount) {
        return depositEconomy(player.getUniqueId(), amount);
    }

    public static boolean depositEconomy(@NotNull UUID playerId, double amount) {
        return deposit(playerId, CurrencyId.VAULT, amount);
    }

    public static boolean withdraw(@NotNull Player player, @NotNull String id, double amount) {
        return handle(id, currency -> currency.take(player, amount));
    }

    public static boolean withdraw(@NotNull UUID playerId, @NotNull String id, double amount) {
        return handle(id, currency -> currency.take(playerId, amount));
    }

    public static boolean withdrawEconomy(@NotNull Player player, double amount) {
        return withdrawEconomy(player.getUniqueId(), amount);
    }

    public static boolean withdrawEconomy(@NotNull UUID playerId, double amount) {
        return withdraw(playerId, CurrencyId.VAULT, amount);
    }



    public static boolean hasCurrency() {
        return !registry().isEmpty();
    }

    public static void register(@NotNull Currency currency) {
        registry().register(currency.getInternalId(), currency);
    }

    @NotNull
    public static Set<Currency> getCurrencies() {
        return registry().values();
    }

    @NotNull
    public static Set<String> getCurrencyIds() {
        return registry().keys();
    }

    @NotNull
    public static Optional<Currency> currency(@NotNull String internalId) {
        return Optional.ofNullable(getCurrency(internalId));
    }

    @Nullable
    public static Currency getCurrency(@NotNull String internalId) {
        return registry().byKey(internalId);
    }

    @NotNull
    public static Currency getCurrencyOrDummy(@NotNull String internalId) {
        return registry().lookup(internalId).orElse(DummyCurrency.INSTANCE);
    }

    @Nullable
    public static Currency getEconomyCurrency() {
        return getCurrency(CurrencyId.VAULT);
    }

    @NotNull
    public static Optional<Currency> economyCurrency() {
        return Optional.ofNullable(getEconomyCurrency());
    }

    @NotNull
    public static DummyCurrency getDummyCurrency() {
        return DummyCurrency.INSTANCE;
    }
}
