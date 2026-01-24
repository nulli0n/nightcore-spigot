package su.nightexpress.nightcore.core;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.core.listener.CoreListener;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogListener;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.ItemPlugins;
import su.nightexpress.nightcore.integration.item.adapter.impl.*;
import su.nightexpress.nightcore.integration.permission.PermissionBridge;
import su.nightexpress.nightcore.integration.permission.PermissionPlugins;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.integration.permission.impl.LuckPermissionProvider;
import su.nightexpress.nightcore.integration.permission.impl.VaultPermissionProvider;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.menu.MenuListener;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;
import su.nightexpress.nightcore.ui.UIListener;
import su.nightexpress.nightcore.ui.dialog.DialogManager;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuRegistry;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;

import java.util.HashSet;
import java.util.function.Supplier;

public class CoreManager extends AbstractManager<NightCore> {

    public CoreManager(@NotNull NightCore core) {
        super(core);
    }

    @Override
    protected void onLoad() {
        this.loadPermissionIntegrations();
        this.loadItemIntegrations();

        this.addListener(new CoreListener(this.plugin));
        this.addListener(new DialogListener(this.plugin));
        this.addListener(new MenuListener(this.plugin));
        this.addListener(new UIListener(this.plugin));

        this.addTask(this::tickMenusAndDialogs, 1);
        this.addAsyncTask(PlayerProfiles::purgeProfiles, CoreConfig.PROFILE_PURGE_INTERVAL.get());
        this.addAsyncTask(this::updateProfiles, CoreConfig.PROFILE_UPDATE_INTERVAL.get());
    }

    @Override
    protected void onShutdown() {
        Dialog.shutdown();
        DialogManager.shutdown();

        EconomyBridge.unregisterAll();
        ItemBridge.unregisterAll();
        PermissionBridge.unregisterAll();
    }

    private void loadPermissionIntegrations() {
        if (Plugins.isInstalled(PermissionPlugins.LUCK_PERMS)) {
            this.registerPermissionProvider(new LuckPermissionProvider());
        }
        else if (Plugins.isInstalled(PermissionPlugins.VAULT)) {
            this.registerPermissionProvider(new VaultPermissionProvider());
        }
    }

    private void registerPermissionProvider(@NotNull PermissionProvider provider) {
        PermissionBridge.register(provider);
        this.plugin.info("Registered permissions provider: " + provider.getName());
    }

    private void loadItemIntegrations() {
        this.registerItemProvider(VanillaItemAdapter.INSTANCE);
        this.registerExternalItemProvider(ItemPlugins.EXCELLENT_CRATES, ExcellentCratesHandler::new);
        this.registerExternalItemProvider(ItemPlugins.EXCELLENT_CRATES, ECratesCrateAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.EXCELLENT_CRATES, ECratesKeyAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.EXECUTABLE_ITEMS, ExecutableItemsAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.ITEMS_ADDER, ItemsAdderAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.MMOITEMS, MMOItemsAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.NEXO, NexoAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.ORAXEN, OraxenAdapter::new);
        this.registerExternalItemProvider(ItemPlugins.CRAFT_ENGINE, CraftEngineAdapter::new);
    }

    private <T> boolean registerExternalItemProvider(@NotNull String pluginName, @NotNull Supplier<ItemAdapter<T>> supplier) {
        if (!Plugins.isInstalled(pluginName)) return false;

        return registerItemProvider(supplier.get());
    }

    private <T> boolean registerItemProvider(@NotNull ItemAdapter<T> provider) {
        String name = provider.getName();
        if (CoreConfig.ITEMS_DISABLED_PROVIDERS.get().contains(name)) {
            return false;
        }

        ItemBridge.register(provider);
        this.plugin.info("Registered item provider: '" + name + "'.");
        return true;
    }

    private void updateProfiles() {
        CachedProfile.updateCandidates(CoreConfig.PROFILE_UPDATE_AMOUNT.get());
    }

    private void tickMenusAndDialogs() {
        this.tickDialogs();
        this.tickMenus();
    }

    @Deprecated
    private void tickDialogs() {
        Dialog.checkTimeOut();
        DialogManager.tickDialogs();
    }

    private void tickMenus() {
        new HashSet<>(AbstractMenu.PLAYER_MENUS.values()).forEach(menu -> {
            if (menu.getOptions().isReadyToRefresh()) {
                menu.flush();
                menu.getOptions().setLastAutoRefresh(System.currentTimeMillis());
            }
        });
        MenuRegistry.getViewers().stream().map(MenuViewer::getMenu).distinct().forEach(Menu::tick);
    }
}
