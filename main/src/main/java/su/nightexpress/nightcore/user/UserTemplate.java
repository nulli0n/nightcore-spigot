package su.nightexpress.nightcore.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public abstract class UserTemplate {

    protected final UUID uuid;

    protected String name;
    protected boolean dirty;

    public UserTemplate(@NotNull UUID uuid, @NotNull String name, long dateCreated, long lastOnline) {
        this.uuid = uuid;
        this.name = name;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = false;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public final boolean isOnline() {
        return this.player().isPresent();
    }

    @NotNull
    public final OfflinePlayer offlinePlayer() {
        return Bukkit.getOfflinePlayer(this.getId());
    }

    @NotNull
    public final Optional<Player> player() {
        return Optional.ofNullable(Bukkit.getPlayer(this.uuid));
    }

    @NotNull
    public final UUID getId() {
        return this.uuid;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String name) {
        this.name = name;
    }
}
