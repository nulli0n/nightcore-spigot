package su.nightexpress.nightcore.bridge.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NightStyle {

    public static final NightStyle EMPTY = new NightStyle(null, null, null, new HashMap<>(), null, null, null);

    private final Map<NightTextDecoration, NightTextDecoration.State> decorations;

    private final NightKey           font;
    private final Color              color;
    private final Color              shadowColor;
    private final NightClickEvent    clickEvent;
    private final NightHoverEvent<?> hoverEvent;
    private final String             insertion;

    NightStyle(
        @Nullable NightKey font,
        @Nullable Color color,
        @Nullable Color shadowColor,
        @NotNull Map<NightTextDecoration, NightTextDecoration.State> decorations,
        @Nullable NightClickEvent clickEvent,
        @Nullable NightHoverEvent<?> hoverEvent,
        @Nullable String insertion
    ) {
        this.font = font;
        this.color = color;
        this.shadowColor = shadowColor;
        this.decorations = decorations;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.insertion = insertion;

        for (NightTextDecoration decoration : NightTextDecoration.values()) {
            this.decorations.putIfAbsent(decoration, NightTextDecoration.State.FALSE);
        }
    }

    @Nullable
    public NightKey font() {
        return this.font;
    }

    @NotNull
    public NightStyle font(@Nullable NightKey font) {
        if (Objects.equals(this.font, font)) return this;

        return new NightStyle(font, this.color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }

    @Nullable
    public Color color() {
        return this.color;
    }

    @NotNull
    public NightStyle color(@Nullable Color color) {
        if (Objects.equals(this.color, color)) return this;

        return new NightStyle(this.font, color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }

    @NotNull
    public NightStyle color(int red, int green, int blue) {
        return this.color(new Color(red, green, blue));
    }

    @NotNull
    public NightStyle color(int red, int green, int blue, int alpha) {
        return this.color(new Color(red, green, blue, alpha));
    }

    @Nullable
    public Color shadowColor() {
        return this.shadowColor;
    }

    @NotNull
    public NightStyle shadowColor(@Nullable Color color) {
        if (Objects.equals(this.shadowColor, color)) return this;

        return new NightStyle(this.font, this.color, color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }

    @NotNull
    public NightStyle shadowColor(int red, int green, int blue) {
        return this.shadowColor(new Color(red, green, blue));
    }

    @NotNull
    public NightStyle shadowColor(int red, int green, int blue, int alpha) {
        return this.shadowColor(new Color(red, green, blue, alpha));
    }

    @NotNull
    public NightTextDecoration.State decoration(@NotNull NightTextDecoration decoration) {
        return this.decorations.getOrDefault(decoration, NightTextDecoration.State.NOT_SET);
    }

    @NotNull
    public NightStyle decoration(@NotNull NightTextDecoration decoration, boolean state) {
        return this.decoration(decoration, NightTextDecoration.State.byBoolean(state));
    }

    @NotNull
    public NightStyle decoration(@NotNull NightTextDecoration decoration, @NotNull NightTextDecoration.State state) {
        if (this.decoration(decoration) == state) return this;

        var newDecorations = new HashMap<>(this.decorations);
        newDecorations.put(decoration, state);

        return new NightStyle(this.font, this.color, this.shadowColor, newDecorations, this.clickEvent, this.hoverEvent, this.insertion);
    }

    @NotNull
    public Map<NightTextDecoration, NightTextDecoration.State> decorations() {
        return this.decorations;
    }

    @Nullable
    public NightClickEvent clickEvent() {
        return this.clickEvent;
    }

    @NotNull
    public NightStyle clickEvent(@Nullable NightClickEvent event) {
        return new NightStyle(this.font, this.color, this.shadowColor, this.decorations, event, this.hoverEvent, this.insertion);
    }

    @Nullable
    public NightHoverEvent<?> hoverEvent() {
        return this.hoverEvent;
    }

    @NotNull
    public NightStyle hoverEvent(@Nullable NightHoverEvent<?> hoverEvent) {
        return new NightStyle(this.font, this.color, this.shadowColor, this.decorations, this.clickEvent, hoverEvent, this.insertion);
    }

    @Nullable
    public String insertion() {
        return this.insertion;
    }

    @NotNull
    public NightStyle insertion(@Nullable String insertion) {
        if (Objects.equals(this.insertion, insertion)) return this;

        return new NightStyle(this.font, this.color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, insertion);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
