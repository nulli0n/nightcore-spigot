package su.nightexpress.nightcore.integration.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.Registries;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;
import su.nightexpress.nightcore.core.CoreConfig;
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

    /**
     * Deposits the given amount to the player. When the currency is Vault and a debit account
     * is configured (see {@link #getVaultDebitAccountId()}), the amount is taken from that account
     * instead of creating money. If the debit account has insufficient balance, no transfer is made
     * and this returns false. The caller is responsible for notifying players when appropriate.
     * To avoid failed payments when a debit account is set, check
     * {@link #hasEnough(UUID, String, double)} with the debit account UUID before depositing.
     */
    public static boolean deposit(@NotNull Player player, @NotNull String id, double amount) {
        return deposit(player.getUniqueId(), id, amount);
    }

    public static boolean deposit(@NotNull UUID playerId, @NotNull String id, double amount) {
        if (CurrencyId.VAULT.equals(id)) {
            Optional<UUID> debitAccountId = getVaultDebitAccountId();
            if (debitAccountId.isPresent()) {
                Currency currency = getCurrency(id);
                if (currency == null) return false;
                UUID debitId = debitAccountId.get();
                if (getBalance(debitId, id) < amount) return false;
                currency.take(debitId, amount);
                currency.give(playerId, amount);
                return true;
            }
        }
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

    /**
     * When a plugin uses a separate Vault account to pay from (see CoreConfig VAULT_DEBIT_ACCOUNT),
     * use this to resolve the account to debit. If none is set, the payer is charged as usual.
     * When {@link #deposit(UUID, String, double)} returns false and this is present, the debit account
     * had insufficient balance; the caller is responsible for notifying players when appropriate.
     *
     * @return Optional UUID of the account to debit for Vault economy; empty if the payer should be charged.
     */
    @NotNull
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
    @NotNull
    public static UUID getVaultDebitAccountIdOrPayer(@NotNull UUID payerId) {
        return getVaultDebitAccountId().orElse(payerId);
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
