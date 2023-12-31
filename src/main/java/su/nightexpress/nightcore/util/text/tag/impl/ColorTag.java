package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.decoration.Decorator;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

public class ColorTag extends Tag implements Decorator {

    protected final Color color;

    public ColorTag(@NotNull String name, @NotNull String hex) {
        this(name, Color.decode(hex));
    }

    public ColorTag(@NotNull Color color) {
        this(Integer.toHexString(color.getRGB()).substring(2), color);
    }

    public ColorTag(@NotNull String name, @NotNull Color color) {
        super(name);
        this.color = color;
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    @Override
    public boolean conflictsWith(@NotNull Tag tag) {
        return tag instanceof ColorTag;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setColor(ChatColor.of(this.getColor()));
    }
}
