package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

@Deprecated
public class FontDecorator implements Decorator {

    private final String font;

    public FontDecorator(@NonNull String font) {
        this.font = font;
    }

    @Override
    @NonNull
    public NightComponent decorate(@NonNull NightComponent component) {
        return component.font(NightKey.key(this.font));
    }
}
