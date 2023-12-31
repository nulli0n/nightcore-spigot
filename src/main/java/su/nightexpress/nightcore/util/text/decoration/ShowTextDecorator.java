package su.nightexpress.nightcore.util.text.decoration;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.List;

public class ShowTextDecorator implements Decorator {

    private final List<String> text;

    public ShowTextDecorator(@NotNull String... strings) {
        this(Lists.newList(strings));
    }

    public ShowTextDecorator(@NotNull List<String> text) {
        this.text = text;
    }

    @NotNull
    public HoverEvent createEvent() {
        String fused = String.join(Tags.LINE_BREAK.getFullName(), this.text);
        Text text = new Text(NightMessage.parse(fused));

        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setHoverEvent(this.createEvent());
    }
}
