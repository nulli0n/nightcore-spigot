package su.nightexpress.nightcore.bridge.paper.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.ItemDialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import io.papermc.paper.registry.data.dialog.input.*;
import io.papermc.paper.registry.data.dialog.type.*;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.List;

public class PaperDialogAdapter implements
    DialogAdapter<Dialog>,
    DialogActionAdapter<DialogAction>,
    DialogBaseAdapter<DialogBase>,
    DialogBodyAdapter<DialogBody>,
    DialogButtonAdapter<ActionButton>,
    DialogInputAdapter<DialogInput>,
    DialogTypeAdapter<DialogType> {

    private final PaperBridge bridge;

    public PaperDialogAdapter(@NotNull PaperBridge bridge) {
        this.bridge = bridge;
    }

    @NotNull
    private Component adaptComponent(@NotNull String component) {
        return this.adaptComponent(NightMessage.parse(component));
    }

    @NotNull
    private Component adaptComponent(@NotNull NightComponent component) {
        return this.bridge.getTextComponentAdapter().adaptComponent(component);
    }

    @Override
    @NotNull
    public Dialog adaptDialog(@NotNull WrappedDialog wrappedDialog) {
        WrappedDialogBase wrappedBase = wrappedDialog.base();
        WrappedDialogType wrappedType = wrappedDialog.type();

        DialogBase base = this.adaptBase(wrappedBase);
        DialogType type = this.adaptType(wrappedType);

        return io.papermc.paper.dialog.Dialog.create(factory -> factory.empty().type(type).base(base));
    }

    @Override
    @NotNull
    public DialogAction adaptAction(@NotNull WrappedDialogAction action) {
        return action.adapt(this);
    }

    @Override
    @NotNull
    public DialogAction.StaticAction adaptAction(@NotNull WrappedDialogStaticAction action) {
        ClickEvent clickEvent = this.bridge.getTextComponentAdapter().adaptClickEvent(action.clickEvent());
        return DialogAction.staticAction(clickEvent);
    }

    @Override
    @NotNull
    public DialogAction.CustomClickAction adaptAction(@NotNull WrappedDialogCustomAction action) {
        NightNbtHolder nbtHolder = action.nbt();
        BinaryTagHolder additions = nbtHolder == null ? null : BinaryTagHolder.binaryTagHolder(nbtHolder.asString());

        return DialogAction.customClick(Key.key(DialogKeys.NAMESPACE, action.id()), additions);
    }

    @Override
    @NotNull
    public DialogAction.CommandTemplateAction adaptAction(@NotNull WrappedDialogCommandTemplateAction action) {
        return DialogAction.commandTemplate(action.template());
    }



    @NotNull
    private DialogBase.DialogAfterAction adaptAfterAction(@NotNull WrappedDialogAfterAction action) {
        return switch (action) {
            case NONE -> DialogBase.DialogAfterAction.NONE;
            case CLOSE -> DialogBase.DialogAfterAction.CLOSE;
            case WAIT_FOR_RESPONSE -> DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE;
        };
    }

    @Override
    @NotNull
    public DialogBase adaptBase(@NotNull WrappedDialogBase base) {
        Component title = this.adaptComponent(base.title());
        Component externalTitle = base.externalTitle() == null ? null : this.adaptComponent(base.externalTitle());
        boolean canCloseWithEscape = base.canCloseWithEscape();
        boolean pause = base.pause();
        DialogBase.DialogAfterAction afterAction = this.adaptAfterAction(base.afterAction());
        List<DialogBody> body = Lists.modify(base.body(), this::adaptBody);
        List<DialogInput> inputs = Lists.modify(base.inputs(), this::adaptInput);

        return DialogBase.create(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs);
    }

    @Override
    @NotNull
    public DialogBody adaptBody(@NotNull WrappedDialogBody body) {
        return body.adapt(this);
    }

    @Override
    @NotNull
    public ItemDialogBody adaptBody(@NotNull WrappedItemDialogBody body) {
        ItemStack item = body.item();
        WrappedPlainMessageDialogBody description = body.description();
        boolean showDecorations = body.showDecorations();
        boolean showTooltip = body.showTooltip();
        int width = body.width();
        int height = body.height();

        PlainMessageDialogBody desc = description == null ? null : this.adaptBody(description);

        return DialogBody.item(item, desc, showDecorations, showTooltip, width, height);
    }

    @Override
    @NotNull
    public PlainMessageDialogBody adaptBody(@NotNull WrappedPlainMessageDialogBody body) {
        Component contents = this.adaptComponent(body.contents());
        int width = body.width();

        return DialogBody.plainMessage(contents, width);
    }

    @Override
    @NotNull
    public ActionButton adaptButton(@NotNull WrappedActionButton wrappedButton) {
        WrappedDialogAction wrappedAction = wrappedButton.action();

        Component label = this.adaptComponent(wrappedButton.label());
        Component tooltip = wrappedButton.tooltip() == null ? null : this.adaptComponent(wrappedButton.tooltip());
        DialogAction action = wrappedAction == null ? null : this.adaptAction(wrappedAction);

        int width = wrappedButton.width();

        return ActionButton.create(label, tooltip, width, action);
    }


    @Override
    @NotNull
    public DialogInput adaptInput(@NotNull WrappedDialogInput input) {
        return input.adapt(this);
    }

    @Override
    @NotNull
    public TextDialogInput adaptInput(@NotNull WrappedTextDialogInput input) {
        String key = input.key();
        int width = input.width();
        Component label = this.adaptComponent(input.label());
        boolean labelVisible = input.labelVisible();
        String initial = input.initial();
        int maxLength = input.maxLength();
        WrappedMultilineOptions wrappedMultiline = input.multiline();

        TextDialogInput.MultilineOptions multilineOptions = wrappedMultiline == null ? null : TextDialogInput.MultilineOptions.create(wrappedMultiline.maxLines(), wrappedMultiline.height());

        return DialogInput.text(key, width, label, labelVisible, initial, maxLength, multilineOptions);
    }

    @NotNull
    private SingleOptionDialogInput.OptionEntry adaptEntry(@NotNull WrappedSingleOptionEntry wrappedEntry) {
        String id = wrappedEntry.id();
        Component display = this.adaptComponent(wrappedEntry.display());
        boolean initial = wrappedEntry.initial();

        return SingleOptionDialogInput.OptionEntry.create(id, display, initial);
    }

    @Override
    @NotNull
    public SingleOptionDialogInput adaptInput(@NotNull WrappedSingleOptionDialogInput input) {
        String key = input.key();
        int width = input.width();
        List<SingleOptionDialogInput.OptionEntry> entries = Lists.modify(input.entries(), this::adaptEntry);
        Component label = this.adaptComponent(input.label());
        boolean labelVisible = input.labelVisible();

        return DialogInput.singleOption(key, width, entries, label, labelVisible);
    }

    @Override
    @NotNull
    public BooleanDialogInput adaptInput(@NotNull WrappedBooleanDialogInput input) {
        String key = input.key();
        Component label = this.adaptComponent(input.label());
        boolean initial = input.initial();
        String onTrue = input.onTrue();
        String onFalse = input.onFalse();

        return DialogInput.bool(key, label, initial, onTrue, onFalse);
    }

    @Override
    @NotNull
    public NumberRangeDialogInput adaptInput(@NotNull WrappedNumberRangeDialogInput input) {
        String key = input.key();
        int width = input.width();
        Component label = this.adaptComponent(input.label());
        String labelFormat = input.labelFormat();
        float start = input.start();
        float end = input.end();
        Float initial = input.initial();
        Float step = input.step();

        return DialogInput.numberRange(key, width, label, labelFormat, start, end, initial, step);
    }

    @Override
    @NotNull
    public DialogType adaptType(@NotNull WrappedDialogType type) {
        return type.adapt(this);
    }

    @Override
    @NotNull
    public ConfirmationType adaptType(@NotNull WrappedConfirmationType type) {
        WrappedActionButton wrappedYes = type.yesButton();
        WrappedActionButton wrappedNo = type.noButton();

        return DialogType.confirmation(this.adaptButton(wrappedYes), this.adaptButton(wrappedNo));
    }

    @Override
    @NotNull
    public DialogListType adaptType(@NotNull WrappedDialogListType type) {
        List<WrappedDialog> wrappedDialogs = type.dialogs();
        WrappedActionButton exitAction = type.exitAction();
        int buttonWidth = type.buttonWidth();
        int columns = type.columns();

        List<Dialog> dialogs = Lists.modify(wrappedDialogs, this::adaptDialog);
        ActionButton exit = exitAction == null ? null : this.adaptButton(exitAction);

        RegistrySet<Dialog> dialogRegistry = RegistrySet.valueSet(RegistryKey.DIALOG, dialogs);
        return DialogType.dialogList(dialogRegistry, exit, columns, buttonWidth);
    }

    @Override
    @NotNull
    public MultiActionType adaptType(@NotNull WrappedMultiActionType type) {
        List<WrappedActionButton> wrappedActions = type.actions();
        WrappedActionButton wrappedExit = type.exitAction();

        List<ActionButton> actions = Lists.modify(wrappedActions, this::adaptButton);
        ActionButton exitAction = wrappedExit == null ? null : this.adaptButton(wrappedExit);
        int columns = type.columns();

        return DialogType.multiAction(actions, exitAction, columns);
    }

    @Override
    @NotNull
    public NoticeType adaptType(@NotNull WrappedNoticeType type) {
        WrappedActionButton wrappedAction = type.action();
        ActionButton action = this.adaptButton(wrappedAction);

        return DialogType.notice(action);
    }

    @Override
    @NotNull
    public ServerLinksType adaptType(@NotNull WrappedServerLinksType type) {
        WrappedActionButton wrappedExit = type.exitAction();

        ActionButton exitAction = wrappedExit == null ? null : this.adaptButton(wrappedExit);
        int columns = type.columns();
        int buttonWidth = type.buttonWidth();

        return DialogType.serverLinks(exitAction, columns, buttonWidth);
    }
}
