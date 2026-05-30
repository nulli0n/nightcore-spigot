package su.nightexpress.nightcore.db.state;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;

public interface Stateful {

    boolean isDirty();

    boolean isClean();

    boolean isRemoved();

    boolean isCacheExpired();

    long getCachedUntil();

    void markDirty();

    void markClean();

    void markRemoved();

    void setPermanentCache();

    void setExpiredCache();

    void setCacheTime(long value, @NonNull TimeUnit timeUnit);

    void setCachedUntil(long cachedUntil);
}
