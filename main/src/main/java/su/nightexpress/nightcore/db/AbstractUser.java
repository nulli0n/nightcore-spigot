package su.nightexpress.nightcore.db;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.TimeUtil;

import java.util.UUID;

@Deprecated
public abstract class AbstractUser {

    protected final UUID uuid;

    protected String name;
    protected long   dateCreated;
    protected long   lastOnline;
    protected long cacheExpireTime;
    protected long autoSaveTime;
    protected long autoSyncTime;

    public AbstractUser(@NotNull UUID uuid, @NotNull String name, long dateCreated, long lastOnline) {
        this.uuid = uuid;
        this.name = name;
        this.setDateCreated(dateCreated);
        this.setLastOnline(lastOnline);

        this.setPermanentCache();
        this.disableAutoSave();
    }

    @Deprecated
    public void onLoad() {

    }

    @Deprecated
    public void onUnload() {

    }


    public boolean isCacheExpired() {
        return TimeUtil.isPassed(this.cacheExpireTime);
    }

    public boolean isAutoSaveReady() {
        return TimeUtil.isPassed(this.autoSaveTime);
    }

    public boolean isAutoSyncReady() {
        return TimeUtil.isPassed(this.autoSyncTime);
    }


    public void setPermanentCache() {
        this.setCacheFor(-1);
    }

    public void disableAutoSave() {
        this.setAutoSaveIn(-1);
    }

    public void disableAutoSync() {
        this.setAutoSyncIn(-1);
    }


    public long getCacheExpireTime() {
        return this.cacheExpireTime;
    }

    public void setCacheFor(long seconds) {
        this.cacheExpireTime = TimeUtil.createFutureTimestamp(seconds);
    }


    public boolean isAutoSavePlanned() {
        return this.autoSaveTime > 0;
    }

    public long getAutoSaveTime() {
        return this.autoSaveTime;
    }

    public void setAutoSaveIn(double seconds) {
        this.autoSaveTime = TimeUtil.createFutureTimestamp(seconds);
    }


    public boolean isAutoSyncPlanned() {
        return this.autoSyncTime > 0;
    }

    public long getAutoSyncTime() {
        return this.autoSyncTime;
    }

    public void setAutoSyncIn(int seconds) {
        this.autoSyncTime = TimeUtil.createFutureTimestamp(seconds);
    }


    @NotNull
    public final UUID getId() {
        return this.uuid;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final long getDateCreated() {
        return dateCreated;
    }

    public final void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public final long getLastOnline() {
        return this.lastOnline;
    }

    public final void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public final boolean isOnline() {
        return this.getPlayer() != null;
    }

    @NotNull
    public final OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.getId());
    }

    @Nullable
    public final Player getPlayer() {
        return Bukkit.getPlayer(this.getId());
    }

    @Override
    public String toString() {
        return "AbstractUser [uuid=" + this.uuid + ", name=" + this.name + ", lastOnline=" + this.lastOnline + "]";
    }
}
