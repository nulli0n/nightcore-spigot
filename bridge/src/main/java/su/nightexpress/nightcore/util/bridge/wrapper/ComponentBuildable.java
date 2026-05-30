package su.nightexpress.nightcore.util.bridge.wrapper;

import org.jspecify.annotations.NonNull;

@Deprecated
public interface ComponentBuildable {

    @NonNull
    NightComponent toComponent();
}
