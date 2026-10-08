package su.nightexpress.nightcore.core;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.item.AdaptedDataItem;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.core.listener.CoreListener;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogListener;
import su.nightexpress.nightcore.integration.item.ItemBridge;
import su.nightexpress.nightcore.integration.item.ItemBridgeProvider;
import su.nightexpress.nightcore.integration.item.ItemProvider;
import su.nightexpress.nightcore.integration.item.bukkit.BukkitItem;
import su.nightexpress.nightcore.integration.item.bukkit.BukkitItemProvider;
import su.nightexpress.nightcore.integration.item.codec.BukkitItemCodec;
import su.nightexpress.nightcore.integration.item.codec.DummyItemCodec;
import su.nightexpress.nightcore.integration.item.codec.IdentifiableItemCodec;
import su.nightexpress.nightcore.integration.item.craftengine.CraftEngineItem;
import su.nightexpress.nightcore.integration.item.craftengine.CraftEngineItemProvider;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.dummy.DummyItem;
import su.nightexpress.nightcore.integration.item.excellentcrates.ExcellentCratesItem;
import su.nightexpress.nightcore.integration.item.excellentcrates.ExcellentCratesItemProvider;
import su.nightexpress.nightcore.integration.item.excellentcrates.codec.ExcellentCratesItemCodec;
import su.nightexpress.nightcore.integration.item.executableitems.ExecutableItemsItem;
import su.nightexpress.nightcore.integration.item.executableitems.ExecutableItemsItemProvider;
import su.nightexpress.nightcore.integration.item.itemsadder.ItemsAdderItem;
import su.nightexpress.nightcore.integration.item.itemsadder.ItemsAdderItemProvider;
import su.nightexpress.nightcore.integration.item.mmoitems.MMOItemsItem;
import su.nightexpress.nightcore.integration.item.mmoitems.MMOItemsItemProvider;
import su.nightexpress.nightcore.integration.item.nexo.NexoItem;
import su.nightexpress.nightcore.integration.item.nexo.NexoItemProvider;
import su.nightexpress.nightcore.integration.item.oraxen.OraxenItem;
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
        // Register dummy item codec, so config.set() can handle dummy items correctly
        ConfigCodecs.register(DummyItem.class, DummyItemCodec.INSTANCE);
        ConfigCodecs.register(BukkitItem.class, BukkitItemCodec.INSTANCE);

        this.registerItemProvider(BukkitItemProvider.INSTANCE, BukkitItemCodec.INSTANCE);

        this.registerIdentifiable("ExecutableItems",
            ExecutableItemsItem.class,
            ExecutableItemsItemProvider::new,
            ExecutableItemsItem::new
        );

        this.registerIdentifiable("ItemsAdder", ItemsAdderItem.class, ItemsAdderItemProvider::new, ItemsAdderItem::new);
        this.registerIdentifiable("MMOItems", MMOItemsItem.class, MMOItemsItemProvider::new, MMOItemsItem::new);
        this.registerIdentifiable("nexo", NexoItem.class, NexoItemProvider::new, NexoItem::new);
        this.registerIdentifiable("Oraxen", OraxenItem.class, OraxenItemProvider::new, OraxenItem::new);
        this.registerIdentifiable("CraftEngine", CraftEngineItem.class, CraftEngineItemProvider::new,
            CraftEngineItem::new);

        Plugins.ifInstalled("ExcellentCrates", () -> {
            ConfigCodecs.register(ExcellentCratesItem.class, ExcellentCratesItemCodec.INSTANCE);
            this.registerItemProvider(new ExcellentCratesItemProvider(), ExcellentCratesItemCodec.INSTANCE);
        });
    }

    private <T, I extends AdaptedItem & AdaptedDataItem<ItemIdData>> boolean registerIdentifiable(String pluginName,
                                                                                                  Class<I> itemClass,
                                                                                                  Supplier<ItemProvider> supplier,
                                                                                                  Function<ItemIdData, I> construct) {
        if (!Plugins.isInstalled(pluginName)) return false;

        ConfigCodec<I> codec = IdentifiableItemCodec.create(construct);
        ConfigCodecs.register(itemClass, codec);

        return this.registerItemProvider(supplier.get(), codec);
    }

    private <I extends AdaptedItem> boolean registerItemProvider(ItemProvider provider, ConfigCodec<I> codec) {
        String name = provider.getId();
        if (CoreConfig.ITEMS_DISABLED_PROVIDERS.get().contains(name)) {
            return false;
        }

        ItemBridge.get().register(provider, codec);
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
