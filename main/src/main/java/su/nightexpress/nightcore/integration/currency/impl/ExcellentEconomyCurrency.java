package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;
import su.nightexpress.excellenteconomy.api.currency.ExcellentCurrency;
import su.nightexpress.excellenteconomy.api.currency.operation.NotificationTarget;
import su.nightexpress.excellenteconomy.api.currency.operation.OperationContext;
import su.nightexpress.excellenteconomy.api.currency.operation.OperationResult;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.type.AbstractCurrency;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.ServerUtils;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ExcellentEconomyCurrency extends AbstractCurrency {

    private static ExcellentEconomyAPI api;
    private static OperationContext context;

    public ExcellentEconomyCurrency(@NonNull String id) {
        super(id, CurrencyId.forCoinsEngine(id));
    }

    @NonNull
    private static ExcellentEconomyAPI getAPI() {
        if (api == null) {
            api = ServerUtils.serviceProvider(ExcellentEconomyAPI.class).orElseThrow(() -> new IllegalStateException("No ExcellentEconomy API service found"));
            context = OperationContext.custom("NightCore - EconomyBridge").silentFor(NotificationTarget.EXECUTOR, NotificationTarget.USER, NotificationTarget.CONSOLE_LOGGER);
        }
        return api;
    }

    @NonNull
    public static Set<ExcellentEconomyCurrency> getCurrencies() {
        Set<ExcellentEconomyCurrency> currencies = new HashSet<>();
        getAPI().currencyRegistry().stream().filter(Predicate.not(ExcellentCurrency::isPrimary)).forEach(currency -> {
            currencies.add(new ExcellentEconomyCurrency(currency.getId()));
        });
        return currencies;
    }

    @NonNull
    private Optional<ExcellentCurrency> currency() {
        return Optional.ofNullable(getAPI().getCurrency(this.originalId));
    }

    @Override
    @NonNull
    public UnaryOperator<String> replacePlaceholders() {
        PlaceholderContext.Builder builder = PlaceholderContext.builder();
        this.currency().ifPresent(currency -> builder.with(currency.placeholders()));
        return str -> builder.build().apply(str);
    }

    @Override
    public boolean canHandleDecimals() {
        return this.currency().map(ExcellentCurrency::isDecimal).orElse(true);
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
    @NonNull
    public String formatValue(double amount) {
        return this.currency().map(currency -> currency.formatValue(amount)).orElse(String.valueOf(amount));
    }

    @Override
    @NonNull
    public String getName() {
        return this.currency().map(ExcellentCurrency::getName).orElse(this.getOriginalId());
    }

    @Override
    @NonNull
    public String getFormat() {
        return this.currency().map(ExcellentCurrency::getFormat).orElse(Placeholders.GENERIC_AMOUNT);
    }

    @Override
    @NonNull
    public ItemStack getIcon() {
        return this.currency().map(ExcellentCurrency::icon).orElse(NightItem.fromType(Material.GOLD_NUGGET)).getItemStack();
    }



    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return this.queryBalanceAsync(player.getUniqueId());
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return getAPI().getBalanceAsync(playerId, this.originalId);
    }

    @Override
    protected double queryBalanceDirect(@NonNull Player player) {
        return getAPI().getCachedUserData(player).getBalance().get(this.originalId);
    }



    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        return this.depositAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        return getAPI().depositAsync(playerId, this.originalId, amount, context).thenApply(OperationResult::bool);
    }

    @Override
    protected void depositDirect(@NonNull Player player, double amount) {
        getAPI().deposit(player, this.originalId, amount, context);
    }


    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        return this.withdrawAsync(player.getUniqueId(), amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        return getAPI().withdrawAsync(playerId, this.originalId, amount, context).thenApply(OperationResult::bool);
    }

    @Override
    protected void withdrawDirect(@NonNull Player player, double amount) {
        getAPI().withdraw(player, this.originalId, amount, context);
    }
}
