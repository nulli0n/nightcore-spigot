package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;

public class LangNode extends TextChildren {

    private final String key;
    private final String fallback;

    public LangNode(@NotNull TextGroup parent, @NotNull String key, @Nullable String fallback) {
        super(parent);
        this.key = key;
        this.fallback = fallback;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NotNull
    public NightComponent toComponent() {
        ColorDecorator colorDecorator = this.parent.getColor();
        NightComponent textComponent = Engine.software().translateComponent(this.key, this.fallback);

        if (colorDecorator != null) {
            colorDecorator.decorate(textComponent);
        }

        this.parent.getDecorators().forEach(decorator -> {
            decorator.decorate(textComponent);
        });

        return textComponent;
    }
}
