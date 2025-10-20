package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.util.Strings;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.nbt.NbtUtil;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.TextParser;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;
import su.nightexpress.nightcore.util.text.night.tag.TagContent;

public class HoverTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        TagContent content = ParserUtils.parseInnerContent(tagContent);

        String value = content.second();
        if (value == null) return;

        NightHoverEvent.Action<?> action = NightHoverEvent.Action.byName(content.first());
        if (action == null) return;

        NightHoverEvent<?> hoverEvent;

        if (action == NightHoverEvent.Action.SHOW_TEXT) {
            hoverEvent = NightHoverEvent.showText(TextParser.parse(value));
        }
        else if (action == NightHoverEvent.Action.SHOW_ITEM) {
            String tag = Strings.fromBase64(value);
            ItemStack itemStack = NbtUtil.tagToItemStack(tag, Version.getCurrent().getDataVersion());
            if (itemStack == null) return;

            hoverEvent = NightHoverEvent.showItem(itemStack);
        }
        else return;

        group.setStyle(style -> style.hoverEvent(hoverEvent));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
