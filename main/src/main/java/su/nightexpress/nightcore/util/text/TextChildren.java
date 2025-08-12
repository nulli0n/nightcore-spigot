package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.ComponentBuildable;

@Deprecated
public abstract class TextChildren implements ComponentBuildable {

    protected final TextGroup parent;

    public TextChildren(@NotNull TextGroup parent) {
        this.parent = parent;
    }

    @NotNull
    public TextGroup getParent() {
        return this.parent;
    }

    public abstract int textLength();
}
