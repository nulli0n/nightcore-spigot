package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface Decorator {

    void decorate(@NotNull NightComponent component);
}
