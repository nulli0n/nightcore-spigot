package su.nightexpress.nightcore.bridge.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PaperProfile implements NightProfile {

    private final PlayerProfile backend;

    public PaperProfile(@NotNull PlayerProfile backend) {
        this.backend = backend;
    }

    @Override
    public void apply(@NotNull SkullMeta meta) {
        meta.setPlayerProfile(this.backend);
    }

    @Override
    @Nullable
    public UUID getId() {
        return this.backend.getId();
    }

    @Override
    @NotNull
    public Optional<UUID> id() {
        return Optional.ofNullable(this.getId());
    }

    @Override
    @Nullable
    public String getName() {
        return this.backend.getName();
    }

    @Override
    @NotNull
    public Optional<String> name() {
        return Optional.ofNullable(this.getName());
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
        return this.backend.update().thenApplyAsync(PaperProfile::new);
    }

    @Override
    public String toString() {
        return "PaperProfile{" +
            "backend=" + backend +
            '}';
    }
}
