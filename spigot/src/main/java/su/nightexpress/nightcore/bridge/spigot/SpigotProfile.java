package su.nightexpress.nightcore.bridge.spigot;

import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpigotProfile implements NightProfile {

    private final PlayerProfile backend;

    public SpigotProfile(PlayerProfile backend) {
        this.backend = backend;
    }

    @Override
    public void apply(@NotNull SkullMeta meta) {
        meta.setOwnerProfile(this.backend);
    }

    @Override
    @Nullable
    public UUID getId() {
        return this.backend.getUniqueId();
    }

    @Override
    @Nullable
    public String getName() {
        return this.backend.getName();
    }

    @Override
    @NotNull
    public PlayerTextures getTextures() {
        return this.backend.getTextures();
    }

    @Override
    public void setTextures(@Nullable PlayerTextures textures) {
        this.backend.setTextures(textures);
    }

    @Override
    public boolean isComplete() {
        return this.backend.isComplete();
    }

    @Override
    @NotNull
    public CompletableFuture<NightProfile> update() {
        return this.backend.update().thenApplyAsync(SpigotProfile::new);
    }

    @Override
    public String toString() {
        return "SpigotProfile{" +
            "backend=" + backend +
            '}';
    }
}
