package su.nightexpress.nightcore.bridge.text.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NightKeybindComponent extends NightAbstractComponent {

    private final String key;

    NightKeybindComponent(@NotNull List<? extends NightComponent> children, @NotNull NightStyle style, @NotNull String key) {
        super(children, style);
        this.key = key;
    }

    @NotNull
    public static NightKeybindComponent create(@NotNull String key, @NotNull NightStyle style) {
        return new NightKeybindComponent(Collections.emptyList(), style, key);
    }

    @Override
    @NotNull
    public <T> T adapt(@NotNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @Override
    @NotNull
    public NightKeybindComponent children(@NotNull List<? extends NightComponent> children) {
        return new NightKeybindComponent(children, this.style, this.key);
    }

    @Override
    @NotNull
    public NightKeybindComponent style(@NotNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightKeybindComponent(this.children, style, this.key);
    }

    @NotNull
    public String key() {
        return this.key;
    }
}
