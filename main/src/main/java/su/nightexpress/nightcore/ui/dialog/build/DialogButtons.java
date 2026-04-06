package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;

public class DialogButtons {

    @NonNull
    public static WrappedActionButton ok() {
        return action(CoreLang.DIALOG_BUTTON_OK).action(DialogActions.customClick(DialogActions.OK)).build();
    }

    @NonNull
    public static WrappedActionButton cancel() {
        return action(CoreLang.DIALOG_BUTTON_CANCEL).action(DialogActions.customClick(DialogActions.CANCEL)).build();
    }

    @NonNull
    public static WrappedActionButton apply() {
        return action(CoreLang.DIALOG_BUTTON_APPLY).action(DialogActions.customClick(DialogActions.APPLY)).build();
    }

    @NonNull
    public static WrappedActionButton reset() {
        return action(CoreLang.DIALOG_BUTTON_RESET).action(DialogActions.customClick(DialogActions.RESET)).build();
    }

    @NonNull
    public static WrappedActionButton back() {
        return action(CoreLang.DIALOG_BUTTON_BACK).action(DialogActions.customClick(DialogActions.BACK)).build();
    }

    public static WrappedActionButton.@NonNull Builder action(@NonNull ButtonLocale locale) {
        return action(locale.value().label(), locale.value().tooltip());
    }

    public static WrappedActionButton.@NonNull Builder action(@NonNull String label) {
        return action(label, null);
    }

    public static WrappedActionButton.@NonNull Builder action(@NonNull String label, @Nullable String tooltip) {
        return new WrappedActionButton.Builder(label, tooltip);
    }
}
