package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.type.AbstractCurrency;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DummyCurrency extends AbstractCurrency {

    public static final DummyCurrency INSTANCE = new DummyCurrency();

    DummyCurrency() {
        super(CurrencyId.DUMMY);
    }

    @Override
    public boolean canHandleDecimals() {
        return true;
    }

    @Override
    public boolean canHandleOffline() {
        return false;
    }

    @Override
    @NonNull
    public String getName() {
        return "Dummy";
    }

    @Override
    @NonNull
    public String getFormat() {
        return Placeholders.GENERIC_AMOUNT + " [Dummy]";
    }

    @Override
    @NonNull
    public ItemStack getIcon() {
        return new ItemStack(Material.BRICK);
    }

    @Override
    public @NonNull CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return CompletableFuture.completedFuture(0D);
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(0D);
    }

    @Override
    protected double queryBalanceDirect(@NonNull Player player) {
        return 0;
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.completedFuture(false);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return CompletableFuture.completedFuture(false);
    }

    @Override
    protected void depositDirect(@NonNull Player player, double amount) {

    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        return CompletableFuture.completedFuture(false);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.completedFuture(false);
    }

    @Override
    protected void withdrawDirect(@NonNull Player player, double amount) {

    }
}
