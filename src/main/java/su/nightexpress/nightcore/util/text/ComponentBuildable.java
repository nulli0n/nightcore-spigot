package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface ComponentBuildable {

    @NotNull NightComponent toComponent();
}
