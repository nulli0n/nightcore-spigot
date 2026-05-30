package su.nightexpress.nightcore.bridge.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NonNull;

public class PaperUtils {

    @NonNull
    public static String serializeComponent(@NonNull Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
