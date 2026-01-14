package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.util.bridge.Software;

public class EventUtils {

    @NotNull
    public static EventAdapter getAdapter() {
        return Software.get().eventAdapter();
    }
}
