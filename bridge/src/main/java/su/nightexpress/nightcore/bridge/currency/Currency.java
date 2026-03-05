package su.nightexpress.nightcore.bridge.currency;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public interface Currency {

    boolean isDummy();

    @NonNull UnaryOperator<String> replacePlaceholders();

    double floorIfNeeded(double amount);

    @NonNull String formatValue(double amount);

    @NonNull String format(double amount);

    @NonNull String applyFormat(@NonNull String format, double amount);



    @Deprecated(forRemoval = true)
    default double getBalance(@NonNull Player player) {
        return this.queryBalance(player);
    }

    @Deprecated(forRemoval = true)
    default double getBalance(@NonNull UUID playerId) {
        return this.queryBalance(playerId);
    }

    @Deprecated(forRemoval = true)
    default void give(@NonNull Player player, double amount) {
        this.give(player.getUniqueId(), amount);
    }

    @Deprecated(forRemoval = true)
    default void give(@NonNull UUID playerId, double amount) {
        this.depositAsync(playerId, amount);
    }

    @Deprecated(forRemoval = true)
    default void take(@NonNull Player player, double amount) {
        this.take(player.getUniqueId(), amount);
    }

    @Deprecated(forRemoval = true)
    default void take(@NonNull UUID playerId, double amount) {
        this.withdrawAsync(playerId, amount);
    }




    double queryBalance(@NonNull Player player);

    double queryBalance(@NonNull UUID playerId);

    @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull Player player);

    @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId);

    void deposit(@NonNull Player player, double amount);

    void deposit(@NonNull UUID playerId, double amount);

    @NonNull CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount);

    @NonNull CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount);

    void withdraw(@NonNull Player player, double amount);

    void withdraw(@NonNull UUID playerId, double amount);

    @NonNull CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount);

    @NonNull CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount);



    boolean canHandleDecimals();

    boolean canHandleOffline();



    @NonNull String getOriginalId();

    @NonNull String getInternalId();

    @NonNull String getName();

    @NonNull String getFormat();

    @NonNull ItemStack getIcon(); // TODO NightItem
}
