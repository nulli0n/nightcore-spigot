package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.api.SimpleTag;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;

public class FontStyleTag extends SimpleTag implements Decorator {

    private static final String INVERT_SIGN = "!";

    public enum Style {
        BOLD, ITALIC, UNDERLINED, STRIKETHROUGH, OBFUSCATED
    }

    private final Style style;
    private final boolean inverted;

    public FontStyleTag(@NotNull String name, @NotNull Style style, boolean inverted) {
        this(name, new String[0], style, inverted);
    }

    public FontStyleTag(@NotNull String name, String[] aliases, @NotNull Style style, boolean inverted) {
        super(name, aliases);
        this.style = style;
        this.inverted = inverted;
    }

    @NotNull
    public FontStyleTag inverted() {
        String[] aliases = this.aliases.stream().map(alias -> INVERT_SIGN + alias).toArray(String[]::new);

        return new FontStyleTag(aliases[0], aliases, this.style, true);
    }

    @NotNull
    public Style getStyle() {
        return this.style;
    }

    @Override
    public void decorate(@NotNull NightComponent component) {
        switch (this.style) {
            case BOLD -> component.setBold(!this.inverted);
            case ITALIC -> component.setItalic(!this.inverted);
            case OBFUSCATED -> component.setObfuscated(!this.inverted);
            case UNDERLINED -> component.setUnderlined(!this.inverted);
            case STRIKETHROUGH -> component.setStrikethrough(!this.inverted);
        }
    }
}
