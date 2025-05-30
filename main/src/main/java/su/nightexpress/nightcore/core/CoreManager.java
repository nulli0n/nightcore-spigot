package su.nightexpress.nightcore.core;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.listener.CoreListener;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogListener;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.menu.MenuListener;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;
import su.nightexpress.nightcore.ui.UIListener;
import su.nightexpress.nightcore.ui.dialog.DialogManager;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuRegistry;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

import java.util.HashSet;

public class CoreManager extends AbstractManager<NightCore> {

    public CoreManager(@NotNull NightCore plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.addListener(new CoreListener(this.plugin));
        this.addListener(new DialogListener(this.plugin));
        this.addListener(new MenuListener(this.plugin));
        this.addListener(new UIListener(this.plugin));

        this.addTask(this::tickDialogs, 1);
        this.addTask(this::tickMenus, 1);
    }

    @Override
    protected void onShutdown() {
        Dialog.shutdown();
        DialogManager.shutdown();
    }

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

        //            if (menu.isReadyToRefresh()) {
        //                menu.flush();
        //                menu.setAutoRefreshIn(TimeUtil.createFutureTimestamp(menu.getAutoRefreshInterval()));
        //            }
        MenuRegistry.getViewers().stream().map(MenuViewer::getMenu).distinct().forEach(Menu::tick);
    }
}
