package su.nightexpress.nightcore.integration.currency.type;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;

public abstract class IncompleteCurrency extends AbstractCurrency {

    protected CurrencySettings settings;

    public IncompleteCurrency(@NotNull String id) {
        super(id);
    }

    public IncompleteCurrency(@NotNull String originalId, @NotNull String internalId) {
        super(originalId, internalId);
        //this.setSettings(this.getDefaultSettings());
    }

    @NotNull
    public abstract CurrencySettings getDefaultSettings();

    @NotNull
    public CurrencySettings getSettings() {
        return this.settings;
    }

    public void setSettings(@NotNull CurrencySettings settings) {
        this.settings = settings;
    }

    @Override
    @NotNull
    public String getName() {
        return this.settings.getName();
    }

    @Override
    @NotNull
    public String getFormat() {
        return this.settings.getFormat();
    }

    @Override
    @NotNull
    public  ItemStack getIcon() {
        return this.settings.getIcon().getItemStack();
    }
}
