package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedBooleanDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedNumberRangeDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedTextDialogInput;

public interface DialogInputAdapter<I> {

    @NotNull I adaptInput(@NotNull WrappedDialogInput input);

    @NotNull I adaptInput(@NotNull WrappedTextDialogInput input);

    @NotNull I adaptInput(@NotNull WrappedSingleOptionDialogInput input);

    @NotNull I adaptInput(@NotNull WrappedBooleanDialogInput input);

    @NotNull I adaptInput(@NotNull WrappedNumberRangeDialogInput input);
}
