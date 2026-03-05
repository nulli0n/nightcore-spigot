package su.nightexpress.nightcore.bridge.currency;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EconomyBridgeAPI {

    boolean hasCurrency(@NonNull String id);

    boolean hasVaultCurrency();

    boolean hasAnyCurrency();

    void register(@NonNull Currency currency);

    boolean unregister(@NonNull Currency currency);

    boolean unregister(@NonNull String id);

    @NonNull Set<Currency> getCurrencies();

    @NonNull Set<String> getCurrencyIds();

    @NonNull Optional<Currency> currency(@NonNull String internalId);

    @Nullable Currency getCurrency(@NonNull String internalId);

    @NonNull Currency getCurrencyOrDummy(@NonNull String internalId);

    @Nullable Currency getVaultCurrency();

    @NonNull Optional<Currency> vaultCurrency();

    @NonNull Currency getDummyCurrency();



    double queryBalance(@NonNull Player player);

    double queryBalance(@NonNull UUID playerId);

    @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull Player player);

    @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId);

    double queryBalance(@NonNull Player player, @NonNull String id);

    double queryBalance(@NonNull UUID playerId, @NonNull String id);

    @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull Player player, @NonNull String id);

    @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId, @NonNull String id);



    void deposit(@NonNull Player player, double amount);

    void deposit(@NonNull UUID playerId, double amount);

    @NonNull CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount);

    @NonNull CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount);

    void deposit(@NonNull Player player, @NonNull String id, double amount);

    void deposit(@NonNull UUID playerId, @NonNull String id, double amount);

    @NonNull CompletableFuture<Boolean> depositAsync(@NonNull Player player, @NonNull String id, double amount);

    @NonNull CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, @NonNull String id, double amount);



    void withdraw(@NonNull Player player, double amount);

    void withdraw(@NonNull UUID playerId, double amount);

    @NonNull CompletableFuture<Boolean> withdrawsync(@NonNull Player player, double amount);

    @NonNull CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount);

    void withdraw(@NonNull Player player, @NonNull String id, double amount);

    void withdraw(@NonNull UUID playerId, @NonNull String id, double amount);

    @NonNull CompletableFuture<Boolean> withdrawsync(@NonNull Player player, @NonNull String id, double amount);

    @NonNull CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, @NonNull String id, double amount);
}
