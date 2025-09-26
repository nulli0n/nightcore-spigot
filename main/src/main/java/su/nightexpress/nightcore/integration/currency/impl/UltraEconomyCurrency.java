package su.nightexpress.nightcore.integration.currency.impl;

import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.base.item.XMaterial;
import me.TechsCode.UltraEconomy.objects.Account;
import me.TechsCode.UltraEconomy.objects.Currency;
import me.TechsCode.UltraEconomy.objects.CurrencyFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.coinsengine.Placeholders;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.type.AbstractCurrency;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UltraEconomyCurrency extends AbstractCurrency {

    public UltraEconomyCurrency(@NotNull String id) {
        super(id, CurrencyId.forUltraEconomy(id));
    }

    @NotNull
    private Optional<Currency> currency() {
        return UltraEconomy.getAPI().getCurrencies().key(this.originalId);
    }

    @NotNull
    public static Set<UltraEconomyCurrency> getCurrencies() {
        return UltraEconomy.getAPI().getCurrencies().stream().map(Currency::getKey).map(UltraEconomyCurrency::new).collect(Collectors.toSet());
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
    @NotNull
    public String format(double amount) {
        return this.currency().map(Currency::getFormat).map(format -> format.formattedFormat(amount)).orElse(String.valueOf(amount));
    }

    @Override
    @NotNull
    public String formatValue(double amount) {
        return this.currency().map(Currency::getFormat).map(format -> format.format(amount)).orElse(String.valueOf(amount));
    }

    @Override
    @NotNull
    public String getName() {
        return this.currency().map(Currency::getName).orElse(this.originalId);
    }

    @Override
    @NotNull
    public String getFormat() {
        return this.currency().map(Currency::getFormat).map(CurrencyFormat::getPluralFormat).orElse(Placeholders.GENERIC_AMOUNT);
    }

    @Override
    @NotNull
    public ItemStack getIcon() {
        return this.currency().map(Currency::getIcon).flatMap(XMaterial::getAsItemStack).orElse(new ItemStack(Material.GOLD_INGOT));
    }



    @Override
    public double getBalance(@NotNull Player player) {
        return this.currency().map(currency -> this.getAccount(player).map(account -> account.getBalance(currency).getOnHand()).orElse(0D)).orElse(0D);
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return this.currency().map(currency -> this.getAccount(playerId).map(account -> account.getBalance(currency).getOnHand()).orElse(0D)).orElse(0D);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        this.currency().ifPresent(currency -> this.getAccount(player).ifPresent(account -> account.getBalance(currency).addHand(amount)));
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {
        this.currency().ifPresent(currency -> this.getAccount(playerId).ifPresent(account -> account.getBalance(currency).addHand(amount)));
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        this.currency().ifPresent(currency -> this.getAccount(player).ifPresent(account -> account.getBalance(currency).removeHand(amount)));
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {
        this.currency().ifPresent(currency -> this.getAccount(playerId).ifPresent(account -> account.getBalance(currency).removeHand(amount)));
    }



    private Optional<Account> getAccount(@NotNull Player player) {
        return UltraEconomy.getAPI().getAccounts().name(player.getName());
    }

    private Optional<Account> getAccount(@NotNull UUID playerId) {
        return UltraEconomy.getAPI().getAccounts().uuid(playerId);
    }
}
