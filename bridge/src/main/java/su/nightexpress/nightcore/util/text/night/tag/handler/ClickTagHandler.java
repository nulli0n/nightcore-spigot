package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Numbers;
import su.nightexpress.nightcore.util.text.event.ClickEvents;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;
import su.nightexpress.nightcore.util.text.night.tag.TagContent;

public class ClickTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        TagContent content = ParserUtils.parseInnerContent(tagContent);

        String value = content.second();
        if (value == null) return;

        NightClickEvent.Action action = Enums.get(content.first(), NightClickEvent.Action.class);
        if (action == null) return;

        NightClickEvent clickEvent = switch (action) {
            case COPY_TO_CLIPBOARD -> ClickEvents.copyToClipboard(value);
            case SUGGEST_COMMAND -> ClickEvents.suggestCommand(value);
            case RUN_COMMAND -> ClickEvents.runCommand(value);
            case CHANGE_PAGE -> ClickEvents.changePage(Numbers.getIntegerAbs(value));
            case OPEN_FILE -> ClickEvents.openFile(value);
            case OPEN_URL -> ClickEvents.openUrl(value);
            case SHOW_DIALOG -> null;
            case CUSTOM -> null; // TODO NightKey + NightNbtHolder
        };
        if (clickEvent == null) return;

        group.setStyle(style -> style.clickEvent(clickEvent));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
