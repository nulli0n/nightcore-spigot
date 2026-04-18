package su.nightexpress.nightcore.db.state;

public interface Stateful {

    boolean isDirty();

    boolean isClean();

    boolean isRemoved();

    void markDirty();

    void markClean();

    void markRemoved();
}
