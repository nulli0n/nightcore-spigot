package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.event.ClickEvents;

@Deprecated
public class ClickDecorator implements Decorator {

    private final ClickEventType action;
    private final String         value;

    public ClickDecorator(@NonNull ClickEventType action, @NonNull String value) {
        this.action = action;
        this.value = value;
    }

    //    @NonNull
    //    public ClickEvent createEvent() {
    //        return new ClickEvent(this.action, this.value);
    //    }

    @Override
    @NonNull
    public NightComponent decorate(@NonNull NightComponent component) {
        NightClickEvent clickEvent = switch (this.action) {
            case COPY_TO_CLIPBOARD -> ClickEvents.copyToClipboard(this.value);
            case SUGGEST_COMMAND -> ClickEvents.suggestCommand(this.value);
            case RUN_COMMAND -> ClickEvents.runCommand(this.value);
            case CHANGE_PAGE -> ClickEvents.changePage(NumberUtil.getIntegerAbs(this.value));
            case OPEN_FILE -> ClickEvents.openFile(this.value);
            case OPEN_URL -> ClickEvents.openUrl(this.value);
        };

        return component.clickEvent(clickEvent);

        //component.setClickEvent(this.action, this.value);

        //component.setClickEvent(this.createEvent());
    }
}
