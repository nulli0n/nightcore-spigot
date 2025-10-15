package su.nightexpress.nightcore.bridge.text.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.NightAbstractComponent;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NightObjectComponent extends NightAbstractComponent {

    private final NightObjectContents contents;

    private NightObjectComponent(@NotNull List<? extends NightComponent> children, @NotNull NightStyle style, @NotNull NightObjectContents contents) {
        super(children, style);
        this.contents = contents;
    }

    @NotNull
    public static NightObjectComponent create(@NotNull NightStyle style, @NotNull NightObjectContents contents) {
        return new NightObjectComponent(Collections.emptyList(), style, contents);
    }

    @Override
    @NotNull
    public <T> T adapt(@NotNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @NotNull
    public NightObjectContents contents() {
        return this.contents;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof NightObjectComponent that)) return false;
        return Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.contents);
    }

    @Override
    public String toString() {
        return "NightObjectComponent{" + "contents=" + contents + '}';
    }

    @Override
    @NotNull
    public NightObjectComponent children(@NotNull List<? extends NightComponent> children) {
        return new NightObjectComponent(children, this.style, this.contents);
    }

    @Override
    @NotNull
    public NightObjectComponent style(@NotNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightObjectComponent(this.children, style, this.contents);
    }

    @NotNull
    public NightObjectComponent contents(@NotNull NightObjectContents contents) {
        if (Objects.equals(this.contents, contents)) return this;

        return new NightObjectComponent(this.children, this.style, contents);
    }
}
