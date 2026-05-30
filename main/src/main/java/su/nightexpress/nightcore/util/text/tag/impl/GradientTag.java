package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    @NonNull
    @Deprecated
    public String enclose(@NonNull String hexStart, @NonNull String hexEnd, @NonNull String text) {
        return this.wrap(text, hexStart, hexEnd);
    }

    @NonNull
    @Deprecated
    public String wrap(@NonNull String string, @NonNull String fromHex, @NonNull String toHex) {
        return this.wrap(string, new String[]{fromHex, toHex});
    }

    @NonNull
    public String wrap(@NonNull String string, @NonNull Color... colors) {
        return this.wrap(string, Stream.of(colors).map(ParserUtils::colorToHexString).toArray(String[]::new));
    }

    @NonNull
    public String wrap(@NonNull String string, @NonNull String... hexCodes) {
        String joined = String.join(String.valueOf(ParserUtils.DELIMITER), hexCodes);
        String tagOpen = TagUtils.brackets(this.getName() + ParserUtils.DELIMITER + joined);
        String tagClose = this.getClosingName();

        return tagOpen + string + tagClose;
    }

    @Override
    @Nullable
    public GradientColorDecorator parse(@NonNull String content) {
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
