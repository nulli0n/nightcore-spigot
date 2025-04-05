package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;

public class TextNode implements ComponentBuildable {

    private final StringBuilder textBuilder;
    private final TextGroup parent;

    private boolean translation;

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

    public void setTranslation(boolean translation) {
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
        if (this.translation) return 1;

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
        Software software = Version.software();

        //BaseComponent textComponent = this.translation == null ? new TextComponent(text) : new TranslatableComponent(this.translation);
        NightComponent textComponent = this.translation ? software.translateComponent(text) : software.textComponent(text);

        if (colorDecorator != null) {
            colorDecorator.decorate(textComponent);
        }

        this.parent.getDecorators().forEach(decorator -> {
            decorator.decorate(textComponent);
        });

        return textComponent;
    }
}
