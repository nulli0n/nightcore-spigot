package su.nightexpress.nightcore.ui.dialog;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.manager.AbstractListener;

@Deprecated
public class DialogListener extends AbstractListener<NightCore> {

    public DialogListener(@NonNull NightCore plugin) {
        super(plugin);
    }
}
