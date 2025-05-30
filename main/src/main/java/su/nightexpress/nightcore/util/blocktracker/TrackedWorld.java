package su.nightexpress.nightcore.util.blocktracker;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackedWorld {

    private final Long2ObjectMap<TrackedChunk> chunkMap;

    protected TrackedWorld() {
        this.chunkMap = new Long2ObjectOpenHashMap<>();
    }

    protected boolean isTracked(@NotNull Block block) {
        TrackedChunk trackedChunk = this.getChunkOf(block);
        return trackedChunk != null && trackedChunk.isTracked(block);
    }

    protected void add(@NotNull Block block) {
        TrackedChunk trackedChunk = this.getChunkOf(block);
        if (trackedChunk == null) return;

        trackedChunk.add(block);
    }

    protected void remove(@NotNull Block block) {
        TrackedChunk trackedChunk = this.getChunkOf(block);
        if (trackedChunk == null) return;

        trackedChunk.remove(block);
    }

    protected void initChunk(@NotNull Chunk chunk) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        TrackedChunk trackedChunk = new TrackedChunk(container);
        this.chunkMap.put(TrackUtil.getChunkKey(chunk), trackedChunk);
    }

    protected void terminateChunk(@NotNull Chunk chunk) {
        TrackedChunk trackedChunk = this.chunkMap.remove(TrackUtil.getChunkKey(chunk));
        if (trackedChunk == null) {
            return;
        }
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (trackedChunk.isEmpty()) {
            container.remove(PlayerBlockTracker.TRACKED_DATA_KEY);
        }
        else {
            trackedChunk.saveTo(container);
        }
    }

    @Nullable
    private TrackedChunk getChunkOf(@NotNull Block block) {
        return this.chunkMap.get(TrackUtil.getChunkKeyOfBlock(block));
    }
}