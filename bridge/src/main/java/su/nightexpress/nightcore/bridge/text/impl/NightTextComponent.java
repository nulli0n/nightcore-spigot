package su.nightexpress.nightcore.bridge.text.impl;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NightTextComponent extends NightAbstractComponent {

    public static final NightTextComponent EMPTY   = create("");
    public static final NightTextComponent NEWLINE = create("\n");
    public static final NightTextComponent SPACE   = create(" ");

    @NonNull
    public static NightTextComponent create(@NonNull String content) {
        return new NightTextComponent(Collections.emptyList(), NightStyle.EMPTY, content);
    }

    @NonNull
    public static NightTextComponent create(@NonNull String content, @NonNull NightStyle style) {
        return new NightTextComponent(Collections.emptyList(), style, content);
    }

    private final String content;

    NightTextComponent(@NonNull List<? extends NightComponent> children, @NonNull NightStyle style,
                       @NonNull String content) {
        super(children, style);
        this.content = content;
    }

    @NonNull
    public <T> T adapt(@NonNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @NonNull
    public String content() {
        return this.content;
    }

    @Override
    @NonNull
    public NightTextComponent children(@NonNull List<? extends NightComponent> children) {
        return new NightTextComponent(children, this.style, this.content);
    }

    @Override
    @NonNull
    public NightTextComponent style(@NonNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightTextComponent(this.children, style, this.content);
    }

    @NonNull
    public NightTextComponent content(@NonNull String content) {
        if (Objects.equals(this.content, content)) return this;

        return new NightTextComponent(this.children, this.style, content);
    }
}
