package su.nightexpress.nightcore.util.text.tag.decorator;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.text.NightMessage;

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
        String fused = String.join(Placeholders.TAG_LINE_BREAK, this.text);
        BaseComponent component = NightMessage.parse(fused);

        Text text;
        if (Version.isAtLeast(Version.MC_1_20_6)) {
            text = new Text(component);
        }
        else {
            text = new Text(new ComponentBuilder().append(component).create());
        }

        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setHoverEvent(this.createEvent());
    }
}
