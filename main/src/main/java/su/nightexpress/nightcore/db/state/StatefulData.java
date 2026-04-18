package su.nightexpress.nightcore.db.state;

import org.jspecify.annotations.NonNull;

public abstract class StatefulData implements Stateful {

    protected State state = State.CLEAN;

    @NonNull
    public final State getState() {
        return this.state;
    }

    public final void setState(@NonNull State state) {
        this.state = state;
    }

    public boolean isDirty() {
        return this.state == State.DIRTY;
    }

    public boolean isClean() {
        return this.state == State.CLEAN;
    }

    public boolean isRemoved() {
        return this.state == State.REMOVED;
    }

    public void markDirty() {
        this.setState(State.DIRTY);
    }

    public void markClean() {
        this.setState(State.CLEAN);
    }

    public void markRemoved() {
        this.setState(State.REMOVED);
    }
}
