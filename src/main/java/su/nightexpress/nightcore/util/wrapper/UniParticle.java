package su.nightexpress.nightcore.util.wrapper;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

public class UniParticle {

    private final Particle particle;
    private final Object   data;

    public UniParticle(@Nullable Particle particle, @Nullable Object data) {
        this.particle = particle;
        this.data = data;
    }

    @NotNull
    public static UniParticle of(@Nullable Particle particle) {
        return UniParticle.of(particle, null);
    }

    @NotNull
    public static UniParticle of(@Nullable Particle particle, @Nullable Object data) {
        return new UniParticle(particle, data);
    }

    @NotNull
    public static UniParticle itemCrack(@NotNull ItemStack item) {
        return new UniParticle(Particle.ITEM_CRACK, new ItemStack(item));
    }

    @NotNull
    public static UniParticle itemCrack(@NotNull Material material) {
        return new UniParticle(Particle.ITEM_CRACK, new ItemStack(material));
    }

    @NotNull
    public static UniParticle blockCrack(@NotNull Material material) {
        return new UniParticle(Particle.BLOCK_CRACK, material.createBlockData());
    }

    @NotNull
    public static UniParticle blockDust(@NotNull Material material) {
        return new UniParticle(Particle.BLOCK_DUST, material.createBlockData());
    }

    @NotNull
    public static UniParticle blockMarker(@NotNull Material material) {
        return new UniParticle(Particle.BLOCK_MARKER, material.createBlockData());
    }

    @NotNull
    public static UniParticle fallingDust(@NotNull Material material) {
        return new UniParticle(Particle.FALLING_DUST, material.createBlockData());
    }

    @NotNull
    public static UniParticle redstone(@NotNull Color color, float size) {
        return new UniParticle(Particle.REDSTONE, new Particle.DustOptions(color, size));
    }

    @NotNull
    public static UniParticle read(@NotNull FileConfig cfg, @NotNull String path) {
        String name = cfg.getString(path + ".Name", "");
        Particle particle = StringUtil.getEnum(name, Particle.class).orElse(null);
        if (particle == null) return UniParticle.of(null);

        Class<?> dataType = particle.getDataType();
        Object data = null;
        if (dataType == BlockData.class) {
            Material material = Material.getMaterial(cfg.getString(path + ".Material", ""));
            data = material != null ? material.createBlockData() : Material.STONE.createBlockData();
        }
        else if (dataType == Particle.DustOptions.class) {
            Color color = StringUtil.getColor(cfg.getString(path + ".Color", ""));
            double size = cfg.getDouble(path + ".Size", 1D);
            data = new Particle.DustOptions(color, (float) size);
        }
        else if (dataType == Particle.DustTransition.class) {
            Color colorStart = StringUtil.getColor(cfg.getString(path + ".Color_From", ""));
            Color colorEnd = StringUtil.getColor(cfg.getString(path + ".Color_To", ""));
            double size = cfg.getDouble(path + ".Size", 1D);
            data = new Particle.DustTransition(colorStart, colorEnd, (float) size);
        }
        else if (dataType == ItemStack.class) {
            ItemStack item = cfg.getItem(path + ".Item");
            data = item.getType().isAir() ? new ItemStack(Material.STONE) : item;
        }
        else if (dataType == Float.class) {
            data = (float) cfg.getDouble(path + ".floatValue", 1F);
        }
        else if (dataType == Integer.class) {
            data = cfg.getInt(path + ".intValue", 1);
        }
        else if (dataType != Void.class) return UniParticle.of(Particle.REDSTONE);

        return UniParticle.of(particle, data);
    }

    public void write(@NotNull FileConfig cfg, @NotNull String path) {
        cfg.set(path + ".Name", this.isEmpty() ? "null" : this.getParticle().name());

        Object data = this.getData();
        if (data instanceof BlockData blockData) {
            cfg.set(path + ".Material", blockData.getMaterial().name());
        }
        else if (data instanceof Particle.DustTransition dustTransition) {
            Color colorStart = dustTransition.getColor();
            Color colorEnd = dustTransition.getToColor();
            cfg.set(path + ".Color_From", colorStart.getRed() + "," + colorStart.getGreen() + "," + colorStart.getBlue());
            cfg.set(path + ".Color_To", colorEnd.getRed() + "," + colorEnd.getGreen() + "," + colorEnd.getBlue());
            cfg.set(path + ".Size", dustTransition.getSize());
        }
        else if (data instanceof Particle.DustOptions dustOptions) {
            Color color = dustOptions.getColor();
            cfg.set(path + ".Color", color.getRed() + "," + color.getGreen() + "," + color.getBlue());
            cfg.set(path + ".Size", dustOptions.getSize());
        }
        else if (data instanceof ItemStack item) {
            cfg.setItem(path + ".Item", item);
        }
        else if (data instanceof Float f) {
            cfg.set(path + ".floatValue", f);
        }
        else if (data instanceof Integer i) {
            cfg.set(path + ".intValue", i);
        }
    }

    public boolean isEmpty() {
        return this.particle == null;
    }

    public Particle getParticle() {
        return particle;
    }

    @Nullable
    public Object getData() {
        return data;
    }

    public void play(@NotNull Location location, double speed, int amount) {
        this.play(location, 0D, speed, amount);
    }

    public void play(@NotNull Location location, double offsetAll, double speed, int amount) {
        this.play(location, offsetAll, offsetAll, offsetAll, speed, amount);
    }

    public void play(@NotNull Location location, double xOffset, double yOffset, double zOffset, double speed, int amount) {
        this.play(null, location, xOffset, yOffset, zOffset, speed, amount);
    }

    public void play(@NotNull Player player, @NotNull Location location, double speed, int amount) {
        this.play(player, location, 0D, speed, amount);
    }

    public void play(@NotNull Player player, @NotNull Location location, double offsetAll, double speed, int amount) {
        this.play(player, location, offsetAll, offsetAll, offsetAll, speed, amount);
    }

    public void play(@Nullable Player player, @NotNull Location location, double xOffset, double yOffset, double zOffset, double speed, int amount) {
        if (this.isEmpty()) return;
        if (this.particle == null || (this.particle.getDataType() != Void.class && this.data == null)) return;

        if (player == null) {
            World world = location.getWorld();
            if (world == null) return;

            world.spawnParticle(this.getParticle(), location, amount, xOffset, yOffset, zOffset, speed, this.getData());
            //EffectUtil.playParticle(location, this.getParticle(), this.getData(), xOffset, yOffset, zOffset, speed, amount);
        }
        else {
            player.spawnParticle(this.getParticle(), location, amount, xOffset, yOffset, zOffset, speed, this.getData());
            //EffectUtil.playParticle(player, location, this.getParticle(), this.getData(), xOffset, yOffset, zOffset, speed, amount);
        }
    }
}
