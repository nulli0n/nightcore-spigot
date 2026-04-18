package su.nightexpress.nightcore.ui.dialog;

import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.dialog.DialogKeys;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickHandler;
import su.nightexpress.nightcore.bridge.dialog.response.DialogClickResult;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.bridge.Software;

public class DialogWatcher extends AbstractManager<NightCore> implements DialogClickHandler {

    private Listener listener;

    public DialogWatcher(@NonNull NightCore plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.listener = Software.get().createDialogListener(this);
        this.plugin.getPluginManager().registerEvents(this.listener, this.plugin);
    }

    @Override
    protected void onShutdown() {
        Dialogs.clearDialogs();

        if (this.listener != null) {
            HandlerList.unregisterAll(this.listener);
            this.listener = null;
        }
    }

    @Override
    public void handleClick(@NonNull DialogClickResult result) {
        NamespacedKey identifier = result.getIdentifier();
        if (!DialogKeys.isRightNamespace(identifier)) {
            //this.plugin.debug("Foreign dialog identifier: '" + identifier + "'.");
            return;
        }

        Dialogs.handleClick(result);
    }
}
