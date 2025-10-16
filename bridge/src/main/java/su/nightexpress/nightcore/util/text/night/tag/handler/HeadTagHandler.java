package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;
import su.nightexpress.nightcore.util.text.night.entry.PlayerHeadEntry;
import su.nightexpress.nightcore.util.text.night.tag.TagContent;

import java.util.UUID;
import java.util.regex.Pattern;

public class HeadTagHandler extends ClassicTagHandler {

    private static final Pattern UUIDv4_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ABCD][0-9a-f]{3}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;
        if (!Version.withCopperAge()) return;

        TagContent content = ParserUtils.parseInnerContent(tagContent);

        String data = ParserUtils.unquoted(content.first());
        boolean hat = !content.hasBoth() || Boolean.parseBoolean(content.second());
        NightPlayerHeadObjectContents contents;

        if (UUIDv4_PATTERN.matcher(data).matches()) {
            contents = NightObjectContents.playerHead(UUID.fromString(data), hat);
        }
        else if (data.contains("/") && NightKey.parseable(data)) {
            contents = NightObjectContents.playerHead(NightKey.key(data), hat);
        }
        else {
            contents = NightObjectContents.playerHead(data, hat);
        }

        group.appendEntry(new PlayerHeadEntry(group, contents));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }

    @Override
    public boolean canBeClosed() {
        return false;
    }
}
