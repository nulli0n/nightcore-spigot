package su.nightexpress.nightcore.util.text.night.tag.handler;

import java.awt.Color;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class ColorTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        Color color = ParserUtils.colorFromSchemeOrHex(tagContent);
        if (color == null) return;

        group.setStyle(nightStyle -> nightStyle.color(color));
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }
}
