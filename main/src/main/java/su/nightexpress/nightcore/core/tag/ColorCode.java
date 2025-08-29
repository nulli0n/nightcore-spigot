package su.nightexpress.nightcore.core.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.text.night.tag.handler.NamedColorTagHandler;

import java.awt.*;

public record ColorCode(@NotNull String name, @NotNull Color color) {

    @NotNull
    public NamedColorTagHandler createHandler() {
        return new NamedColorTagHandler(this.name, this.color);
    }

    @Override
    public String toString() {
        return "ColorCode{" +
            "name='" + name + '\'' +
            ", color=" + color +
            '}';
    }
}
