package su.nightexpress.nightcore.bridge.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class PaperUtils {

    @NotNull
    public static String serializeComponent(@NotNull Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
