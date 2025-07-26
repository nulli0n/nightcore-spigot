package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

public class DialogButtons {

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label) {
        return action(NightMessage.parse(label));
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label, @NotNull String tooltip) {
        return action(NightMessage.parse(label), NightMessage.parse(tooltip));
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull NightComponent label) {
        return new WrappedActionButton.Builder(label);
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull NightComponent label, @Nullable NightComponent tooltip) {
        return new WrappedActionButton.Builder(label, tooltip);
    }
}
