package su.nightexpress.nightcore.integration.currency.type;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.currency.CurrencySettings;

public abstract class IncompleteCurrency extends AbstractCurrency {

    protected CurrencySettings settings;

    public IncompleteCurrency(@NonNull String id) {
        super(id);
    }

    public IncompleteCurrency(@NonNull String originalId, @NonNull String internalId) {
        super(originalId, internalId);
        //this.setSettings(this.getDefaultSettings());
    }

    @NonNull
    public abstract CurrencySettings getDefaultSettings();

    @NonNull
    public CurrencySettings getSettings() {
        return this.settings;
    }

    public void setSettings(@NonNull CurrencySettings settings) {
        this.settings = settings;
    }

    @Override
    @NonNull
    public String getName() {
        return this.settings.getName();
    }

    @Override
    @NonNull
    public String getFormat() {
        return this.settings.getFormat();
    }

    @Override
    @NonNull
    public ItemStack getIcon() {
        return this.settings.getIcon().getItemStack();
    }
}
