package su.nightexpress.nightcore.ui.dialog.build;

import java.util.List;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedBooleanDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedNumberRangeDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedTextDialogInput;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class DialogInputs {

    public static WrappedBooleanDialogInput.@NonNull Builder bool(@NonNull String key, @NonNull TextLocale label) {
        return bool(key, label.text());
    }

    public static WrappedBooleanDialogInput.@NonNull Builder bool(@NonNull String key, @NonNull String label) {
        return new WrappedBooleanDialogInput.Builder(key, label);
    }

    @NonNull
    public static WrappedBooleanDialogInput bool(@NonNull String key, @NonNull String label, boolean initial, @NonNull String onTrue, @NonNull String onFalse) {
        return bool(key, label).initial(initial).onTrue(onTrue).onFalse(onFalse).build();
    }

    public static WrappedNumberRangeDialogInput.@NonNull Builder numberRange(@NonNull String key, @NonNull DialogElementLocale locale, float start, float end) {
        return new WrappedNumberRangeDialogInput.Builder(key, locale.contents(), start, end).width(locale.width());
    }

    public static WrappedNumberRangeDialogInput.@NonNull Builder numberRange(@NonNull String key, @NonNull TextLocale label, float start, float end) {
        return numberRange(key, label.text(), start, end);
    }

    public static WrappedNumberRangeDialogInput.@NonNull Builder numberRange(@NonNull String key, @NonNull String label, float start, float end) {
        return new WrappedNumberRangeDialogInput.Builder(key, label, start, end);
    }

    public static WrappedSingleOptionDialogInput.@NonNull Builder singleOption(@NonNull String key, @NonNull DialogElementLocale locale, @NonNull List<WrappedSingleOptionEntry> entries) {
        return new WrappedSingleOptionDialogInput.Builder(key, locale.contents(), entries).width(locale.width());
    }

    public static WrappedSingleOptionDialogInput.@NonNull Builder singleOption(@NonNull String key, @NonNull TextLocale label, @NonNull List<WrappedSingleOptionEntry> entries) {
        return singleOption(key, label.text(), entries);
    }

    public static WrappedSingleOptionDialogInput.@NonNull Builder singleOption(@NonNull String key, @NonNull String label, @NonNull List<WrappedSingleOptionEntry> entries) {
        return new WrappedSingleOptionDialogInput.Builder(key, label, entries);
    }

    public static WrappedTextDialogInput.@NonNull Builder text(@NonNull String key, @NonNull DialogElementLocale locale) {
        return new WrappedTextDialogInput.Builder(key, locale.contents()).width(locale.width());
    }

    public static WrappedTextDialogInput.@NonNull Builder text(@NonNull String key, @NonNull TextLocale label) {
        return text(key, label.text());
    }

    public static WrappedTextDialogInput.@NonNull Builder text(@NonNull String key, @NonNull String label) {
        return new WrappedTextDialogInput.Builder(key, label);
    }
}
