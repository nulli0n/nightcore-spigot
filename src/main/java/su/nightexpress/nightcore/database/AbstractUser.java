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

    public boolean isCacheExpired() {
        return this.getCachedUntil() > 0 && System.currentTimeMillis() > this.getCachedUntil();
    }

    public long getCachedUntil() {
        return cachedUntil;
    }

    public void setCachedUntil(long cachedUntil) {
        this.cachedUntil = cachedUntil;
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
