package su.nightexpress.nightcore.util.geodata.pos;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.Objects;

public class BlockPos implements Writeable {

    private final int x,y,z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public static BlockPos read(@NotNull FileConfig config, @NotNull String path) {
        String str = config.getString(path, "");
        return deserialize(str);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    @NotNull
    public String serialize() {
        return this.x + "," + this.y + "," + this.z;
    }

    @NotNull
    public static BlockPos deserialize(@NotNull String str) {
        String[] split = str.split(",");
        if (split.length < 3) return empty();

        int x = NumberUtil.getAnyInteger(split[0], 0);
        int y = NumberUtil.getAnyInteger(split[1], 0);
        int z = NumberUtil.getAnyInteger(split[2], 0);

        return new BlockPos(x, y, z);
    }



    public boolean isEmpty() {
        return this.x == 0 && this.y == 0 && this.z == 0;
    }

    @NotNull
    public static BlockPos empty() {
        return new BlockPos(0, 0, 0);
    }

    @NotNull
    public static BlockPos from(@NotNull Block block) {
        return new BlockPos(block.getX(), block.getY(), block.getZ());
    }

    @NotNull
    public static BlockPos from(@NotNull Location location) {
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    public static BlockPos from(@NotNull ExactPos pos) {
        return new BlockPos((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
    }



    @NotNull
    public Block toBlock(@NotNull World world) {
        return world.getBlockAt(this.x, this.y, this.z);
    }

    @NotNull
    public Location toLocation(@NotNull World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    @NotNull
    public ChunkPos toChunkPos() {
        return ChunkPos.from(this);
    }

    @NotNull
    public Chunk toChunk(@NotNull World world) {
        return this.toChunkPos().getChunk(world);
    }

    public boolean isChunkLoaded(@NotNull World world) {
        return this.toChunkPos().isLoaded(world);
    }



    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    @NotNull
    public BlockPos copy() {
        return new BlockPos(this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BlockPos other)) return false;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "BlockPos{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            '}';
    }
}
