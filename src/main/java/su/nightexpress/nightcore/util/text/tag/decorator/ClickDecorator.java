package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class ClickDecorator implements Decorator {

    private final ClickEventType action;
    private final String            value;

    public ClickDecorator(@NotNull ClickEventType action, @NotNull String value) {
        this.action = action;
        this.value = value;
    }

//    @NotNull
//    public ClickEvent createEvent() {
//        return new ClickEvent(this.action, this.value);
//    }

    @Override
    public void decorate(@NotNull NightComponent component) {
        component.setClickEvent(this.action, this.value);
        //component.setClickEvent(this.createEvent());
    }
}
