package su.nightexpress.nightcore.integration.currency;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.bridge.currency.EconomyBridgeAPI;
import su.nightexpress.nightcore.integration.currency.impl.DummyCurrency;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EconomyBridgeProvider implements EconomyBridgeAPI {

    private final CurrencyRegistry registry;

    public EconomyBridgeProvider(@NonNull CurrencyRegistry registry) {
        this.registry = registry;
    }

    @Override
    public boolean hasCurrency(@NonNull String id) {
        return this.registry.contains(id);
    }

    @Override
    public boolean hasVaultCurrency() {
        return this.hasCurrency(CurrencyId.VAULT);
    }

    @Override
    public boolean hasAnyCurrency() {
        return !this.registry.isEmpty();
    }

    @Override
    public void register(@NonNull Currency currency) {
        this.registry.register(currency);
    }

    @Override
    public boolean unregister(@NonNull Currency currency) {
        return this.unregister(currency.getInternalId());
    }

    @Override
    public boolean unregister(@NonNull String id) {
        return this.registry.unregister(id);
    }

    @NonNull
    public Set<Currency> getCurrencies() {
        return this.registry.values();
    }

    @NonNull
    public Set<String> getCurrencyIds() {
        return this.registry.keys();
    }

    @Override
    @NonNull
    public Optional<Currency> currency(@NonNull String internalId) {
        return this.registry.byId(internalId);
    }

    @Override
    @Nullable
    public Currency getCurrency(@NonNull String internalId) {
        return registry.getById(internalId);
    }

    @Override
    @NonNull
    public Currency getCurrencyOrDummy(@NonNull String internalId) {
        return registry.byId(internalId).orElse(DummyCurrency.INSTANCE);
    }

    @Override
    @Nullable
    public Currency getVaultCurrency() {
        return getCurrency(CurrencyId.VAULT);
    }

    @Override
    @NonNull
    public Optional<Currency> vaultCurrency() {
        return Optional.ofNullable(getVaultCurrency());
    }

    @Override
    @NonNull
    public DummyCurrency getDummyCurrency() {
        return DummyCurrency.INSTANCE;
    }




    @Override
    public double queryBalance(@NonNull Player player) {
        return queryBalance(player, CurrencyId.VAULT);
    }

    @Override
    public double queryBalance(@NonNull UUID playerId) {
        return queryBalance(playerId, CurrencyId.VAULT);
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return queryBalanceAsync(player, CurrencyId.VAULT);
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return queryBalanceAsync(playerId, CurrencyId.VAULT);
    }

    @Override
    public double queryBalance(@NonNull Player player, @NonNull String id) {
        return currency(id).map(currency -> currency.queryBalance(player)).orElse(0D);
    }

    @Override
    public double queryBalance(@NonNull UUID playerId, @NonNull String id) {
        return currency(id).map(currency -> currency.queryBalance(playerId)).orElse(0D);
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player, @NonNull String id) {
        return currency(id).map(currency -> currency.queryBalanceAsync(player)).orElse(CompletableFuture.completedFuture(0D));
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId, @NonNull String id) {
        return currency(id).map(currency -> currency.queryBalanceAsync(playerId)).orElse(CompletableFuture.completedFuture(0D));
    }



    @Override
    public void deposit(@NonNull Player player, double amount) {
        deposit(player.getUniqueId(), amount);
    }

    @Override
    public void deposit(@NonNull UUID playerId, double amount) {
        deposit(playerId, CurrencyId.VAULT, amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return depositAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        return depositAsync(playerId, CurrencyId.VAULT, amount);
    }

    @Override
    public void deposit(@NonNull Player player, @NonNull String id, double amount) {
        deposit(player.getUniqueId(), id, amount);
    }

    @Override
    public void deposit(@NonNull UUID playerId, @NonNull String id, double amount) {
        currency(id).ifPresent(currency -> currency.deposit(playerId, amount));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, @NonNull String id, double amount) {
        return currency(id).map(currency -> currency.depositAsync(player, amount)).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, @NonNull String id, double amount) {
        return currency(id).map(currency -> currency.depositAsync(playerId, amount)).orElse(CompletableFuture.completedFuture(false));
    }



    @Override
    public void withdraw(@NonNull Player player, double amount) {
        withdraw(player.getUniqueId(), amount);
    }

    @Override
    public void withdraw(@NonNull UUID playerId, double amount) {
        withdraw(playerId, CurrencyId.VAULT, amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawsync(@NonNull Player player, double amount) {
        return withdrawAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        return withdrawAsync(playerId, CurrencyId.VAULT, amount);
    }

    @Override
    public void withdraw(@NonNull Player player, @NonNull String id, double amount) {
        withdraw(player.getUniqueId(), id, amount);
    }

    @Override
    public void withdraw(@NonNull UUID playerId, @NonNull String id, double amount) {
        currency(id).ifPresent(currency -> currency.withdraw(playerId, amount));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawsync(@NonNull Player player, @NonNull String id, double amount) {
        return currency(id).map(currency -> currency.withdrawAsync(player, amount)).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, @NonNull String id, double amount) {
        return currency(id).map(currency -> currency.withdrawAsync(playerId, amount)).orElse(CompletableFuture.completedFuture(false));
    }
}
