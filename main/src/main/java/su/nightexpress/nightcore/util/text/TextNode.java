package su.nightexpress.nightcore.util.text;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;

@Deprecated
public class TextNode extends TextChildren {

    private final StringBuilder textBuilder;

    public TextNode(@NonNull TextGroup parent) {
        super(parent);
        this.textBuilder = new StringBuilder();
    }

    public void append(char letter) {
        this.textBuilder.append(letter);
    }

    public void append(@NonNull String str) {
        this.textBuilder.append(str);
    }

    @NonNull
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
    @NonNull
    public NightComponent toComponent() {
        ColorDecorator colorDecorator = this.parent.getColor();
        NightComponent component = NightComponent.text(this.textBuilder.toString());

        for (Decorator decorator : this.parent.getDecorators()) {
            component = decorator.decorate(component);
        }

        if (colorDecorator != null) {
            component = colorDecorator.decorate(component);
        }

        return component;
    }
}
