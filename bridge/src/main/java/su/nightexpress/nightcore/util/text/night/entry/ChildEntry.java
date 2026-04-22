package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.NonNull;

public abstract class ChildEntry implements Entry {

    protected final EntryGroup parent;

    protected ChildEntry(@NonNull EntryGroup parent) {
        this.parent = parent;
    }

    @NonNull
    public EntryGroup getParent() {
        return this.parent;
    }

    public abstract int textLength();
}
