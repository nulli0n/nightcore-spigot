package su.nightexpress.nightcore.database;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Deprecated
public interface DataUser {

    void onLoad();

    void onUnload();

    boolean isCacheExpired();

    boolean isAutoSaveReady();

    boolean isSyncReady();

    long getCachedUntil();

    long getAutoSaveIn();

    long getNextSyncIn();

    void cancelAutoSave();

    void cancelSynchronization();

    void setCachedUntil(long cachedUntil);

    void setAutoSaveIn(double seconds);

    void setNextSyncIn(int seconds);

    @NonNull
    UUID getId();

    @NonNull
    String getName();

    void setName(String name);

    long getDateCreated();

    void setDateCreated(long dateCreated);

    long getLastOnline();

    void setLastOnline(long lastOnline);

    boolean isOnline();

    @NonNull
    OfflinePlayer getOfflinePlayer();

    @Nullable
    Player getPlayer();
}
