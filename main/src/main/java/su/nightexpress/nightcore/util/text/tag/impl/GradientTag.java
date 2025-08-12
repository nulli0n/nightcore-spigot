package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.GradientColorDecorator;

import java.awt.*;
import java.util.stream.Stream;

@Deprecated
public class GradientTag extends Tag implements ContentTag {

    public static final String NAME = "gradient";

    public GradientTag() {
        super(NAME);
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String hexStart, @NotNull String hexEnd, @NotNull String text) {
        return this.wrap(text, hexStart, hexEnd);
    }

    @NotNull
    @Deprecated
    public String wrap(@NotNull String string, @NotNull String fromHex, @NotNull String toHex) {
        return this.wrap(string, new String[]{fromHex, toHex});
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull Color... colors) {
        return this.wrap(string, Stream.of(colors).map(ParserUtils::colorToHexString).toArray(String[]::new));
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull String... hexCodes) {
        String joined = String.join(String.valueOf(ParserUtils.DELIMITER), hexCodes);
        String tagOpen = TagUtils.brackets(this.getName() + ParserUtils.DELIMITER + joined);
        String tagClose = this.getClosingName();

        return tagOpen + string + tagClose;
    }

    @Override
    @Nullable
    public GradientColorDecorator parse(@NotNull String content) {
        String[] split = content.split(String.valueOf(ParserUtils.DELIMITER));

        int length = split.length;
        if (length < 2) return null;

        Color[] colors = new Color[length];

        for (int index = 0; index < length; index++) {
            Color stop = ParserUtils.colorFromSchemeOrHex(split[index]);
            if (stop == null) continue;

            colors[index] = stop;
        }

        return new GradientColorDecorator(colors);
    }
}
