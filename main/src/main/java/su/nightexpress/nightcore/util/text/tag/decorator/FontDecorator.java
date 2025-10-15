package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@Deprecated
public class FontDecorator implements Decorator {

    private final String font;

    public FontDecorator(@NotNull String font) {
        this.font = font;
    }

    @Override
    @NotNull
    public NightComponent decorate(@NotNull NightComponent component) {
        return component.font(NightKey.key(this.font));
    }
}
