package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedBooleanDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedNumberRangeDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedTextDialogInput;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.List;

public class DialogInputs {

    @NotNull
    public static WrappedBooleanDialogInput.Builder bool(@NotNull String key, @NotNull String label) {
        return bool(key, NightMessage.parse(label));
    }

    @NotNull
    public static WrappedBooleanDialogInput.Builder bool(@NotNull String key, @NotNull NightComponent label) {
        return new WrappedBooleanDialogInput.Builder(key, label);
    }

    @NotNull
    public static WrappedBooleanDialogInput bool(@NotNull String key, @NotNull String label, boolean initial, @NotNull String onTrue, @NotNull String onFalse) {
        return bool(key, NightMessage.parse(label), initial, onTrue, onFalse);
    }

    @NotNull
    public static WrappedBooleanDialogInput bool(@NotNull String key, @NotNull NightComponent label, boolean initial, @NotNull String onTrue, @NotNull String onFalse) {
        return bool(key, label).initial(initial).onTrue(onTrue).onFalse(onFalse).build();
    }

    @NotNull
    public static WrappedNumberRangeDialogInput numberRange(@NotNull String key,
                                                            int width,
                                                            @NotNull String label,
                                                            @NotNull String labelFormat,
                                                            float start,
                                                            float end,
                                                            @Nullable Float initial,
                                                            @Nullable Float step) {
        return numberRange(key, width, NightMessage.parse(label), labelFormat, start, end, initial, step);
    }

    @NotNull
    public static WrappedNumberRangeDialogInput numberRange(@NotNull String key,
                                                            int width,
                                                            @NotNull NightComponent label,
                                                            @NotNull String labelFormat,
                                                            float start,
                                                            float end,
                                                            @Nullable Float initial,
                                                            @Nullable Float step) {
        return numberRange(key, label, start, end).width(width).labelFormat(labelFormat).initial(initial).step(step).build();
    }

    @NotNull
    public static WrappedNumberRangeDialogInput.Builder numberRange(@NotNull String key, @NotNull String label, float start, float end) {
        return numberRange(key, NightMessage.parse(label), start, end);
    }

    @NotNull
    public static WrappedNumberRangeDialogInput.Builder numberRange(@NotNull String key, @NotNull NightComponent label, float start, float end) {
        return new WrappedNumberRangeDialogInput.Builder(key, label, start, end);
    }

    @NotNull
    public static WrappedSingleOptionDialogInput singleOption(@NotNull String key,
                                                              int width,
                                                              @NotNull List<WrappedSingleOptionEntry> entries,
                                                              @NotNull NightComponent label,
                                                              boolean labelVisible) {
        return singleOption(key, label, entries).width(width).labelVisible(labelVisible).build();
    }

    @NotNull
    public static WrappedSingleOptionDialogInput.Builder singleOption(@NotNull String key, @NotNull String label, @NotNull List<WrappedSingleOptionEntry> entries) {
        return singleOption(key, NightMessage.parse(label), entries);
    }

    @NotNull
    public static WrappedSingleOptionDialogInput.Builder singleOption(@NotNull String key, @NotNull NightComponent label, @NotNull List<WrappedSingleOptionEntry> entries) {
        return new WrappedSingleOptionDialogInput.Builder(key, label, entries);
    }

    @NotNull
    public static WrappedTextDialogInput text(@NotNull String key,
                                              @NotNull NightComponent label,
                                              @NotNull String initial,
                                              boolean labelVisible,
                                              int width,
                                              int maxLength,
                                              @Nullable WrappedMultilineOptions multilineOptions) {
        return text(key, label).width(width).labelVisible(labelVisible).initial(initial).maxLength(maxLength).multiline(multilineOptions).build();
    }

    @NotNull
    public static WrappedTextDialogInput.Builder text(@NotNull String key, @NotNull String label) {
        return text(key, NightMessage.parse(label));
    }

    @NotNull
    public static WrappedTextDialogInput.Builder text(@NotNull String key, @NotNull NightComponent label) {
        return new WrappedTextDialogInput.Builder(key, label);
    }
}
