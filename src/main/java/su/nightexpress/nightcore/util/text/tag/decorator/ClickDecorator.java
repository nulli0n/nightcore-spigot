package su.nightexpress.nightcore.util.text.tag.decorator;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;

public class ClickDecorator implements Decorator {

    private final ClickEvent.Action action;
    private final String            value;

    public ClickDecorator(@NotNull ClickEvent.Action action, @NotNull String value) {
        this.action = action;
        this.value = value;
    }

    @NotNull
    public ClickEvent createEvent() {
        return new ClickEvent(this.action, this.value);
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setClickEvent(this.createEvent());
    }
}
