package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class FontDecorator implements Decorator {

    private final String font;

    public FontDecorator(@NotNull String font) {
        this.font = font;
    }

    @Override
    public void decorate(@NotNull NightComponent component) {
        component.setFont(this.font);
    }
}
