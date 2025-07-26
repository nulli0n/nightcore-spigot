package su.nightexpress.nightcore.bridge.text.impl;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static NightTextComponent create(@NotNull String content) {
        return new NightTextComponent(Collections.emptyList(), NightStyle.EMPTY, content);
    }

    @NotNull
    public static NightTextComponent create(@NotNull String content, @NotNull NightStyle style) {
        return new NightTextComponent(Collections.emptyList(), style, content);
    }

    private final String content;

    NightTextComponent(@NotNull List<? extends NightComponent> children, @NotNull NightStyle style, @NotNull String content) {
        super(children, style);
        this.content = content;
    }

    @NotNull
    public <T> T adapt(@NotNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @NotNull
    public String content() {
        return this.content;
    }

    @Override
    @NotNull
    public NightTextComponent children(@NotNull List<? extends NightComponent> children) {
        return new NightTextComponent(children, this.style, this.content);
    }

    @Override
    @NotNull
    public NightTextComponent style(@NotNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightTextComponent(this.children, style, this.content);
    }

    @NotNull
    public NightTextComponent content(@NotNull String content) {
        if (Objects.equals(this.content, content)) return this;

        return new NightTextComponent(this.children, this.style, content);
    }
}
