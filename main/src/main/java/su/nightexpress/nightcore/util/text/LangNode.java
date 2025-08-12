package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;
import su.nightexpress.nightcore.util.text.tag.decorator.Decorator;

@Deprecated
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
        NightComponent component = NightComponent.translatable(this.key, this.fallback);

        if (colorDecorator != null) {
            component = colorDecorator.decorate(component);
        }

        for (Decorator decorator : this.parent.getDecorators()) {
            component = decorator.decorate(component);
        }

        return component;
    }
}
