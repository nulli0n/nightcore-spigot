package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.decoration.*;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

public class HexColorTag extends Tag implements DecoratorParser {

    public static final String NAME = "color";

    public HexColorTag() {
        super(NAME);
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public boolean conflictsWith(@NotNull Tag tag) {
        return tag instanceof ColorTag || tag instanceof HexColorTag;
    }

    @Override
    @Nullable
    public ParsedDecorator parse(@NotNull String content) {
        int tagLength = this.getName().length() + 1; // 1 for semicolon
        content = content.substring(tagLength);

        try {
            Color color = Color.decode(content);
            ColorDecorator decorator = new ColorDecorator(color);

            return new ParsedDecorator(decorator, 7 + tagLength);
        }
        catch (NumberFormatException exception) {
            return null;
        }
    }
}
