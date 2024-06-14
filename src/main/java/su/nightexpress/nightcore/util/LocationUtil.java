package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class LocationUtil {

    @Nullable
    public static String serialize(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) return null;

        return location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw() + "," + world.getName();
    }

    @NotNull
    public static List<String> serialize(@NotNull Collection<Location> list) {
        return new ArrayList<>(list.stream().map(LocationUtil::serialize).filter(Objects::nonNull).toList());
    }

    @Nullable
    public static Location deserialize(@NotNull String raw) {
        String[] split = raw.split(",");
        if (split.length != 6) return null;

        World world = Bukkit.getWorld(split[5]);
        if (world == null) {
            Plugins.CORE.error("Invalid/Unloaded world for: '" + raw + "' location!");
            return null;
        }

        double x = NumberUtil.getAnyDouble(split[0], 0);
        double y = NumberUtil.getAnyDouble(split[1], 0);
        double z = NumberUtil.getAnyDouble(split[2], 0);
        float pitch = (float) NumberUtil.getAnyDouble(split[3], 0);
        float yaw = (float) NumberUtil.getAnyDouble(split[4], 0);

        return new Location(world, x, y, z, yaw, pitch);
    }

    @NotNull
    public static List<Location> deserialize(@NotNull Collection<String> list) {
        return new ArrayList<>(list.stream().map(LocationUtil::deserialize).filter(Objects::nonNull).toList());
    }

    @NotNull
    public static String getWorldName(@NotNull Location location) {
        World world = location.getWorld();
        return world == null ? "null" : world.getName();
    }

    @NotNull
    @Deprecated
    public static Location getCenter(@NotNull Location location) {
        return setCenter3D(location);
    }

    @NotNull
    @Deprecated
    public static Location getCenter(@NotNull Location location, boolean doVertical) {
        return setCenter(location, doVertical);
    }

    @NotNull
    public static Location setCenter2D(@NotNull Location location) {
        return setCenter(location, false);
    }

    @NotNull
    public static Location setCenter3D(@NotNull Location location) {
        return setCenter(location, true);
    }

    @NotNull
    public static Location setCenter(@NotNull Location location, boolean is3D) {
        //Location centered = location.clone();
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + (is3D ? 0.5 : 0));
        location.setZ(location.getBlockZ() + 0.5);
        return location;
    }

    @NotNull
    public static Vector getDirection(@NotNull Location from, @NotNull Location to) {
        Location origin = from.clone();
        origin.setDirection(to.toVector().subtract(origin.toVector()));
        return origin.getDirection();
    }
}
