package su.nightexpress.nightcore.util.text.decoration;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColorDecorator implements Decorator {

    private final Color color;

    public ColorDecorator(@NotNull Color color) {
        this.color = color;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setColor(ChatColor.of(this.color));
    }
}
