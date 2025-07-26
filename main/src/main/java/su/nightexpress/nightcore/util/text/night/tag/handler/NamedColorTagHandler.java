package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

import java.awt.*;

public class NamedColorTagHandler extends ClassicTagHandler {

    private final String colorName;
    private final Color  color;

    public NamedColorTagHandler(@NotNull String colorName, @NotNull Color color) {
        this.colorName = colorName;
        this.color = color;
    }

    @NotNull
    public String getColorName() {
        return this.colorName;
    }

    @NotNull
    public Color getColor() {
        return this.color;
    }

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        group.setStyle(style -> style.color(this.color));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
