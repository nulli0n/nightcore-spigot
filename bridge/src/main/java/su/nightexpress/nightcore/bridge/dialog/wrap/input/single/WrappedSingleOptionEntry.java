package su.nightexpress.nightcore.bridge.dialog.wrap.input.single;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public record WrappedSingleOptionEntry(@NotNull String id, @NotNull NightComponent display, boolean initial) {

}
