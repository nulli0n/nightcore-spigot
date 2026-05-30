package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.event.EventAdapter;
import su.nightexpress.nightcore.util.bridge.Software;

public class EventUtils {

    @NonNull
    public static EventAdapter getAdapter() {
        return Software.get().eventAdapter();
    }
}
