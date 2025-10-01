package su.nightexpress.nightcore.bridge.wrap;

import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NightProfile {

    void apply(@NotNull SkullMeta meta);

    @Nullable UUID getId();

    @NotNull Optional<UUID> id();

    @Nullable String getName();

    @NotNull Optional<String> name();

    @NotNull PlayerTextures getTextures();

    void setTextures(@Nullable PlayerTextures textures);

    boolean isComplete();

    @NotNull CompletableFuture<NightProfile> update();
}
