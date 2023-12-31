package su.nightexpress.nightcore.util.blocktracker;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Plugins;

import java.util.*;
import java.util.function.Predicate;

public final class PlayerBlockTracker {

    public static final  Set<Predicate<Block>>   BLOCK_FILTERS     = new HashSet<>();
    public static final  NamespacedKey           TRACKED_DATA_KEY  = NamespacedKey.minecraft("tracked_chunk_data");
    private static final Map<UUID, TrackedWorld> TRACKED_WORLD_MAP = new Object2ObjectOpenHashMap<>();

    private static TrackListener<?> listener;

    public static void initialize() {
        if (listener == null) {
            initCurrentlyLoadedWorlds();
            (listener = new TrackListener<>(Plugins.CORE)).registerListeners();
        }
    }

    public static void shutdown() {
        if (listener != null) {
            terminateCurrentlyLoadedWorlds();
            listener.unregisterListeners();
            listener = null;
            BLOCK_FILTERS.clear();
        }
    }

    public static void initWorld(@NotNull World world) {
        TrackedWorld trackedWorld = new TrackedWorld();
        for (Chunk loadedChunk : world.getLoadedChunks()) {
            trackedWorld.initChunk(loadedChunk);
        }
        TRACKED_WORLD_MAP.put(world.getUID(), trackedWorld);
    }

    public static void terminateWorld(@NotNull World world) {
        TrackedWorld trackedWorld = TRACKED_WORLD_MAP.remove(world.getUID());
        if (trackedWorld == null) {
            return;
        }
        for (Chunk loadedChunk : world.getLoadedChunks()) {
            trackedWorld.terminateChunk(loadedChunk);
        }
    }

    public static void initChunk(@NotNull Chunk chunk) {
        TrackedWorld trackedWorld = getTrackedWorldOf(chunk);
        if (trackedWorld == null) {
            return;
        }
        trackedWorld.initChunk(chunk);
    }

    public static void terminateChunk(@NotNull Chunk chunk) {
        TrackedWorld trackedWorld = getTrackedWorldOf(chunk);
        if (trackedWorld == null) {
            return;
        }
        trackedWorld.terminateChunk(chunk);
    }

    public static void initCurrentlyLoadedWorlds() {
        Bukkit.getWorlds().forEach(PlayerBlockTracker::initWorld);
    }

    public static void terminateCurrentlyLoadedWorlds() {
        Bukkit.getWorlds().forEach(PlayerBlockTracker::terminateWorld);
    }

    public static boolean isTracked(@NotNull Block block) {
        TrackedWorld trackedWorld = getTrackedWorldOf(block);
        if (trackedWorld == null) {
            return false;
        }
        return trackedWorld.isTracked(block);
    }

    public static void track(@NotNull Block block) {
        if (BLOCK_FILTERS.stream().noneMatch(filter -> filter.test(block))) return;
        trackForce(block);
    }

    public static void trackForce(@NotNull Block block) {
        TrackedWorld trackedWorld = getTrackedWorldOf(block);
        if (trackedWorld == null) {
            return;
        }
        trackedWorld.add(block);
    }

    public static void unTrack(@NotNull Block block) {
        TrackedWorld trackedWorld = getTrackedWorldOf(block);
        if (trackedWorld == null) {
            return;
        }
        trackedWorld.remove(block);
    }

    public static void track(@NotNull Collection<Block> trackedBlocks) {
        trackedBlocks.forEach(PlayerBlockTracker::trackForce);
    }

    public static void unTrack(@NotNull Collection<Block> trackedBlocks) {
        trackedBlocks.forEach(PlayerBlockTracker::unTrack);
    }

    public static void shift(@NotNull BlockFace direction, @NotNull List<Block> blocks) {
        unTrack(blocks);
        track(blocks.stream().map(block -> block.getRelative(direction)).toList());
    }

    public static void move(@NotNull Block from, @NotNull Block to) {
        unTrack(from);
        track(to);
    }

    @Nullable
    private static TrackedWorld getTrackedWorldOf(@NotNull Block block) {
        return TRACKED_WORLD_MAP.get(block.getWorld().getUID());
    }

    @Nullable
    private static TrackedWorld getTrackedWorldOf(@NotNull Chunk chunk) {
        return TRACKED_WORLD_MAP.get(chunk.getWorld().getUID());
    }
}
