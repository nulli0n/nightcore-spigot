package su.nightexpress.nightcore.ui.dialog.build;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogBase;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.List;

public class DialogBases {

    @NotNull
    public static WrappedDialogBase create(@NotNull String title,
                                           @Nullable String externalTitle,
                                           boolean canCloseWithEscape,
                                           boolean pause,
                                           @NotNull WrappedDialogAfterAction afterAction,
                                           @NotNull List<WrappedDialogBody> body,
                                           @NotNull List<WrappedDialogInput> inputs) {
        return create(NightMessage.parse(title), externalTitle == null ? null : NightMessage.parse(externalTitle), canCloseWithEscape, pause, afterAction, body, inputs);
    }

    @NotNull
    public static WrappedDialogBase create(@NotNull NightComponent title,
                                           @Nullable NightComponent externalTitle,
                                           boolean canCloseWithEscape,
                                           boolean pause,
                                           @NotNull WrappedDialogAfterAction afterAction,
                                           @NotNull List<WrappedDialogBody> body,
                                           @NotNull List<WrappedDialogInput> inputs) {
        return builder(title).externalTitle(externalTitle).canCloseWithEscape(canCloseWithEscape).pause(pause).afterAction(afterAction).body(body).inputs(inputs).build();
    }

    @NotNull
    public static WrappedDialogBase.Builder builder(@NotNull String title) {
        return builder(NightMessage.parse(title));
    }

    @NotNull
    public static WrappedDialogBase.Builder builder(@NotNull NightComponent title) {
        return new WrappedDialogBase.Builder(title);
    }
}
