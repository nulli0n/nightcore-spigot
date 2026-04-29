package su.nightexpress.nightcore.integration.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.currency.EconomyBridgeAPI;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.integration.currency.impl.DummyCurrency;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class EconomyBridge {

    private static EconomyBridgeAPI api;

    static void register(@NonNull EconomyBridgeAPI api) {
        EconomyBridge.api = api;
    }

    public static boolean initialized() {
        return api != null;
    }

    @NonNull
    public static EconomyBridgeAPI api() {
        if (api == null) throw new IllegalStateException("API is not initialized!");

        return api;
    }

    @Deprecated(forRemoval = true)
    public static void unregisterAll() {
        registry().clear();
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static NightRegistry<String, Currency> registry() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Deprecated(forRemoval = true)
    public static boolean hasCurrency(@NonNull String id) {
        return getCurrency(id) != null;
    }

    @Deprecated(forRemoval = true)
    public static boolean hasEconomy() {
        return hasCurrency(CurrencyId.VAULT);
    }

    @Deprecated(forRemoval = true)
    public static boolean handle(@NonNull String id, @NonNull Consumer<Currency> consumer) {
        Currency currency = getCurrency(id);
        if (currency == null) return false;

        consumer.accept(currency);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean hasEnough(@NonNull Player player, @NonNull String id, double amount) {
        return getBalance(player, id) >= amount;
    }

    @Deprecated(forRemoval = true)
    public static boolean hasEnough(@NonNull UUID playerId, @NonNull String id, double amount) {
        return getBalance(playerId, id) >= amount;
    }

    
    

    @Deprecated(forRemoval = true)
    public static double getBalance(@NonNull Player player, @NonNull String id) {
        Currency currency = getCurrency(id);
        return currency == null ? 0D : currency.queryBalance(player);
    }

    @Deprecated(forRemoval = true)
    public static double getBalance(@NonNull UUID playerId, @NonNull String id) {
        Currency currency = getCurrency(id);
        return currency == null ? 0D : currency.queryBalance(playerId);
    }

    @Deprecated(forRemoval = true)
    public static double getEconomyBalance(@NonNull Player player) {
        return getEconomyBalance(player.getUniqueId());
    }

    @Deprecated(forRemoval = true)
    public static double getEconomyBalance(@NonNull UUID playerId) {
        return getBalance(playerId, CurrencyId.VAULT);
    }

    @Deprecated(forRemoval = true)
    public static boolean deposit(@NonNull Player player, double amount) {
        deposit(player.getUniqueId(), amount);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean deposit(@NonNull UUID playerId, double amount) {
        api.deposit(playerId, CurrencyId.VAULT, amount);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean deposit(@NonNull Player player, @NonNull String id, double amount) {
        return deposit(player.getUniqueId(), id, amount);
    }

    @Deprecated(forRemoval = true)
    public static boolean deposit(@NonNull UUID playerId, @NonNull String id, double amount) {
        api.deposit(playerId, id, amount);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean depositEconomy(@NonNull Player player, double amount) {
        return depositEconomy(player.getUniqueId(), amount);
    }

    @Deprecated(forRemoval = true)
    public static boolean depositEconomy(@NonNull UUID playerId, double amount) {
        api.deposit(playerId, CurrencyId.VAULT, amount);
        return true;
    }



    @Deprecated(forRemoval = true)
    public static boolean withdraw(@NonNull Player player, double amount) {
        return withdraw(player.getUniqueId(), amount);
    }

    @Deprecated(forRemoval = true)
    public static boolean withdraw(@NonNull UUID playerId, double amount) {
        api.withdraw(playerId, CurrencyId.VAULT, amount);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean withdraw(@NonNull Player player, @NonNull String id, double amount) {
        withdraw(player.getUniqueId(), id, amount);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean withdraw(@NonNull UUID playerId, @NonNull String id, double amount) {
        api.withdraw(playerId, id, amount);
        return true;
    }

    @Deprecated(forRemoval = true)
    public static boolean withdrawEconomy(@NonNull Player player, double amount) {
        return withdrawEconomy(player.getUniqueId(), amount);
    }

    @Deprecated(forRemoval = true)
    public static boolean withdrawEconomy(@NonNull UUID playerId, double amount) {
        api.withdraw(playerId, CurrencyId.VAULT, amount);
        return true;
    }

    /**
     * When a plugin uses a separate Vault account to pay from (see CoreConfig VAULT_DEBIT_ACCOUNT),
     * use this to resolve the account to debit. If none is set, the payer is charged as usual.
     * When {@link #deposit(UUID, String, double)} returns false and this is present, the debit account
     * had insufficient balance; the caller is responsible for notifying players when appropriate.
     *
     * @return Optional UUID of the account to debit for Vault economy; empty if the payer should be charged.
     */
    @NonNull
    public static Optional<UUID> getVaultDebitAccountId() {
        return CoreConfig.VAULT_DEBIT_ACCOUNT.get();
    }

    /**
     * Resolves the account to debit for a Vault economy payment: the configured debit account if set,
     * otherwise the payer. Use this before {@link #hasEnough(UUID, String, double)} and
     * {@link #withdraw(UUID, String, double)} so the correct account is checked and charged.
     *
     * @param payerId the player initiating the payment (used when no debit account is configured).
     * @return UUID of the account to debit.
     */
    @NonNull
    public static UUID getVaultDebitAccountIdOrPayer(@NonNull UUID payerId) {
        return getVaultDebitAccountId().orElse(payerId);
    }

    @Deprecated(forRemoval = true)
    public static boolean hasCurrency() {
        return api.hasAnyCurrency();
    }

    @Deprecated(forRemoval = true)
    public static void register(@NonNull Currency currency) {
        api.register(currency);
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Set<Currency> getCurrencies() {
        return api.getCurrencies();
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Set<String> getCurrencyIds() {
        return api.getCurrencyIds();
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Optional<Currency> currency(@NonNull String internalId) {
        return Optional.ofNullable(getCurrency(internalId));
    }

    @Nullable
    @Deprecated(forRemoval = true)
    public static Currency getCurrency(@NonNull String internalId) {
        return api.getCurrency(internalId);
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Currency getCurrencyOrDummy(@NonNull String internalId) {
        return api.getCurrencyOrDummy(internalId);
    }

    @Nullable
    @Deprecated(forRemoval = true)
    public static Currency getEconomyCurrency() {
        return getCurrency(CurrencyId.VAULT);
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Optional<Currency> economyCurrency() {
        return Optional.ofNullable(getEconomyCurrency());
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static DummyCurrency getDummyCurrency() {
        return DummyCurrency.INSTANCE;
    }
}
