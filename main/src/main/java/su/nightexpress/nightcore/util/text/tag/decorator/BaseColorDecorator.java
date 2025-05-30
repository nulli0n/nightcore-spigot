package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;

public class BaseColorDecorator implements ColorDecorator {

    private final Color color;

    public BaseColorDecorator(@NotNull Color color) {
        this.color = color;
    }

    @Override
    public void decorate(@NotNull NightComponent component) {
        component.setColor(this.color);
        //component.setColor(ChatColor.of(this.color));
    }
}
