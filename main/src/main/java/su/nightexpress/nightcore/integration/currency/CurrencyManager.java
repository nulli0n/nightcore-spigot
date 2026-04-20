package su.nightexpress.nightcore.integration.currency;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.integration.currency.command.CurrencyCommands;
import su.nightexpress.nightcore.integration.currency.impl.*;
import su.nightexpress.nightcore.integration.currency.listener.CurrencyListener;
import su.nightexpress.nightcore.integration.currency.type.IncompleteCurrency;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CurrencyManager extends AbstractManager<NightCore> {

    public static final String FILE_NAME = "currencies.yml";

    private final CurrencyRegistry      registry;
    private final Map<String, Runnable> pluginProviders;

    private FileConfig config;

    public CurrencyManager(@NotNull NightCore plugin) {
        super(plugin);
        this.registry = new CurrencyRegistry();
        this.pluginProviders = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        EconomyBridge.register(new EconomyBridgeProvider(this.registry));

        this.config = FileConfig.loadOrExtract(this.plugin, FILE_NAME);

        this.loadProviders();
        this.loadBuiltInCurrencies();
        this.loadItemCurrencies();
        CurrencyCommands.load(this.plugin);

        this.addListener(new CurrencyListener(this.plugin, this));

        // Clean up when all plugins are loaded.
        this.plugin.runTask(() -> {
            this.pluginProviders.clear();
            this.config.saveChanges();
        });
    }

    @Override
    protected void onShutdown() {
        this.pluginProviders.clear();
        CurrencyCommands.shutdown();
    }

    public void handlePluginLoad(@NotNull String pluginName) {
        var provider = this.pluginProviders.get(pluginName);
        if (provider != null) {
            plugin.info(pluginName + " detected! Loading currency...");
            provider.run();
        }
    }

    private void loadProviders() {
        this.addExternalLoader(CurrencyPlugins.VAULT, () -> this.loadIncompleted(VaultEconomyCurrency::new));
        this.addExternalLoader(CurrencyPlugins.PLAYER_POINTS, () -> this.loadIncompleted(PlayerPointsCurrency::new));
        this.addExternalLoader(CurrencyPlugins.VOTING_PLUGIN, () -> this.loadIncompleted(VotingPluginCurrency::new));
        this.addExternalLoader(CurrencyPlugins.ELITEMOBS, () -> this.loadIncompleted(EliteMobsCurrency::new));
        this.addExternalLoader(CurrencyPlugins.COINS_ENGINE, () -> ExcellentEconomyCurrency
            .getCurrencies(CurrencyId::forCoinsEngine).forEach(this::register));

        // Two variants for compatibility.
        this.addExternalLoader(CurrencyPlugins.EXCELLENT_ECONOMY, () -> {
            ExcellentEconomyCurrency.getCurrencies(CurrencyId::forCoinsEngine).forEach(this::register);
            ExcellentEconomyCurrency.getCurrencies(CurrencyId::forExcellentEconomy).forEach(this::register);
        });


        // Try load any provider(s) of the plugins that are already enabled aka loaded.
        this.pluginProviders.keySet().forEach(pluginName -> {
            Plugin currencyPlugin = this.plugin.getPluginManager().getPlugin(pluginName);
            if (currencyPlugin == null || !currencyPlugin.isEnabled()) return;

            this.handlePluginLoad(pluginName);
        });
    }

    public void loadBuiltInCurrencies() {
        this.loadIncompleted(XPLevelsCurrency::new);
    }

    public void loadItemCurrencies() {
        String path = "Items";

        if (!this.config.contains(path)) {
            ItemStackCurrency.createDefaults().forEach(currency -> this.config.set(path + "." + currency
                .getInternalId(), currency));
        }

        this.config.getSection(path).forEach(sId -> {
            try {
                this.loadIncompleted(() -> ItemStackCurrency.read(this.config, path + "." + sId, sId));
            }
            catch (IllegalStateException exception) {
                this.plugin.error("Item currency '" + sId + "' not loaded: " + exception.getMessage());
            }
        });
    }

    @Nullable
    public ItemStackCurrency createItemCurrency(@NotNull String name, @NotNull ItemStack itemStack) {
        String id = Strings.filterForVariable(name);
        if (this.registry.contains(id)) return null;

        ItemStackCurrency currency = new ItemStackCurrency(id, itemStack);

        this.saveCurrency(currency);
        this.loadIncompleted(() -> currency);
        return currency;
    }

    private void saveCurrency(@NotNull ItemStackCurrency currency) {
        this.config.set("Items." + currency.getInternalId(), currency);
        this.config.save();
    }

    private void addExternalLoader(@NotNull String pluginName, @NotNull Runnable runnable) {
        if (!Plugins.isInstalled(pluginName)) return;

        this.pluginProviders.put(pluginName, runnable);
    }

    public <T extends IncompleteCurrency> void loadIncompleted(@NotNull Supplier<T> supplier) {
        this.register(supplier.get(), currency -> {
            String path = currency instanceof ItemStackCurrency ? "Items" : "Currencies";
            CurrencySettings settings = ConfigValue.create(path + "." + currency.getInternalId() + ".Settings",
                CurrencySettings::load, currency.getDefaultSettings()).read(this.config);
            currency.setSettings(settings);
        });
    }

    private void register(@NotNull Currency currency) {
        this.register(currency, null);
    }

    private <T extends Currency> void register(@NotNull T currency, @Nullable Consumer<T> preRegister) {
        String id = currency.getInternalId();
        if (CoreConfig.ECONOMY_DISABLED_PROVIDERS.get().contains(id)) return;

        if (preRegister != null) preRegister.accept(currency);

        this.registry.register(currency);
        this.plugin.info("Currency registered: '" + id + "'.");
    }
}
