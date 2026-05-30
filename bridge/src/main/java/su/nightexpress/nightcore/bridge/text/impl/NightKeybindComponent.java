package su.nightexpress.nightcore.bridge.text.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NightKeybindComponent extends NightAbstractComponent {

    private final String key;

    NightKeybindComponent(@NonNull List<? extends NightComponent> children, @NonNull NightStyle style,
                          @NonNull String key) {
        super(children, style);
        this.key = key;
    }

    @NonNull
    public static NightKeybindComponent create(@NonNull String key, @NonNull NightStyle style) {
        return new NightKeybindComponent(Collections.emptyList(), style, key);
    }

    @Override
    @NonNull
    public <T> T adapt(@NonNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @Override
    @NonNull
    public NightKeybindComponent children(@NonNull List<? extends NightComponent> children) {
        return new NightKeybindComponent(children, this.style, this.key);
    }

    @Override
    @NonNull
    public NightKeybindComponent style(@NonNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightKeybindComponent(this.children, style, this.key);
    }

    @NonNull
    public String key() {
        return this.key;
    }
}
