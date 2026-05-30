package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@Deprecated
public interface Decorator {

    @NonNull
    NightComponent decorate(@NonNull NightComponent component);
}
