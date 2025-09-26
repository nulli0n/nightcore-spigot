package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.List;
import java.util.UUID;

public class ItemStackCurrency extends IncompleteCurrency implements Writeable {

    private ItemStack itemStack;

    public ItemStackCurrency(@NotNull String id, @NotNull ItemStack itemStack) {
        super(id);
        this.setItemStack(itemStack);
    }

    @NotNull
    public static List<ItemStackCurrency> createDefaults() {
        return Lists.newList(
            new ItemStackCurrency("gold", new ItemStack(Material.GOLD_INGOT)),
            new ItemStackCurrency("emerald", new ItemStack(Material.EMERALD)),
            new ItemStackCurrency("custom_diamond", NightItem.fromType(Material.DIAMOND).setDisplayName(TagWrappers.AQUA.wrap("Custom Diamond")).getItemStack())
        );
    }

    @NotNull
    public static ItemStackCurrency read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) throws IllegalStateException {
        ItemTag tag = ItemTag.read(config, path + ".Tag");
        ItemStack itemStack = tag.getItemStack();
        if (itemStack == null) throw new IllegalStateException("Invalid ItemStack tag: '" + tag.getTag() + "'.");

        return new ItemStackCurrency(id, itemStack);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
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
    @NotNull
    public CurrencySettings getDefaultSettings() {
        return new CurrencySettings(ItemUtil.getNameSerialized(this.itemStack), Placeholders.GENERIC_AMOUNT + "x " + Placeholders.GENERIC_NAME, NightItem.fromItemStack(this.itemStack));
    }

    public void setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = new ItemStack(itemStack);
    }

    @NotNull
    public ItemStack getItemStack() {
        return new ItemStack(this.itemStack);
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return Players.countItem(player, this.itemStack);
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return 0;
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        Players.addItem(player, this.itemStack, (int) amount);
    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {

    }

    @Override
    public void take(@NotNull Player player, double amount) {
        Players.takeItem(player, this.itemStack, (int) amount);
    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {

    }
}
