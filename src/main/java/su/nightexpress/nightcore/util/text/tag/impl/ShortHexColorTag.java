package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.decoration.DecoratorParser;
import su.nightexpress.nightcore.util.text.decoration.ParsedDecorator;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

@Deprecated
public class ShortHexColorTag extends Tag implements DecoratorParser {

    public ShortHexColorTag() {
        super("#");
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public boolean conflictsWith(@NotNull Tag tag) {
        return super.conflictsWith(tag) || tag instanceof ColorTag || tag instanceof GradientTag;
    }

    @Override
    @Nullable
    public ParsedDecorator parse(@NotNull String content) {
        Color color;

        try {
            color = Color.decode(content);
        }
        catch (NumberFormatException exception) {
            return null;
        }

        return new ParsedDecorator(new ColorTag(color), 7);
    }
}
