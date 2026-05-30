package su.nightexpress.nightcore.bridge.text.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    NightTranslatableComponent(@NonNull List<? extends NightComponent> children,
                               @NonNull NightStyle style,
                               @NonNull String key,
                               @Nullable String fallback,
                               @NonNull List<NightTranslationArgument> args) {
        super(children, style);
        this.key = key;
        this.fallback = fallback;
        this.args = new ArrayList<>(args);
    }

    @NonNull
    public static NightTranslatableComponent create(@NonNull String key, @NonNull NightStyle style) {
        return create(key, null, style);
    }

    @NonNull
    public static NightTranslatableComponent create(@NonNull String key, @Nullable String fallback,
                                                    @NonNull NightStyle style) {
        return create(key, fallback, Collections.emptyList(), style);
    }

    @NonNull
    public static NightTranslatableComponent create(@NonNull String key, @Nullable String fallback,
                                                    @NonNull List<NightTranslationArgument> args,
                                                    @NonNull NightStyle style) {
        return new NightTranslatableComponent(Collections.emptyList(), style, key, fallback, args);
    }

    @NonNull
    public <T> T adapt(@NonNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @Override
    @NonNull
    public NightTranslatableComponent children(@NonNull List<? extends NightComponent> children) {
        return new NightTranslatableComponent(children, this.style, this.key, this.fallback, this.args);
    }

    @Override
    @NonNull
    public NightTranslatableComponent style(@NonNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightTranslatableComponent(this.children, style, this.key, this.fallback, this.args);
    }

    @NonNull
    public String key() {
        return this.key;
    }

    @NonNull
    public List<NightTranslationArgument> arguments() {
        return this.args;
    }

    @Nullable
    public String fallback() {
        return this.fallback;
    }
}
