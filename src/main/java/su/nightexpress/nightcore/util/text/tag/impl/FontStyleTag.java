package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.decoration.Decorator;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class FontStyleTag extends Tag implements Decorator {

    public enum Style {
        BOLD, ITALIC, UNDERLINED, STRIKETHROUGH, OBFUSCATED
    }

    private final Style style;

    public FontStyleTag(@NotNull String name, @NotNull Style style) {
        super(name);
        this.style = style;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @NotNull
    public Style getStyle() {
        return style;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        switch (this.getStyle()) {
            case BOLD -> component.setBold(true);
            case ITALIC -> component.setItalic(true);
            case OBFUSCATED -> component.setObfuscated(true);
            case UNDERLINED -> component.setUnderlined(true);
            case STRIKETHROUGH -> component.setStrikethrough(true);
        }
    }
}
