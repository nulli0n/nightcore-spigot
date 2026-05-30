package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedBooleanDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedNumberRangeDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedTextDialogInput;

public interface DialogInputAdapter<I> {

    @NonNull
    I adaptInput(@NonNull WrappedDialogInput input);

    @NonNull
    I adaptInput(@NonNull WrappedTextDialogInput input);

    @NonNull
    I adaptInput(@NonNull WrappedSingleOptionDialogInput input);

    @NonNull
    I adaptInput(@NonNull WrappedBooleanDialogInput input);

    @NonNull
    I adaptInput(@NonNull WrappedNumberRangeDialogInput input);
}
