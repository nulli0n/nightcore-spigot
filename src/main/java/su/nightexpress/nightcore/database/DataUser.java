package su.nightexpress.nightcore.database;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface DataUser {

    void onLoad();

    void onUnload();

    boolean isCacheExpired();

    boolean isAutoSaveReady();

    boolean isSyncReady();

    long getCachedUntil();

    void setCachedUntil(long cachedUntil);

    long getAutoSaveIn();

    void setAutoSaveIn(int seconds);

    long getNextSyncIn();

    void setNextSyncIn(int seconds);

    void cancelAutoSave();

    void cancelSynchronization();

    @NotNull
    UUID getId();

    @NotNull
    String getName();

    void setName(String name);

    long getDateCreated();

    void setDateCreated(long dateCreated);

    long getLastOnline();

    void setLastOnline(long lastOnline);

    boolean isOnline();

    @NotNull
    OfflinePlayer getOfflinePlayer();

    @Nullable
    Player getPlayer();
}
