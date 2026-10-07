package su.nightexpress.nightcore.core;

import java.util.HashSet;
import java.util.function.Supplier;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.listener.CoreListener;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogListener;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.ItemBridgeProvider;
import su.nightexpress.nightcore.integration.item.ItemProvider;
import su.nightexpress.nightcore.integration.item.bukkit.BukkitItemProvider;
import su.nightexpress.nightcore.integration.item.craftengine.CraftEngineItemProvider;
import su.nightexpress.nightcore.integration.item.excellentcrates.ExcellentCratesItemProvider;
import su.nightexpress.nightcore.integration.item.executableitems.ExecutableItemsItemProvider;
import su.nightexpress.nightcore.integration.item.itemsadder.ItemsAdderItemProvider;
import su.nightexpress.nightcore.integration.item.mmoitems.MMOItemsItemProvider;
import su.nightexpress.nightcore.integration.item.nexo.NexoItemProvider;
import su.nightexpress.nightcore.integration.item.oraxen.OraxenItemProvider;
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

@NullMarked
public class CoreManager extends AbstractManager<NightCore> {

    public CoreManager(NightCore core) {
        super(core);
    }

    @Override
    protected void onLoad() {
        ItemBridge.setProvider(new ItemBridgeProvider());

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

        ItemBridge.get().unregisterAllProviders();
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

    private void registerPermissionProvider(PermissionProvider provider) {
        PermissionBridge.register(provider);
        this.plugin.info("Registered permissions provider: " + provider.getName());
    }

    private void loadItemIntegrations() {
        this.registerItemProvider(BukkitItemProvider.INSTANCE);
        this.registerExternalItemProvider("ExcellentCrates", ExcellentCratesItemProvider::new);
        this.registerExternalItemProvider("ExecutableItems", ExecutableItemsItemProvider::new);
        this.registerExternalItemProvider("ItemsAdder", ItemsAdderItemProvider::new);
        this.registerExternalItemProvider("MMOItems", MMOItemsItemProvider::new);
        this.registerExternalItemProvider("nexo", NexoItemProvider::new);
        this.registerExternalItemProvider("Oraxen", OraxenItemProvider::new);
        this.registerExternalItemProvider("CraftEngine", CraftEngineItemProvider::new);
    }

    private <T> boolean registerExternalItemProvider(String pluginName,
                                                     Supplier<ItemProvider> supplier) {
        if (!Plugins.isInstalled(pluginName)) return false;

        return this.registerItemProvider(supplier.get());
    }

    private <T> boolean registerItemProvider(ItemProvider provider) {
        String name = provider.getId();
        if (CoreConfig.ITEMS_DISABLED_PROVIDERS.get().contains(name)) {
            return false;
        }

        ItemBridge.get().registerProvider(provider);
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
