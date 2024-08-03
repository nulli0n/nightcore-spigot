package su.nightexpress.nightcore.database;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;

import java.util.UUID;

public abstract class AbstractUser<P extends NightCorePlugin> implements DataUser {

    protected final P plugin;
    protected final UUID uuid;

    protected String name;
    protected long   dateCreated;
    protected long   lastOnline;
    protected long   cachedUntil;
    protected long   autoSaveIn;
    protected long   nextSyncIn;

    public AbstractUser(@NotNull P plugin, @NotNull UUID uuid, @NotNull String name, long dateCreated, long lastOnline) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.setDateCreated(dateCreated);
        this.setLastOnline(lastOnline);
        this.setCachedUntil(-1);
    }

    public void onLoad() {

    }

    public void onUnload() {

    }

    private long getTimestamp(int seconds) {
        return seconds < 0 ? -1L : System.currentTimeMillis() + 1000L * seconds;
    }

    public boolean isCacheExpired() {
        return this.getCachedUntil() > 0 && System.currentTimeMillis() >= this.getCachedUntil();
    }

    @Override
    public boolean isAutoSaveReady() {
        return this.getAutoSaveIn() > 0 && System.currentTimeMillis() >= this.getAutoSaveIn();
    }

    @Override
    public boolean isSyncReady() {
        return this.getNextSyncIn() >= 0 && System.currentTimeMillis() >= this.getNextSyncIn();
    }

    @Override
    public void cancelAutoSave() {
        this.setAutoSaveIn(-1);
    }

    @Override
    public void cancelSynchronization() {
        this.setNextSyncIn(-1);
    }

    @Override
    public long getCachedUntil() {
        return cachedUntil;
    }

    @Override
    public void setCachedUntil(long cachedUntil) {
        this.cachedUntil = cachedUntil;
    }

    @Override
    public long getAutoSaveIn() {
        return autoSaveIn;
    }

    @Override
    public void setAutoSaveIn(int seconds) {
        this.autoSaveIn = this.getTimestamp(seconds);
    }

    @Override
    public long getNextSyncIn() {
        return nextSyncIn;
    }

    @Override
    public void setNextSyncIn(int seconds) {
        this.nextSyncIn = this.getTimestamp(seconds);
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
        return this.plugin.getServer().getOfflinePlayer(this.getId());
    }

    @Nullable
    public final Player getPlayer() {
        return this.plugin.getServer().getPlayer(this.getId());
    }

    @Override
    public String toString() {
        return "AbstractUser [uuid=" + this.uuid + ", name=" + this.name + ", lastOnline=" + this.lastOnline + "]";
    }
}
