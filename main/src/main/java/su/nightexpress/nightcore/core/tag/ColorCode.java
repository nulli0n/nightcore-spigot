package su.nightexpress.nightcore.core.tag;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.text.night.tag.handler.NamedColorTagHandler;

import java.awt.*;

public record ColorCode(@NonNull String name, @NonNull Color color) {

    @NonNull
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
