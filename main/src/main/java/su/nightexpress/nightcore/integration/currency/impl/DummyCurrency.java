package su.nightexpress.nightcore.integration.currency.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.type.AbstractCurrency;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.Placeholders;

import java.util.UUID;

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
    @NotNull
    public String getName() {
        return "Dummy";
    }

    @Override
    @NotNull
    public String getFormat() {
        return Placeholders.GENERIC_AMOUNT + " [Dummy]";
    }

    @Override
    @NotNull
    public ItemStack getIcon() {
        return new ItemStack(Material.BRICK);
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return 0;
    }

    @Override
    public double getBalance(@NotNull UUID playerId) {
        return 0;
    }

    @Override
    public void give(@NotNull Player player, double amount) {

    }

    @Override
    public void give(@NotNull UUID playerId, double amount) {

    }

    @Override
    public void take(@NotNull Player player, double amount) {

    }

    @Override
    public void take(@NotNull UUID playerId, double amount) {

    }
}
