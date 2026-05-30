package su.nightexpress.nightcore.db.state;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.TimeUtil;

public abstract class StatefulData implements Stateful {

    protected volatile State state       = State.CLEAN;
    protected volatile long  cachedUntil = -1L;

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

    @Override
    public boolean isCacheExpired() {
        return this.cachedUntil >= 0L && TimeUtil.isPassed(this.cachedUntil);
    }

    @Override
    public long getCachedUntil() {
        return this.cachedUntil;
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

    @Override
    public void setPermanentCache() {
        this.setCachedUntil(-1L);
    }

    @Override
    public void setExpiredCache() {
        this.setCachedUntil(0L);
    }

    @Override
    public void setCacheTime(long value, @NonNull TimeUnit timeUnit) {
        this.setCachedUntil(TimeUtil.createFutureTimestamp(value, timeUnit));
    }

    @Override
    public void setCachedUntil(long cachedUntil) {
        this.cachedUntil = cachedUntil;
    }
}
