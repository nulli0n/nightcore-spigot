package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ItemStackCurrency extends IncompleteCurrency implements Writeable {

    private ItemStack itemStack;

    public ItemStackCurrency(@NonNull String id, @NonNull ItemStack itemStack) {
        super(id);
        this.setItemStack(itemStack);
    }

    @NonNull
    public static List<ItemStackCurrency> createDefaults() {
        return Lists.newList(
            new ItemStackCurrency("gold", new ItemStack(Material.GOLD_INGOT)),
            new ItemStackCurrency("emerald", new ItemStack(Material.EMERALD)),
            new ItemStackCurrency("custom_diamond", NightItem.fromType(Material.DIAMOND).setDisplayName(TagWrappers.AQUA.wrap("Custom Diamond")).getItemStack())
        );
    }

    @NonNull
    public static ItemStackCurrency read(@NonNull FileConfig config, @NonNull String path, @NonNull String id) throws IllegalStateException {
        ItemTag tag = ItemTag.read(config, path + ".Tag");
        ItemStack itemStack = tag.getItemStack();
        if (itemStack == null) throw new IllegalStateException("Invalid ItemStack tag: '" + tag.getTag() + "'.");

        return new ItemStackCurrency(id, itemStack);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Tag", ItemTag.of(this.itemStack));
    }

    @Override
    public boolean canHandleDecimals() {
        return false;
    }

    @Override
    public boolean canHandleOffline() {
        return false;
    }

    @Override
    @NonNull
    public CurrencySettings getDefaultSettings() {
        return new CurrencySettings(ItemUtil.getNameSerialized(this.itemStack), Placeholders.GENERIC_AMOUNT + "x " + Placeholders.GENERIC_NAME, NightItem.fromItemStack(this.itemStack));
    }

    public void setItemStack(@NonNull ItemStack itemStack) {
        this.itemStack = new ItemStack(itemStack);
    }

    @NonNull
    public ItemStack getItemStack() {
        return new ItemStack(this.itemStack);
    }


    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull Player player) {
        return CompletableFuture.completedFuture(this.queryBalanceDirect(player));
    }

    @Override
    @NonNull
    public CompletableFuture<Double> queryBalanceAsync(@NonNull UUID playerId) {
        return CompletableFuture.completedFuture(Players.findById(playerId).map(this::queryBalanceDirect).orElse(0D));
    }

    @Override
    protected double queryBalanceDirect(@NonNull Player player) {
        return Players.countItem(player, this.itemStack);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull Player player, double amount) {
        this.depositDirect(player, amount);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> depositAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.completedFuture(Players.findById(playerId).map(player -> {
            this.depositDirect(player, amount);
            return true;
        }).orElse(false));
    }

    @Override
    protected void depositDirect(@NonNull Player player, double amount) {
        Players.addItem(player, this.itemStack, (int) amount);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull Player player, double amount) {
        this.withdrawDirect(player, amount);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    @NonNull
    public CompletableFuture<Boolean> withdrawAsync(@NonNull UUID playerId, double amount) {
        return CompletableFuture.completedFuture(Players.findById(playerId).map(player -> {
            this.withdrawDirect(player, amount);
            return true;
        }).orElse(false));
    }

    @Override
    protected void withdrawDirect(@NonNull Player player, double amount) {
        Players.takeItem(player, this.itemStack, (int) amount);
    }
}
