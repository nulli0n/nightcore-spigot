package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;

@Deprecated
public class BaseColorDecorator implements ColorDecorator {

    private final Color color;

    public BaseColorDecorator(@NonNull Color color) {
        this.color = color;
    }

    @Override
    @NonNull
    public NightComponent decorate(@NonNull NightComponent component) {
        return component.color(this.color);
    }
}
