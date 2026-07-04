package su.nightexpress.nightcore.ui.dialog.build;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;

@NullMarked
public final class DialogButtons {

    private DialogButtons() {
    }

    public static WrappedActionButton ok() {
        return action(CoreLang.DIALOG_BUTTON_OK).action(DialogActions.customClick(DialogActions.OK)).build();
    }

    public static WrappedActionButton cancel() {
        return action(CoreLang.DIALOG_BUTTON_CANCEL).action(DialogActions.customClick(DialogActions.CANCEL)).build();
    }

    public static WrappedActionButton apply() {
        return action(CoreLang.DIALOG_BUTTON_APPLY).action(DialogActions.customClick(DialogActions.APPLY)).build();
    }

    public static WrappedActionButton confirm() {
        return action(CoreLang.DIALOG_BUTTON_CONFIRM).action(DialogActions.customClick(DialogActions.CONFIRM)).build();
    }

    public static WrappedActionButton reset() {
        return action(CoreLang.DIALOG_BUTTON_RESET).action(DialogActions.customClick(DialogActions.RESET)).build();
    }

    public static WrappedActionButton back() {
        return action(CoreLang.DIALOG_BUTTON_BACK).action(DialogActions.customClick(DialogActions.BACK)).build();
    }

    public static WrappedActionButton.Builder action(ButtonLocale locale) {
        return action(locale.value().label(), locale.value().tooltip());
    }

    public static WrappedActionButton.Builder action(String label) {
        return action(label, null);
    }

    public static WrappedActionButton.Builder action(String label, @Nullable String tooltip) {
        return new WrappedActionButton.Builder(label, tooltip);
    }
}
