package su.nightexpress.nightcore.util.geodata.pos;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.geodata.GeoUtils;

import java.util.Objects;

public class ChunkPos implements Writeable {

    private final int x;
    private final int z;

    public ChunkPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @NonNull
    public static ChunkPos read(@NonNull FileConfig config, @NonNull String path) {
        return deserialize(String.valueOf(config.getString(path)));
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path, this.serialize());
    }

    @NonNull
    public String serialize() {
        return this.x + "," + this.z;
    }

    @NonNull
    public static ChunkPos deserialize(@NonNull String str) {
        String[] split = str.split(",");
        if (split.length < 2) return empty();

        int x = NumberUtil.getAnyInteger(split[0], 0);
        int z = NumberUtil.getAnyInteger(split[1], 0);

        return new ChunkPos(x, z);
    }


    @NonNull
    public static ChunkPos empty() {
        return new ChunkPos(0, 0);
    }

    @NonNull
    public static ChunkPos from(@NonNull Location location) {
        return from(location.getBlockX(), location.getBlockZ());
    }

    @NonNull
    public static ChunkPos from(@NonNull Block block) {
        return from(block.getX(), block.getZ());
    }

    @NonNull
    public static ChunkPos from(@NonNull BlockPos blockPos) {
        return from(blockPos.getX(), blockPos.getZ());
    }

    @NonNull
    public static ChunkPos from(@NonNull ExactPos blockPos) {
        return from((int) blockPos.getX(), (int) blockPos.getZ());
    }

    @NonNull
    public static ChunkPos from(@NonNull ChunkSnapshot snapshot) {
        return new ChunkPos(snapshot.getX(), snapshot.getZ());
    }

    @NonNull
    public static ChunkPos from(@NonNull Chunk chunk) {
        return new ChunkPos(chunk.getX(), chunk.getZ());
    }

    @NonNull
    public static ChunkPos from(int x, int z) {
        int chunkX = GeoUtils.shiftToChunk(x);
        int chunkZ = GeoUtils.shiftToChunk(z);

        return new ChunkPos(chunkX, chunkZ);
    }


    public boolean isLoaded(@NonNull World world) {
        return world.isChunkLoaded(this.x, this.z);
    }

    @NonNull
    public Chunk getChunk(@NonNull World world) {
        return world.getChunkAt(this.x, this.z, false);
    }


    @NonNull
    public ChunkPos copy() {
        return new ChunkPos(this.x, this.z);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChunkPos other)) return false;
        return x == other.x && z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public String toString() {
        return "ChunkPos{" +
            "x=" + x +
            ", z=" + z +
            '}';
    }
}
