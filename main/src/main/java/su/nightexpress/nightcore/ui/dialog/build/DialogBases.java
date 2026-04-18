package su.nightexpress.nightcore.ui.dialog.build;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogBase;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class DialogBases {

    @NonNull
    public static WrappedDialogBase create(@NonNull TextLocale title, @Nullable TextLocale externalTitle, boolean canCloseWithEscape, boolean pause, @NonNull WrappedDialogAfterAction afterAction, @NonNull List<WrappedDialogBody> body, @NonNull List<WrappedDialogInput> inputs) {
        return create(title.text(), externalTitle == null ? null : externalTitle.text(), canCloseWithEscape, pause,
            afterAction, body, inputs);
    }

    /*@NonNull
    public static WrappedDialogBase create(@NonNull String title,
                                           @Nullable String externalTitle,
                                           boolean canCloseWithEscape,
                                           boolean pause,
                                           @NonNull WrappedDialogAfterAction afterAction,
                                           @NonNull List<WrappedDialogBody> body,
                                           @NonNull List<WrappedDialogInput> inputs) {
        return create(NightMessage.parse(title), externalTitle == null ? null : NightMessage.parse(externalTitle), canCloseWithEscape, pause, afterAction, body, inputs);
    }*/

    @NonNull
    public static WrappedDialogBase create(@NonNull String title, @Nullable String externalTitle, boolean canCloseWithEscape, boolean pause, @NonNull WrappedDialogAfterAction afterAction, @NonNull List<WrappedDialogBody> body, @NonNull List<WrappedDialogInput> inputs) {
        return builder(title).externalTitle(externalTitle).canCloseWithEscape(canCloseWithEscape).pause(pause)
            .afterAction(afterAction).body(body).inputs(inputs).build();
    }

    public static WrappedDialogBase.@NonNull Builder builder(@NonNull TextLocale title) {
        return builder(title.text());
    }

    /*@NonNull
    public static WrappedDialogBase.Builder builder(@NonNull String title) {
        return builder(NightMessage.parse(title));
    }*/

    public static WrappedDialogBase.@NonNull Builder builder(@NonNull String title) {
        return new WrappedDialogBase.Builder(title);
    }
}
