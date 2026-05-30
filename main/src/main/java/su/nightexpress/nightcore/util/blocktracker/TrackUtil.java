package su.nightexpress.nightcore.util.blocktracker;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jspecify.annotations.NonNull;

public class TrackUtil {

    public static long getChunkKey(@NonNull Chunk chunk) {
        return getChunkKey(chunk.getX(), chunk.getZ());
    }

    public static long getChunkKey(final int chunkX, final int chunkZ) {
        return (long) chunkX & 0xFFFFFFFFL | ((long) chunkZ & 0xFFFFFFFFL) << 32;
    }

    public static long getChunkKeyOfBlock(@NonNull Block block) {
        return getChunkKey(block.getX() >> 4, block.getZ() >> 4);
    }

    public static int getRelativeChunkPosition(@NonNull Block block) {
        final int relX = (block.getX() % 16 + 16) % 16;
        final int relZ = (block.getZ() % 16 + 16) % 16;
        final int relY = block.getY();
        return (relY & 0xFFFF) | ((relX & 0xFF) << 16) | ((relZ & 0xFF) << 24);
    }

}