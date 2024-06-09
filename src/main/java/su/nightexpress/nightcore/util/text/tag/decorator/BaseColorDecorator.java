package su.nightexpress.nightcore.util.text.tag.decorator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BaseColorDecorator implements ColorDecorator {

    private final Color color;

    public BaseColorDecorator(@NotNull Color color) {
        this.color = color;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setColor(ChatColor.of(this.color));
    }
}
