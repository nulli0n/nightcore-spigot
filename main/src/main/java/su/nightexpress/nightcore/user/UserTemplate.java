package su.nightexpress.nightcore.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public abstract class UserTemplate {

    protected final UUID uuid;

    protected String name;
    protected boolean dirty;

    @Deprecated
    public UserTemplate(@NonNull UUID uuid, @NonNull String name, long dateCreated, long lastOnline) {
        this(uuid, name);
    }

    public UserTemplate(@NonNull UUID uuid, @NonNull String name) {
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

    public boolean isHolder(@NonNull CommandSender sender) {
        return sender instanceof Player player && this.isHolder(player.getUniqueId());
    }
    
    public boolean isHolder(@NonNull OfflinePlayer player) {
        return this.isHolder(player.getUniqueId());
    }
    
    public boolean isHolder(@NonNull UUID playerId) {
        return this.uuid.equals(playerId);
    }

    @NonNull
    public final OfflinePlayer offlinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    @NonNull
    public final Optional<Player> player() {
        return Optional.ofNullable(Bukkit.getPlayer(this.uuid));
    }

    @NonNull
    public final UUID getId() {
        return this.uuid;
    }

    @NonNull
    @Deprecated
    public final String getIdString() {
        return this.uuid.toString();
    }

    @NonNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NonNull String name) {
        this.name = name;
    }
}
