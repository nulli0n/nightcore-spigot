package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.bridge.PaperBridge;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.SpigotBridge;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Version {

    V1_19_R3("1.19.4", Status.OUTDATED),
    V1_20_R1("1.20.1", Status.OUTDATED),
    V1_20_R2("1.20.2", Status.OUTDATED),
    V1_20_R3("1.20.4", Status.OUTDATED),
    MC_1_20_6("1.20.6", Status.OUTDATED),
    MC_1_21_0("1.21", Status.OUTDATED),
    MC_1_21("1.21.1"),
    MC_1_21_2("1.21.2", Status.OUTDATED),
    MC_1_21_3("1.21.3"),
    MC_1_21_4("1.21.4"),
    UNKNOWN("Unknown"),
    ;

    public static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    private static Version current;
    private static Software software;

    private final Status status;
    private final String localized;

    Version(@NotNull String localized) {
        this(localized, Status.SUPPORTED);
    }

    Version(@NotNull String localized, @NotNull Status status) {
        this.localized = localized;
        this.status = status;
    }

    public enum Status {
        SUPPORTED,
        OUTDATED,
        DROPPED
    }

    public static void load(@NotNull NightCore core) {
        String bukkitVersion = core.getServer().getBukkitVersion();
        String bukkitName = core.getServer().getName();
        String exact = bukkitVersion.split("-")[0];
        boolean isSpigot = bukkitName.equalsIgnoreCase("Spigot") || bukkitName.equalsIgnoreCase("CraftBukkit");

        current = Stream.of(values()).sorted(Comparator.reverseOrder()).filter(version -> exact.equalsIgnoreCase(version.getLocalized())).findFirst().orElse(UNKNOWN);
        software = isSpigot ? new SpigotBridge() : new PaperBridge();
        software.initialize(core);
        core.info("Server version detected as " + bukkitName + " " + current.getLocalized() + ". Using " + software().getName() + ".");

        loadComponents(core);
    }

    private static void loadComponents(@NotNull NightCore core) {
        boolean nbtLoad = ItemNbt.load(core);
        if (!nbtLoad) {
            core.error("Could not load Item NBT Compressor.");
            return;
        }

        if (!ItemNbt.test()) {
            core.error("Failed test of Item NBT Compressor.");
        }
    }

    public static void printCaution(@NotNull NightCorePlugin plugin) {
        if (current != UNKNOWN && current.isSupported()) return;

        plugin.warn("=".repeat(35));

        if (current == Version.UNKNOWN) {
            plugin.warn("WARNING: This plugin is not supposed to run on this server version!");
            plugin.warn("If server version is newer than " + values()[values().length - 1] + ", then wait for an update please.");
            plugin.warn("Otherwise this plugin will not work properly or even load.");
        }
        else if (current.isDeprecated()) {
            plugin.warn("WARNING: You're running an outdated server version (" + current.getLocalized() + ")!");
            plugin.warn("This version will no longer be supported in future relases.");
            plugin.warn("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }
        else if (current.isDropped()) {
            plugin.error("ERROR: You're running an unsupported server version (" + current.getLocalized() + ")!");
            plugin.error("Please upgrade your server to " + Lists.next(current, (Version::isSupported)).getLocalized() + ".");
        }

        plugin.warn("ABSOLUTELY NO DISCORD SUPPORT WILL BE PROVIDED");
        plugin.warn("=".repeat(35));
    }

    @NotNull
    public static Software software() {
        return software;
    }

    @NotNull
    public static Version getCurrent() {
        return current;
    }

    public static boolean isSpigot() {
        return software.isSpigot();
    }

    public static boolean isPaper() {
        return software.isPaper();
    }

    public boolean isDeprecated() {
        return this.status == Status.OUTDATED;
    }

    public boolean isDropped() {
        return this.status == Status.DROPPED;
    }

    public boolean isSupported() {
        return this.status == Status.SUPPORTED;
    }

    @NotNull
    public Status getStatus() {
        return this.status;
    }

    @NotNull
    public String getLocalized() {
        return this.localized;
    }

    public boolean isLower(@NotNull Version version) {
        return this.ordinal() < version.ordinal();
    }

    public boolean isHigher(@NotNull Version version) {
        return this.ordinal() > version.ordinal();
    }

    public static boolean isAtLeast(@NotNull Version version) {
        return version.isCurrent() || getCurrent().isHigher(version);
    }

    public static boolean isAbove(@NotNull Version version) {
        return getCurrent().isHigher(version);
    }

    public static boolean isBehind(@NotNull Version version) {
        return getCurrent().isLower(version);
    }

    public boolean isCurrent() {
        return this == Version.getCurrent();
    }
}
