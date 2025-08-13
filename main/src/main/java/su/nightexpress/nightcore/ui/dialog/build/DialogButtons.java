package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

public class DialogButtons {

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull ButtonLocale locale) {
        return action(locale.value().label(), locale.value().tooltip());
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label) {
        return action(label, null);
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label, @Nullable String tooltip) {
        return action(NightMessage.parse(label), tooltip == null ? null : NightMessage.parse(tooltip));
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull NightComponent label) {
        return action(label, null);
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull NightComponent label, @Nullable NightComponent tooltip) {
        return new WrappedActionButton.Builder(label, tooltip);
    }
}
