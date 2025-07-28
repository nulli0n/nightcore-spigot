package su.nightexpress.nightcore.util.profile;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.core.CoreConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CachedProfile {

    private final boolean permanent;
    private final boolean noUpdate;

    private NightProfile profile;
    private long lastQueried;
    private boolean inUpdate;

    public CachedProfile(@NotNull NightProfile profile, boolean permanent, boolean noUpdate) {
        this.profile = profile;
        this.lastQueried = 0L;
        this.permanent = permanent;
        this.noUpdate = noUpdate;
    }

    @NotNull
    public NightProfile query() {
        if (this.isUpdateTime()) {
            this.update();
        }

        return this.queryNoUpdate();
    }

    @NotNull
    public NightProfile queryNoUpdate() {
        this.updateQueryTime();
        return this.profile;
    }

    @NotNull
    public CompletableFuture<CachedProfile> update() {
        if (this.noUpdate) {
            return CompletableFuture.supplyAsync(() -> this);
        }
        if (this.inUpdate) {
            return CompletableFuture.supplyAsync(() -> this);
        }

        this.inUpdate = true;
        this.updateQueryTime();

        return this.profile.update().thenApply(updated -> {
            this.profile = updated;
            this.inUpdate = false;
            return this;
        });
    }

    private void updateQueryTime() {
        this.lastQueried = System.currentTimeMillis();
    }

    public boolean isUpdateTime() {
        return !this.noUpdate && !this.permanent && this.checkTime(CoreConfig.PROFILE_CACHE_UPDATE_TIME.get());
    }

    public boolean isPurgeTime() {
        return !this.permanent && this.checkTime(CoreConfig.PROFILE_CACHE_PURGE_TIME.get());
    }

    private boolean checkTime(int minutes) {
        if (minutes < 0) return false;

        long timeSinceQuery = (System.currentTimeMillis() - this.lastQueried);
        long timeThreshold = TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);

        return timeSinceQuery >= timeThreshold;
    }
}
