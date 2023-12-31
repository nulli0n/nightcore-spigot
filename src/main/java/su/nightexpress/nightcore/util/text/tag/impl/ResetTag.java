package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.decoration.Decorator;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class ResetTag extends Tag implements Decorator {

    public ResetTag() {
        super("r");
    }

    @Override
    public int getWeight() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        component.setColor(null);
        component.setBold(false);
        component.setItalic(false);
        component.setObfuscated(false);
        component.setUnderlined(false);
        component.setStrikethrough(false);
        component.setFont(null);
        component.setInsertion(null);
        component.setHoverEvent(null);
        component.setClickEvent(null);
    }
}
