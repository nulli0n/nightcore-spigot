package su.nightexpress.nightcore.bridge.text.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NightTranslatableComponent extends NightAbstractComponent {

    private final String key;
    private final String fallback;

    private final List<NightTranslationArgument> args;

    NightTranslatableComponent(@NotNull List<? extends NightComponent> children,
                                      @NotNull NightStyle style,
                                      @NotNull String key,
                                      @Nullable String fallback,
                                      @NotNull List<NightTranslationArgument> args) {
        super(children, style);
        this.key = key;
        this.fallback = fallback;
        this.args = new ArrayList<>(args);
    }

    @NotNull
    public static NightTranslatableComponent create(@NotNull String key, @NotNull NightStyle style) {
        return create(key, null, style);
    }

    @NotNull
    public static NightTranslatableComponent create(@NotNull String key, @Nullable String fallback, @NotNull NightStyle style) {
        return create(key, fallback, Collections.emptyList(), style);
    }

    @NotNull
    public static NightTranslatableComponent create(@NotNull String key, @Nullable String fallback, @NotNull List<NightTranslationArgument> args, @NotNull NightStyle style) {
        return new NightTranslatableComponent(Collections.emptyList(), style, key, fallback, args);
    }

    @NotNull
    public <T> T adapt(@NotNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @Override
    @NotNull
    public NightTranslatableComponent children(@NotNull List<? extends NightComponent> children) {
        return new NightTranslatableComponent(children, this.style, this.key, this.fallback, this.args);
    }

    @Override
    @NotNull
    public NightTranslatableComponent style(@NotNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightTranslatableComponent(this.children, style, this.key, this.fallback, this.args);
    }

    @NotNull
    public String key() {
        return this.key;
    }

    @NotNull
    public List<NightTranslationArgument> arguments() {
        return this.args;
    }

    @Nullable
    public String fallback() {
        return this.fallback;
    }
}
