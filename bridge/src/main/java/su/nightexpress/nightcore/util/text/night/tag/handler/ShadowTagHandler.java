package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Numbers;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;
import su.nightexpress.nightcore.util.text.night.tag.TagContent;

import java.awt.*;

public class ShadowTagHandler extends ClassicTagHandler {

    private static final float DEFAULT_ALPHA = 0.25f;
    private static final float MAX_ALPHA = 1f;

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null || tagContent.length() < 7) return;

        TagContent content = ParserUtils.parseInnerContent(tagContent);
        String alphaRaw = content.second();

        Color color = ParserUtils.colorFromSchemeOrHex(content.first());
        if (color == null) return;

        float[] rgb = color.getRGBColorComponents(null);
        float alpha = alphaRaw == null ? DEFAULT_ALPHA: Math.min(MAX_ALPHA, Numbers.getFloatAbs(alphaRaw));

        group.setStyle(nightStyle -> nightStyle.shadowColor(new Color(rgb[0], rgb[1], rgb[2], alpha)));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
