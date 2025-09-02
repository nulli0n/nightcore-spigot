package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;

public class DialogButtons {

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull ButtonLocale locale) {
        return action(locale.value().label(), locale.value().tooltip());
    }

    /*@NotNull
    public static WrappedActionButton.Builder action(@NotNull String label) {
        return action(label, null);
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label, @Nullable String tooltip) {
        return action(NightMessage.parse(label), tooltip == null ? null : NightMessage.parse(tooltip));
    }*/

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label) {
        return action(label, null);
    }

    @NotNull
    public static WrappedActionButton.Builder action(@NotNull String label, @Nullable String tooltip) {
        return new WrappedActionButton.Builder(label, tooltip);
    }
}
