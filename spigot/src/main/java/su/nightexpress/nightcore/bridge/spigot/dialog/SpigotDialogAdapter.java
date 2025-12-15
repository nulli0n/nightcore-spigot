package su.nightexpress.nightcore.bridge.spigot.dialog;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.dialog.*;
import net.md_5.bungee.api.dialog.action.*;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;
import net.md_5.bungee.api.dialog.input.*;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.DialogKeys;
import su.nightexpress.nightcore.bridge.dialog.adapter.*;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCommandTemplateAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogCustomAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogStaticAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogBase;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedItemDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedBooleanDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedNumberRangeDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedTextDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.type.*;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.List;

public class SpigotDialogAdapter implements
    DialogAdapter<Dialog>,
    DialogActionAdapter<Action>,
    DialogBaseAdapter<DialogBase>,
    DialogBodyAdapter<DialogBody>,
    DialogButtonAdapter<ActionButton>,
    DialogInputAdapter<DialogInput>,
    DialogTypeAdapter<Dialog> {

    private final SpigotBridge bridge;

    private DialogBase tempBase; // Spigot don't have 'DialogType', what a shame...

    public SpigotDialogAdapter(@NotNull SpigotBridge bridge) {
        this.bridge = bridge;
    }

    @NotNull
    private BaseComponent adaptComponent(@NotNull String component) {
        return this.adaptComponent(NightMessage.parse(component));
    }

    @NotNull
    private BaseComponent adaptComponent(@NotNull NightComponent component) {
        return this.bridge.getTextComponentAdapter().adaptComponent(component);
    }

    @Override
    @NotNull
    public Dialog adaptDialog(@NotNull WrappedDialog wrappedDialog) {
        WrappedDialogBase wrappedBase = wrappedDialog.base();
        WrappedDialogType wrappedType = wrappedDialog.type();

        this.tempBase = this.adaptBase(wrappedBase);
        Dialog type = this.adaptType(wrappedType);
        this.tempBase = null;

        return type;
    }

    @Override
    @NotNull
    public Action adaptAction(@NotNull WrappedDialogAction action) {
        return action.adapt(this);
    }

    @Override
    @NotNull
    public StaticAction adaptAction(@NotNull WrappedDialogStaticAction action) {
        ClickEvent clickEvent = this.bridge.getTextComponentAdapter().adaptClickEvent(action.clickEvent());
        return new StaticAction(clickEvent);
    }

    @Override
    @NotNull
    public CustomClickAction adaptAction(@NotNull WrappedDialogCustomAction action) {
        CustomClickAction handle = new CustomClickAction(NightKey.key(DialogKeys.NAMESPACE, action.id()).asString());
        handle.additions(action.nbt() == null ? null : action.nbt().payload());
        return handle;
    }

    @Override
    @NotNull
    public RunCommandAction adaptAction(@NotNull WrappedDialogCommandTemplateAction action) {
        return new RunCommandAction(action.template());
    }



    @NotNull
    private DialogBase.AfterAction adaptAfterAction(@NotNull WrappedDialogAfterAction action) {
        return switch (action) {
            case NONE -> DialogBase.AfterAction.NONE;
            case CLOSE -> DialogBase.AfterAction.CLOSE;
            case WAIT_FOR_RESPONSE -> DialogBase.AfterAction.WAIT_FOR_RESPONSE;
        };
    }

    @Override
    @NotNull
    public DialogBase adaptBase(@NotNull WrappedDialogBase base) {
        BaseComponent title = this.adaptComponent(base.title());
        BaseComponent externalTitle = base.externalTitle() == null ? null : this.adaptComponent(base.externalTitle());
        boolean canCloseWithEscape = base.canCloseWithEscape();
        boolean pause = base.pause();
        DialogBase.AfterAction afterAction = this.adaptAfterAction(base.afterAction());
        List<DialogBody> body = Lists.modify(base.body(), this::adaptBody);
        List<DialogInput> inputs = Lists.modify(base.inputs(), this::adaptInput);

        return new DialogBase(title, externalTitle, inputs, body, canCloseWithEscape, pause, afterAction);
    }

    @Override
    @NotNull
    public DialogBody adaptBody(@NotNull WrappedDialogBody body) {
        return body.adapt(this);
    }

    @Override
    @NotNull
    public DialogBody adaptBody(@NotNull WrappedItemDialogBody body) {
        return new PlainMessageBody(this.adaptComponent(body.description() == null ? "" : body.description().contents()), body.width());
    }

    @Override
    @NotNull
    public PlainMessageBody adaptBody(@NotNull WrappedPlainMessageDialogBody body) {
        BaseComponent contents = this.adaptComponent(body.contents());
        int width = body.width();

        return new PlainMessageBody(contents, width);
    }

    @Override
    @NotNull
    public ActionButton adaptButton(@NotNull WrappedActionButton wrappedButton) {
        WrappedDialogAction wrappedAction = wrappedButton.action();
        if (wrappedAction == null) wrappedAction = new WrappedDialogCustomAction("empty", NightNbtHolder.fromJson(new JsonObject())); // Spigot does not allow null actions.

        BaseComponent label = this.adaptComponent(wrappedButton.label());
        BaseComponent tooltip = wrappedButton.tooltip() == null ? null : this.adaptComponent(wrappedButton.tooltip());
        Action action = this.adaptAction(wrappedAction);

        int width = wrappedButton.width();

        return new ActionButton(label, tooltip, width, action);
    }


    @Override
    @NotNull
    public DialogInput adaptInput(@NotNull WrappedDialogInput input) {
        return input.adapt(this);
    }

    @Override
    @NotNull
    public TextInput adaptInput(@NotNull WrappedTextDialogInput input) {
        String key = input.key();
        int width = input.width();
        BaseComponent label = this.adaptComponent(input.label());
        boolean labelVisible = input.labelVisible();
        String initial = input.initial();
        int maxLength = input.maxLength();
        WrappedMultilineOptions wrappedMultiline = input.multiline();

        TextInput.Multiline multilineOptions = wrappedMultiline == null ? null : new TextInput.Multiline(wrappedMultiline.maxLines(), wrappedMultiline.height());

        return new TextInput(key, width, label, labelVisible, initial, maxLength, multilineOptions);
    }

    @NotNull
    private InputOption adaptEntry(@NotNull WrappedSingleOptionEntry wrappedEntry) {
        String id = wrappedEntry.id();
        BaseComponent display = this.adaptComponent(wrappedEntry.display());
        boolean initial = wrappedEntry.initial();

        return new InputOption(id, display, initial);
    }

    @Override
    @NotNull
    public SingleOptionInput adaptInput(@NotNull WrappedSingleOptionDialogInput input) {
        String key = input.key();
        int width = input.width();
        List<InputOption> entries = Lists.modify(input.entries(), this::adaptEntry);
        BaseComponent label = this.adaptComponent(input.label());
        boolean labelVisible = input.labelVisible();

        return new SingleOptionInput(key, width, label, labelVisible, entries);
    }

    @Override
    @NotNull
    public BooleanInput adaptInput(@NotNull WrappedBooleanDialogInput input) {
        String key = input.key();
        BaseComponent label = this.adaptComponent(input.label());
        boolean initial = input.initial();
        String onTrue = input.onTrue();
        String onFalse = input.onFalse();

        return new BooleanInput(key, label, initial, onTrue, onFalse);
    }

    @Override
    @NotNull
    public NumberRangeInput adaptInput(@NotNull WrappedNumberRangeDialogInput input) {
        String key = input.key();
        int width = input.width();
        BaseComponent label = this.adaptComponent(input.label());
        String labelFormat = input.labelFormat();
        float start = input.start();
        float end = input.end();
        Float initial = input.initial();
        Float step = input.step();

        return new NumberRangeInput(key, width, label, labelFormat, start, end, initial, step);
    }

    @Override
    @NotNull
    public Dialog adaptType(@NotNull WrappedDialogType type) {
        return type.adapt(this);
    }

    @Override
    @NotNull
    public ConfirmationDialog adaptType(@NotNull WrappedConfirmationType type) {
        WrappedActionButton wrappedYes = type.yesButton();
        WrappedActionButton wrappedNo = type.noButton();

        return new ConfirmationDialog(this.tempBase, this.adaptButton(wrappedYes), this.adaptButton(wrappedNo));
    }

    @Override
    @NotNull
    public DialogListDialog adaptType(@NotNull WrappedDialogListType type) {
        List<WrappedDialog> wrappedDialogs = type.dialogs();
        WrappedActionButton exitAction = type.exitAction();
        int buttonWidth = type.buttonWidth();
        int columns = type.columns();

        List<Dialog> dialogs = Lists.modify(wrappedDialogs, this::adaptDialog);
        ActionButton exit = exitAction == null ? null : this.adaptButton(exitAction);

        return new DialogListDialog(this.tempBase, dialogs, exit, columns, buttonWidth);
    }

    @Override
    @NotNull
    public MultiActionDialog adaptType(@NotNull WrappedMultiActionType type) {
        List<WrappedActionButton> wrappedActions = type.actions();
        WrappedActionButton wrappedExit = type.exitAction();

        List<ActionButton> actions = Lists.modify(wrappedActions, this::adaptButton);
        ActionButton exitAction = wrappedExit == null ? null : this.adaptButton(wrappedExit);
        int columns = type.columns();

        return new MultiActionDialog(this.tempBase, actions, columns, exitAction);
    }

    @Override
    @NotNull
    public NoticeDialog adaptType(@NotNull WrappedNoticeType type) {
        WrappedActionButton wrappedAction = type.action();
        ActionButton action = this.adaptButton(wrappedAction);

        return new NoticeDialog(this.tempBase, action);
    }

    @Override
    @NotNull
    public ServerLinksDialog adaptType(@NotNull WrappedServerLinksType type) {
        WrappedActionButton wrappedExit = type.exitAction();

        ActionButton exitAction = wrappedExit == null ? null : this.adaptButton(wrappedExit);
        int columns = type.columns();
        int buttonWidth = type.buttonWidth();

        return new ServerLinksDialog(this.tempBase, exitAction, columns, buttonWidth);
    }
}
