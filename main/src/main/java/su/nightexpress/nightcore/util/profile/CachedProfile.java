package su.nightexpress.nightcore.util.profile;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.core.CoreConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class CachedProfile {

    private static final ConcurrentLinkedQueue<CachedProfile> UPDATE_CANDIDATES = new ConcurrentLinkedQueue<>();

    private final boolean permanent;
    private final boolean noUpdate;

    private NightProfile profile;
    private long lastQueried;
    private long lastUpdated;
    private boolean inUpdate;

    public CachedProfile(@NotNull NightProfile profile, boolean permanent, boolean noUpdate) {
        this.profile = profile;
        this.lastQueried = 0L;
        this.lastUpdated = 0L;
        this.permanent = permanent;
        this.noUpdate = noUpdate;
    }

    public static void updateCandidates(int amount) {
        for (int i = 0; i < amount; i++) {
            if (UPDATE_CANDIDATES.isEmpty()) break;

            UPDATE_CANDIDATES.poll().update();
        }
    }

    @NotNull
    public NightProfile query() {
        if (this.isUpdateTime()) {
            this.scheduleUpdate();
        }

        return this.queryNoUpdate();
    }

    @NotNull
    public NightProfile queryNoUpdate() {
        this.updateQueryTime();
        return this.profile;
    }

    public void scheduleUpdate() {
        if (this.noUpdate || this.inUpdate) return;

        this.inUpdate = true;
        UPDATE_CANDIDATES.add(this);
    }

    @NotNull
    public CompletableFuture<CachedProfile> update() {
        return this.profile.update().thenApply(updated -> {
            this.update(updated);
            return this;
        });
    }

    public void update(@NotNull NightProfile updated) {
        this.profile = updated;
        this.inUpdate = false;
        this.updateUpdateTime();
    }

    /*@NotNull
    public CompletableFuture<CachedProfile> update() {
        if (this.noUpdate || this.inUpdate) {
            return CompletableFuture.supplyAsync(() -> this);
        }

        this.inUpdate = true;
        this.updateQueryTime();
        this.updateUpdateTime();
        UPDATE_CANDIDATES.add(this);

        return this.profile.update().thenApply(updated -> {
            this.profile = updated;
            this.inUpdate = false;
            return this;
        });
    }*/

    private void updateQueryTime() {
        this.lastQueried = System.currentTimeMillis();
    }

    private void updateUpdateTime() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isUpdateTime() {
        return !this.inUpdate && !this.noUpdate && this.checkTime(this.lastUpdated, CoreConfig.PROFILE_CACHE_UPDATE_TIME.get());
    }

    public boolean isPurgeTime() {
        return !this.permanent && this.checkTime(this.lastQueried, CoreConfig.PROFILE_CACHE_PURGE_TIME.get());
    }

    private boolean checkTime(long timestamp, int minutes) {
        if (minutes < 0) return false;

        long timeSince = (System.currentTimeMillis() - timestamp);
        long timeThreshold = TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);

        return timeSince >= timeThreshold;
    }
}
