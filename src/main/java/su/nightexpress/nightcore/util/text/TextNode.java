package su.nightexpress.nightcore.util.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;

public class TextNode implements ComponentBuildable {

    private final StringBuilder textBuilder;
    private final TextGroup parent;

    private String translation;

    public TextNode(@NotNull TextGroup parent) {
        this.textBuilder = new StringBuilder();
        this.parent = parent;
    }

    public void append(char letter) {
        this.textBuilder.append(letter);
    }

    public void append(@NotNull String str) {
        this.textBuilder.append(str);
    }

    public void setTranslation(@Nullable String translation) {
        this.translation = translation;
    }

    @NotNull
    public StringBuilder getTextBuilder() {
        return textBuilder;
    }

    @NotNull
    public TextGroup getParent() {
        return parent;
    }

    public int textLength() {
        int legnth = 0;
        if (this.translation != null) return 1;
        //if (this.textBuilder.toString().equals("\n")) return legnth; // Do not count length for line breaks.

        for (int index = 0; index < this.textBuilder.length(); index++) {
            char letter = this.textBuilder.charAt(index);
            if (Character.isWhitespace(letter)) continue;

            legnth++;
        }
        return legnth;
    }

    @Override
    @NotNull
    public BaseComponent toComponent() {
        String text = this.textBuilder.toString();
        if (CoreConfig.LEGACY_COLOR_SUPPORT.get()) {
            text = Colorizer.legacy(text);
        }

        ColorDecorator colorDecorator = this.parent.getColor();
        BaseComponent textComponent = this.translation == null ? new TextComponent(text) : new TranslatableComponent(this.translation);

        if (colorDecorator != null) {
            colorDecorator.decorate(textComponent);
        }

        this.parent.getDecorators().forEach(decorator -> {
            decorator.decorate(textComponent);
        });

        return textComponent;
    }
}
