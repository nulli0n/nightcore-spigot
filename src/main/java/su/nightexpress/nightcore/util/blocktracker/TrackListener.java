package su.nightexpress.nightcore.util.blocktracker;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.manager.AbstractListener;

public class TrackListener<P extends NightCorePlugin> extends AbstractListener<P> {

    private static final String META_TRACK_FALLING_BLOCK = "tracker_falling_block";

    public TrackListener(@NotNull P plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLoad(WorldLoadEvent event) {
        PlayerBlockTracker.initWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUnload(WorldUnloadEvent event) {
        PlayerBlockTracker.terminateWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLoad(ChunkLoadEvent event) {
        PlayerBlockTracker.initChunk(event.getChunk());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUnload(ChunkUnloadEvent event) {
        PlayerBlockTracker.terminateChunk(event.getChunk());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        PlayerBlockTracker.track(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        PlayerBlockTracker.unTrack(event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        PlayerBlockTracker.unTrack(event.blockList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        if (event.getBlock().getBlockData() instanceof Lightable) return;
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onForm(BlockFormEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFlow(BlockFromToEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrow(BlockGrowEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent e) {
        if (e.getPlayer() == null) return;
        PlayerBlockTracker.track(e.getBlocks().stream().map(BlockState::getBlock).toList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMultiPlace(BlockMultiPlaceEvent event) {
        PlayerBlockTracker.track(event.getReplacedBlockStates().stream().map(BlockState::getBlock).toList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        PlayerBlockTracker.shift(event.getDirection(), event.getBlocks());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isSticky()) {
            return;
        }
        PlayerBlockTracker.shift(event.getDirection(), event.getBlocks());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpread(BlockSpreadEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityForm(EntityBlockFormEvent event) {
        PlayerBlockTracker.unTrack(event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityFallingSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof FallingBlock)) return;

        Block block = entity.getLocation().getBlock();
        if (!PlayerBlockTracker.isTracked(block)) return;

        entity.setMetadata(META_TRACK_FALLING_BLOCK, new FixedMetadataValue(plugin, true));
        PlayerBlockTracker.unTrack(block);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityFallingLand(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (!entity.hasMetadata(META_TRACK_FALLING_BLOCK)) return;

        PlayerBlockTracker.trackForce(event.getBlock());
    }
}
