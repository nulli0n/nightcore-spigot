package su.nightexpress.nightcore.util.bridge.wrapper;

import org.jetbrains.annotations.NotNull;

public interface ComponentBuildable {

    @NotNull NightComponent toComponent();
}
