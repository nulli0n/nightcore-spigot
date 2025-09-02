package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

import java.awt.*;

public class ColorTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        Color color = ParserUtils.colorFromSchemeOrHex(tagContent);
        if (color == null) return;

        group.setStyle(nightStyle -> nightStyle.color(color));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
