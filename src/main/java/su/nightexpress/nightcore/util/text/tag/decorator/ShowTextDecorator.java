package su.nightexpress.nightcore.util.text.tag.decorator;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.bridge.wrapper.HoverEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.List;

public class ShowTextDecorator implements Decorator {

    private final List<String> text;

    public ShowTextDecorator(@NotNull String... strings) {
        this(Lists.newList(strings));
    }

    public ShowTextDecorator(@NotNull List<String> text) {
        this.text = text;
    }

//    @NotNull
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
    public void decorate(@NotNull NightComponent component) {
        component.setHoverEvent(HoverEventType.SHOW_TEXT, String.join(Placeholders.TAG_LINE_BREAK, this.text));
        //component.setHoverEvent(this.createEvent());
    }
}
