package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface Entry {

    @NonNull
    NightComponent toComponent();
}
