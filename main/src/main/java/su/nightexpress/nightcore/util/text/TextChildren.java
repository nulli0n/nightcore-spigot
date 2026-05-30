package su.nightexpress.nightcore.util.text;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.bridge.wrapper.ComponentBuildable;

@Deprecated
public abstract class TextChildren implements ComponentBuildable {

    protected final TextGroup parent;

    public TextChildren(@NonNull TextGroup parent) {
        this.parent = parent;
    }

    @NonNull
    public TextGroup getParent() {
        return this.parent;
    }

    public abstract int textLength();
}
