package su.nightexpress.nightcore.util.text.night.entry;

import org.jetbrains.annotations.NotNull;

public abstract class ChildEntry implements Entry {

    protected final EntryGroup parent;

    public ChildEntry(@NotNull EntryGroup parent) {
        this.parent = parent;
    }

    @NotNull
    public EntryGroup getParent() {
        return this.parent;
    }

    public abstract int textLength();
}
