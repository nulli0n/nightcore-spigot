package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@Deprecated
public interface Decorator {

    @NotNull NightComponent decorate(@NotNull NightComponent component);
}
