package su.nightexpress.nightcore.util.text.tag.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.decoration.Decorator;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

public class TranslateTag extends Tag implements Decorator {

    public static final String NAME = "translate";

    public TranslateTag() {
        super(NAME);
    }

    @Override
    public int getWeight() {
        return Integer.MAX_VALUE - 10;
    }

    @Override
    public void decorate(@NotNull BaseComponent component) {
        if (!(component instanceof TextComponent textComponent)) return;

        String content = textComponent.getText();
        textComponent.setText("");

        TranslatableComponent translatableComponent = new TranslatableComponent(content);
        component.addExtra(translatableComponent);
    }
}
