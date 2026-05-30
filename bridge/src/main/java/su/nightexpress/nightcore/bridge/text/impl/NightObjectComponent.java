package su.nightexpress.nightcore.bridge.text.impl;

import org.jspecify.annotations.NonNull;
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

    private NightObjectComponent(@NonNull List<? extends NightComponent> children, @NonNull NightStyle style,
                                 @NonNull NightObjectContents contents) {
        super(children, style);
        this.contents = contents;
    }

    @NonNull
    public static NightObjectComponent create(@NonNull NightStyle style, @NonNull NightObjectContents contents) {
        return new NightObjectComponent(Collections.emptyList(), style, contents);
    }

    @Override
    @NonNull
    public <T> T adapt(@NonNull TextComponentAdapter<T> adapter) {
        return adapter.adaptComponent(this);
    }

    @NonNull
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
    @NonNull
    public NightObjectComponent children(@NonNull List<? extends NightComponent> children) {
        return new NightObjectComponent(children, this.style, this.contents);
    }

    @Override
    @NonNull
    public NightObjectComponent style(@NonNull NightStyle style) {
        if (Objects.equals(this.style, style)) return this;

        return new NightObjectComponent(this.children, style, this.contents);
    }

    @NonNull
    public NightObjectComponent contents(@NonNull NightObjectContents contents) {
        if (Objects.equals(this.contents, contents)) return this;

        return new NightObjectComponent(this.children, this.style, contents);
    }
}
