package su.nightexpress.nightcore.util.text.tag.decorator;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public class FontDecorator implements Decorator {

    private final String font;

    public FontDecorator(@NotNull String font) {
        this.font = font;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setFont(this.font);
    }
}
