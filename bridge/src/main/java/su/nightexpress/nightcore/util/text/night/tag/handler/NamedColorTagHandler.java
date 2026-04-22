package su.nightexpress.nightcore.util.text.night.tag.handler;

import java.awt.Color;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class NamedColorTagHandler extends ClassicTagHandler {

    private final String colorName;
    private final Color  color;

    public NamedColorTagHandler(@NonNull String colorName, @NonNull Color color) {
        this.colorName = colorName;
        this.color = color;
    }

    @NonNull
    public String getColorName() {
        return this.colorName;
    }

    @NonNull
    public Color getColor() {
        return this.color;
    }

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        group.setStyle(style -> style.color(this.color));
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }
}
