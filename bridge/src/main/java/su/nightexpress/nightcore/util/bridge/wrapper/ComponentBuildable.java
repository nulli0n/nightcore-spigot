package su.nightexpress.nightcore.util.bridge.wrapper;

import org.jetbrains.annotations.NotNull;

@Deprecated
public interface ComponentBuildable {

    @NotNull NightComponent toComponent();
}
