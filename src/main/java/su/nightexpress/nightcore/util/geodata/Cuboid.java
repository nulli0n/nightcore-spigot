package su.nightexpress.nightcore.util.geodata;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

import java.util.*;
import java.util.stream.Collectors;

public class Cuboid {

    private final BlockPos min;
    private final BlockPos max;
    private final BlockPos center;

    private final BlockPos minUp;
    private final BlockPos maxDown;

    private final BlockPos zUp;
    private final BlockPos zDown;

    private final BlockPos xDown;
    private final BlockPos xUp;

    private final boolean empty;

    private final Set<ChunkPos> intersectingChunks;

    public Cuboid(@NotNull BlockPos min, @NotNull BlockPos max) {
        int minX = Math.min(min.getX(), max.getX());
        int minY = Math.min(min.getY(), max.getY());
        int minZ = Math.min(min.getZ(), max.getZ());

        int maxX = Math.max(min.getX(), max.getX());
        int maxY = Math.max(min.getY(), max.getY());
        int maxZ = Math.max(min.getZ(), max.getZ());

        this.min = new BlockPos(minX, minY, minZ);
        this.max = new BlockPos(maxX, maxY, maxZ);

        int cx = (int) (minX + (maxX - minX) / 2D);
        int cy = (int) (minY + (maxY - minY) / 2D);
        int cz = (int) (minZ + (maxZ - minZ) / 2D);

        this.center = new BlockPos(cx, cy, cz);

        this.empty = this.min.isEmpty() && this.max.isEmpty();

        minUp = new BlockPos(this.min.getX(), this.max.getY(), this.min.getZ());
        maxDown = new BlockPos(this.max.getX(), this.min.getY(), this.max.getZ());

        zUp = new BlockPos(this.min.getX(), this.max.getY(), this.max.getZ());
        zDown = new BlockPos(this.min.getX(), this.min.getY(), this.max.getZ());

        xDown = new BlockPos(this.max.getX(), this.min.getY(), this.min.getZ());
        xUp = new BlockPos(this.max.getX(), this.max.getY(), this.min.getZ());

        this.intersectingChunks = new HashSet<>(this.getIntersectingChunks());
    }

    public boolean isSimilar(@NotNull Cuboid other) {
        if (this.isEmpty() || other.isEmpty()) return false;

        return this.min.equals(other.min) && this.max.equals(other.max);
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean contains(@NotNull Location location) {
        return this.contains(location, DimensionType._3D);
    }

    public boolean contains(@NotNull Location location, @NotNull DimensionType type) {
        return this.contains(BlockPos.from(location), type);
    }

    public boolean contains(@NotNull ExactPos pos) {
        return this.contains(pos.toBlockPos());
    }

    public boolean contains(@NotNull BlockPos pos) {
        return this.contains(pos, DimensionType._3D);
    }

    public boolean contains(@NotNull ChunkPos pos) {
        return this.containsX(GeoUtils.shiftToCoord(pos.getX())) && this.containsZ(GeoUtils.shiftToCoord(pos.getZ()));
    }

    public boolean contains(@NotNull BlockPos pos, @NotNull DimensionType type) {
        if (!this.containsX(pos.getX())) return false;
        if (!this.containsZ(pos.getZ())) return false;

        if (type == DimensionType._3D) {
            return this.containsY(pos.getY());
        }
        return true;
    }

    public boolean containsX(int x) {
        return x >= this.min.getX() && x <= this.max.getX();
    }

    public boolean containsY(int y) {
        return y >= this.min.getY() && y <= this.max.getY();
    }

    public boolean containsZ(int z) {
        return z >= this.min.getZ() && z <= this.max.getZ();
    }

    @NotNull
    public List<Block> getBlocks(@NotNull World world) {
        List<Block> blocks = new ArrayList<>();

        for (int x = this.min.getX(); x <= this.max.getX(); x++) {
            for (int y = this.min.getY(); y <= this.max.getY(); y++) {
                for (int z = this.min.getZ(); z <= this.max.getZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    @NotNull
    public List<BlockPos> getCorners() {
        List<BlockPos> list = new ArrayList<>();

        list.add(this.min);
        list.add(this.max);
        list.add(minUp);
        list.add(maxDown);
        list.add(zUp);
        list.add(zDown);
        list.add(xUp);
        list.add(xDown);

        return list;
    }

    @NotNull
    public List<BlockPos> getCornerWiresY() {
        List<BlockPos> list = new ArrayList<>();

        for (int y = this.min.getY() + 1; y < this.minUp.getY(); y++) {
            list.add(new BlockPos(this.min.getX(), y, this.min.getZ()));
        }

        for (int y = this.max.getY() - 1; y > this.maxDown.getY(); y--) {
            list.add(new BlockPos(this.max.getX(), y, this.max.getZ()));
        }

        for (int y = this.zDown.getY() + 1; y < this.zUp.getY(); y++) {
            list.add(new BlockPos(this.zDown.getX(), y, this.zDown.getZ()));
        }

        for (int y = this.xDown.getY() + 1; y < this.xUp.getY(); y++) {
            list.add(new BlockPos(this.xDown.getX(), y, this.xDown.getZ()));
        }

        return list;
    }

    @NotNull
    public List<BlockPos> getCornerWiresX() {
        List<BlockPos> list = new ArrayList<>();

        for (int x = this.min.getX() + 1; x < this.xDown.getX(); x++) {
            list.add(new BlockPos(x, this.min.getY(), this.min.getZ()));
            list.add(new BlockPos(x, this.minUp.getY(), this.minUp.getZ()));
        }
        for (int x = this.zDown.getX() + 1; x < this.max.getX(); x++) {
            list.add(new BlockPos(x, this.zDown.getY(), this.zDown.getZ()));
            list.add(new BlockPos(x, this.zUp.getY(), this.zUp.getZ()));
        }

        return list;
    }

    @NotNull
    public List<BlockPos> getCornerWiresZ() {
        List<BlockPos> list = new ArrayList<>();

        for (int z = this.min.getZ() + 1; z < this.zDown.getZ(); z++) {
            list.add(new BlockPos(this.min.getX(), this.min.getY(), z));
            list.add(new BlockPos(this.min.getX(), this.minUp.getY(), z));
        }
        for (int z = this.xDown.getZ() + 1; z < this.max.getZ(); z++) {
            list.add(new BlockPos(this.xDown.getX(), this.xDown.getY(), z));
            list.add(new BlockPos(this.xUp.getX(), this.xUp.getY(), z));
        }

        return list;
    }

    public boolean isIntersectingWith(@NotNull Cuboid other) {
        return this.isIntersectingWith(other, DimensionType._3D);
    }

    public boolean isIntersectingWith(@NotNull Cuboid other, @NotNull DimensionType type) {
        return other.includedIn(this, type) || this.includedIn(other, type);
    }

    private boolean checkIntersect(float min1, float max1, float min2, float max2) {
        return min1 <= max2 && max1 >= min2;
    }

    public boolean includedIn(@NotNull Cuboid other, @NotNull DimensionType dimensionType) {
        if (!this.checkIntersect(this.min.getX(), this.max.getX(), other.getMin().getX(), other.getMax().getX())) return false;
        if (!this.checkIntersect(this.min.getZ(), this.max.getZ(), other.getMin().getZ(), other.getMax().getZ())) return false;

        if (dimensionType == DimensionType._3D) {
            return this.checkIntersect(this.min.getY(), this.max.getY(), other.getMin().getY(), other.getMax().getY());
        }

        return true;
    }

    @NotNull
    public Set<Chunk> getIntersectingChunks(@NotNull World world) {
        return this.getIntersectingChunkPositions().stream().map(pos -> pos.getChunk(world)).collect(Collectors.toSet());
    }

    @NotNull
    public Set<ChunkPos> getIntersectingChunkPositions() {
        return this.intersectingChunks;
    }

    @NotNull
    private Collection<ChunkPos> getIntersectingChunks() {
        List<ChunkPos> chunks = new ArrayList<>();
        if (this.isEmpty()) return chunks;

        int minX = GeoUtils.shiftToChunk(this.min.getX());
        int maxX = GeoUtils.shiftToChunk(this.max.getX());
        int minZ = GeoUtils.shiftToChunk(this.min.getZ());
        int maxZ = GeoUtils.shiftToChunk(this.max.getZ());

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                chunks.add(new ChunkPos(x, z));
            }
        }

        return chunks;
    }

    @NotNull
    public BlockPos getMin() {
        return this.min;
    }

    @NotNull
    public BlockPos getMax() {
        return this.max;
    }

    @NotNull
    public BlockPos getCenter() {
        return this.center;
    }

    public int getVolume() {
        int xLength = this.max.getX() - this.min.getX() + 1;
        int yLength = this.max.getY() - this.min.getY() + 1;
        int zLength = this.max.getZ() - this.min.getZ() + 1;

        return xLength * zLength * yLength;
    }
}
