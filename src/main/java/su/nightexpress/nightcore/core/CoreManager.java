package su.nightexpress.nightcore.core;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.listener.CoreListener;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogListener;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.menu.MenuListener;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;

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

        this.addAsyncTask(this::tickDialogs, 1);
        this.addTask(this::tickMenus, 1);
    }

    @Override
    protected void onShutdown() {

    }

    private void tickDialogs() {
        Dialog.checkTimeOut();
    }

    private void tickMenus() {
        new HashSet<>(AbstractMenu.PLAYER_MENUS.values()).forEach(menu -> {
            if (menu.getOptions().isReadyToRefresh()) {
                menu.flush();
                menu.getOptions().setLastAutoRefresh(System.currentTimeMillis());
            }
        });
    }
}
