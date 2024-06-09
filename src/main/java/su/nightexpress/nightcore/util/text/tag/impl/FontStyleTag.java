package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.tag.api.SimpleTag;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;

public class FontStyleTag extends SimpleTag implements Decorator {

    public enum Style {
        BOLD, ITALIC, UNDERLINED, STRIKETHROUGH, OBFUSCATED
    }

    private final Style style;

    public FontStyleTag(@NotNull String name, @NotNull Style style) {
        super(name);
        this.style = style;
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
