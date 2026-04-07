package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Version {

    @Deprecated V1_19_R3("1.19.4", 3337, Status.DROPPED),
    @Deprecated V1_20_R1("1.20.1", 3465, Status.DROPPED),
    @Deprecated V1_20_R2("1.20.2", 3578, Status.DROPPED),
    @Deprecated V1_20_R3("1.20.4", 3700, Status.DROPPED),
    @Deprecated MC_1_20_6("1.20.6", 3839, Status.DROPPED),
    @Deprecated MC_1_21_0("1.21", 3953, Status.DROPPED),
    @Deprecated MC_1_21("1.21.1", 3955, Status.DROPPED),
    @Deprecated MC_1_21_2("1.21.2", 4080, Status.DROPPED),
    @Deprecated MC_1_21_3("1.21.3", 4082, Status.DROPPED),
    @Deprecated MC_1_21_4("1.21.4", 4189, Status.DROPPED),
    @Deprecated MC_1_21_5("1.21.5", 4325, Status.DROPPED),
    @Deprecated MC_1_21_6("1.21.6", 4435, Status.DROPPED),
    @Deprecated MC_1_21_7("1.21.7", 4438, Status.DROPPED),
    MC_1_21_8("1.21.8", 4440),
    MC_1_21_9("1.21.9", 4554, Status.OUTDATED),
    MC_1_21_10("1.21.10", 4556, Status.OUTDATED),
    MC_1_21_11("1.21.11", 4671),
    MC_26_1("26.1", 4786, Status.DROPPED),
    MC_26_1_1("26.1.1", 4788),
    UNKNOWN("Unknown", 10000),
    ;

    private static final boolean isPaper = checkPaper();
    private static final boolean isFolia = checkFolia();

    private static Version current;
    private final Status status;
    private final int    dataVersion;
    private final String localized;

    Version(@NonNull String localized, int dataVersion) {
        this(localized, dataVersion, Status.SUPPORTED);
    }

    Version(@NonNull String localized, int dataVersion, @NonNull Status status) {
        this.localized = localized;
        this.dataVersion = dataVersion;
        this.status = status;
    }

    public enum Status {
        SUPPORTED,
        OUTDATED,
        DROPPED
    }

    @NonNull
    public static Version detect() {
        String bukkitVersion = isSpigot() ? Bukkit.getServer().getBukkitVersion() : Bukkit.getServer().getVersion();
        String exact = bukkitVersion.split("-")[0];

        current = Stream.of(values()).sorted(Comparator.reverseOrder()).filter(version -> exact.equalsIgnoreCase(version.getLocalized())).findFirst().orElse(UNKNOWN);

        return current;
    }

    @NonNull
    public static Version getCurrent() {
        if (current == null) throw new IllegalStateException("Version is not initialized!");

        return current;
    }

    @Deprecated
    public static boolean withDialogs() {
        return true;
    }

    public static boolean withCopperAge() {
        return isAtLeast(MC_1_21_9);
    }

    public static boolean isSpigot() {
        return !isPaper;
    }

    public static boolean isPaper() {
        return isPaper;
    }

    public static boolean isFolia() {
        return isFolia;
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

    @NonNull
    public Status getStatus() {
        return this.status;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }

    @NonNull
    public String getLocalized() {
        return this.localized;
    }

    public boolean isLower(@NonNull Version version) {
        return this.ordinal() < version.ordinal();
    }

    public boolean isHigher(@NonNull Version version) {
        return this.ordinal() > version.ordinal();
    }

    public static boolean isAtLeast(@NonNull Version version) {
        return version.isCurrent() || getCurrent().isHigher(version);
    }

    public static boolean isAbove(@NonNull Version version) {
        return getCurrent().isHigher(version);
    }

    public static boolean isBehind(@NonNull Version version) {
        return getCurrent().isLower(version);
    }

    public static boolean isBehindOrEqual(@NonNull Version version){
        return getCurrent().isLower(version) || getCurrent() == version;
    }

    public boolean isCurrent() {
        return this == Version.getCurrent();
    }

    private static boolean checkPaper() {
        return Reflex.classExists("com.destroystokyo.paper.ParticleBuilder");
    }

    private static boolean checkFolia() {
        return Reflex.classExists("io.papermc.paper.threadedregions.RegionizedServer");
    }
}
