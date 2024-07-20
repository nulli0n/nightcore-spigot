package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.decorator.GradientColorDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

public class GradientTag extends Tag implements ContentTag {

    public static final String NAME = "gradient";

    public GradientTag() {
        super(NAME);
    }

    @NotNull
    public String enclose(@NotNull String hexStart, @NotNull String hexEnd, @NotNull String text) {
        String tagOpen = brackets(this.getName() + ":" + hexStart + ":" + hexEnd);
        String tagClose = this.getClosingName();

        return tagOpen + text + tagClose;
    }

    @Override
    @Nullable
    public GradientColorDecorator parse(@NotNull String content) {
        String[] split = content.split(":");
        if (split.length < 2) return null;

        String code1 = split[0];
        String code2 = split[1];

        Color from;
        Color to;

        // TODO Support for named colors
        try {
            from = Color.decode(code1);
            to = Color.decode(code2);
        }
        catch (NumberFormatException exception) {
            return null;
        }

        return new GradientColorDecorator(from, to);
    }
}
