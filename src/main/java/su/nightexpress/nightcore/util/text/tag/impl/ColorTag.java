package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.tag.api.SimpleTag;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;

import java.awt.*;

public class ColorTag extends SimpleTag implements ColorDecorator {

    protected final Color color;

    public ColorTag(@NotNull String name, @NotNull String hex) {
        this(name, new String[0], hex);
    }

    public ColorTag(@NotNull String name, @NotNull String[] aliases, @NotNull String hex) {
        this(name, aliases, Color.decode(hex));
    }

    public ColorTag(@NotNull Color color) {
        this(color, new String[0]);
    }

    public ColorTag(@NotNull Color color, @NotNull String[] aliases) {
        this(Integer.toHexString(color.getRGB()).substring(2), aliases, color);
    }

    public ColorTag(@NotNull String name, @NotNull Color color) {
        this(name, new String[0], color);
    }

    public ColorTag(@NotNull String name, @NotNull String[] aliases, @NotNull Color color) {
        super(name, aliases);
        this.color = color;
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setColor(ChatColor.of(this.getColor()));
    }
}
