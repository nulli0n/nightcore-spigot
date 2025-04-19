package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.spigot.SpigotBridge;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Version {

    V1_19_R3("1.19.4", 3337, Status.OUTDATED),
    V1_20_R1("1.20.1", 3465, Status.OUTDATED),
    V1_20_R2("1.20.2", 3578, Status.OUTDATED),
    V1_20_R3("1.20.4", 3700, Status.OUTDATED),
    MC_1_20_6("1.20.6", 3839, Status.OUTDATED),
    MC_1_21_0("1.21", 3953, Status.OUTDATED),
    MC_1_21("1.21.1", 3955),
    MC_1_21_2("1.21.2", 4080, Status.OUTDATED),
    MC_1_21_3("1.21.3", 4082),
    MC_1_21_4("1.21.4", 4189),
    MC_1_21_5("1.21.5", 4325),
    UNKNOWN("Unknown", 0),
    ;

    public static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    private static Version current;
    private static Software software;

    private final Status status;
    private final int dataVersion;
    private final String localized;

    Version(@NotNull String localized, int dataVersion) {
        this(localized, dataVersion, Status.SUPPORTED);
    }

    Version(@NotNull String localized, int dataVersion, @NotNull Status status) {
        this.localized = localized;
        this.dataVersion = dataVersion;
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

        ItemNbt.load(core);
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

    public int getDataVersion() {
        return this.dataVersion;
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

    public static boolean isBehindOrEqual(@NotNull Version version){
        return getCurrent().isLower(version) || getCurrent() == version;
    }

    public boolean isCurrent() {
        return this == Version.getCurrent();
    }
}
