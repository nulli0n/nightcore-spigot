package su.nightexpress.nightcore.util.geodata;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.Objects;

public class ChunkPos {

    private final int x,z;

    public ChunkPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @NotNull
    public static ChunkPos empty() {
        return new ChunkPos(0, 0);
    }

    @NotNull
    public static ChunkPos from(@NotNull Location location) {
        return from(location.getBlockX(), location.getBlockZ());
    }

    @NotNull
    public static ChunkPos from(@NotNull Block block) {
        return from(block.getX(), block.getZ());
    }

    @NotNull
    public static ChunkPos from(@NotNull BlockPos blockPos) {
        return from(blockPos.getX(), blockPos.getZ());
    }

    @NotNull
    public static ChunkPos from(int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        return new ChunkPos(chunkX, chunkZ);
    }

    @NotNull
    public static ChunkPos from(@NotNull ChunkSnapshot snapshot) {
        return new ChunkPos(snapshot.getX(), snapshot.getZ());
    }

    @NotNull
    public static ChunkPos from(@NotNull Chunk chunk) {
        return new ChunkPos(chunk.getX(), chunk.getZ());
    }

    @NotNull
    public static ChunkPos read(@NotNull FileConfig config, @NotNull String path) {
        String str = config.getString(path, "");
        return deserialize(str);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    @NotNull
    public static ChunkPos deserialize(@NotNull String str) {
        String[] split = str.split(",");
        if (split.length < 2) return empty();

        int x = NumberUtil.getAnyInteger(split[0], 0);
        int z = NumberUtil.getAnyInteger(split[1], 0);

        return new ChunkPos(x, z);
    }

    public boolean isLoaded(@NotNull World world) {
        return world.isChunkLoaded(this.x, this.z);
    }

    @NotNull
    public Chunk getChunk(@NotNull World world) {
        return world.getChunkAt(this.x, this.z, false);
    }

    @NotNull
    public String serialize() {
        return this.x + "," + this.z;
    }

    @NotNull
    public ChunkPos copy() {
        return new ChunkPos(this.x, this.z);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
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
