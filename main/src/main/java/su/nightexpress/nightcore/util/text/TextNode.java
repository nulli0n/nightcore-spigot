package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;

public class TextNode extends TextChildren {

    private final StringBuilder textBuilder;

    public TextNode(@NotNull TextGroup parent) {
        super(parent);
        this.textBuilder = new StringBuilder();
    }

    public void append(char letter) {
        this.textBuilder.append(letter);
    }

    public void append(@NotNull String str) {
        this.textBuilder.append(str);
    }

    @NotNull
    public StringBuilder getTextBuilder() {
        return this.textBuilder;
    }

    @Override
    public int textLength() {
        int legnth = 0;
        for (int index = 0; index < this.textBuilder.length(); index++) {
            char letter = this.textBuilder.charAt(index);
            if (Character.isWhitespace(letter)) continue;

            legnth++;
        }

        return legnth;
    }

    @Override
    @NotNull
    public NightComponent toComponent() {
        String text = this.textBuilder.toString();
        ColorDecorator colorDecorator = this.parent.getColor();
        NightComponent textComponent = Engine.software().textComponent(text);

        if (colorDecorator != null) {
            colorDecorator.decorate(textComponent);
        }

        this.parent.getDecorators().forEach(decorator -> {
            decorator.decorate(textComponent);
        });

        return textComponent;
    }
}
