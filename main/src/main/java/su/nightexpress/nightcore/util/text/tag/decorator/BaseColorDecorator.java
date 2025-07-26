package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;

@Deprecated
public class BaseColorDecorator implements ColorDecorator {

    private final Color color;

    public BaseColorDecorator(@NotNull Color color) {
        this.color = color;
    }

    @Override
    @NotNull
    public NightComponent decorate(@NotNull NightComponent component) {
        return component.color(this.color);
    }
}
