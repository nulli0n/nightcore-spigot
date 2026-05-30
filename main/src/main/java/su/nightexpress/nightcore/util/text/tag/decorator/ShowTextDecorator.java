package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.List;

@Deprecated
public class ShowTextDecorator implements Decorator {

    private final List<String> text;

    public ShowTextDecorator(@NonNull String... strings) {
        this(Lists.newList(strings));
    }

    public ShowTextDecorator(@NonNull List<String> text) {
        this.text = text;
    }

    //    @NonNull
    //    public HoverEvent createEvent() {
    //        String fused = String.join(Placeholders.TAG_LINE_BREAK, this.text);
    //        BaseComponent component = NightMessage.parse(fused);
    //
    //        Text text;
    //        if (Version.isAtLeast(Version.MC_1_20_6)) {
    //            text = new Text(component);
    //        }
    //        else {
    //            text = new Text(new ComponentBuilder().append(component).create());
    //        }
    //
    //        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
    //    }

    @Override
    @NonNull
    public NightComponent decorate(@NonNull NightComponent component) {
        String oneLined = String.join(Placeholders.TAG_LINE_BREAK, this.text);
        NightComponent nightComponent = NightMessage.parse(oneLined);

        return component.hoverEvent(NightHoverEvent.showText((NightAbstractComponent) nightComponent));

        //component.setHoverEvent(NightMessage.parse(String.join(Placeholders.TAG_LINE_BREAK, this.text)));

        //component.setHoverEvent(HoverEventType.SHOW_TEXT, String.join(Placeholders.TAG_LINE_BREAK, this.text));
        //component.setHoverEvent(this.createEvent());
    }
}
