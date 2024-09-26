package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.stream.Stream;

public enum Version {

    V1_18_R2("1.18.2", Status.DROPPED),
    V1_19_R3("1.19.4"),
    V1_20_R1("1.20.1", Status.OUTDATED),
    V1_20_R2("1.20.2", Status.OUTDATED),
    V1_20_R3("1.20.4"),
    MC_1_20_6("1.20.6", Status.DROPPED),
    MC_1_21_0("1.21", Status.OUTDATED),
    MC_1_21("1.21.1"),
    UNKNOWN("Unknown"),
    ;

    public static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    private static Version current;

    private final Status status;
    private final String  localized;

    Version(@NotNull String localized) {
        this(localized, Status.SUPPORTED);
    }

    Version(@NotNull String localized, @NotNull Status status) {
        this.localized = localized;
        this.status = status;
    }

    @NotNull
    @Deprecated
    public static String getProtocol() {
        return Bukkit.getServer().getBukkitVersion();
    }

    public enum Status {
        SUPPORTED,
        OUTDATED,
        DROPPED
    }

    @NotNull
    public static Version getCurrent() {
        if (current == null) {
            String protocol = Bukkit.getServer().getBukkitVersion();
            String exact = protocol.split("-")[0];

            current = Stream.of(values()).sorted(Comparator.reverseOrder()).filter(version -> exact.equalsIgnoreCase(version.getLocalized())).findFirst().orElse(UNKNOWN);
        }
        return current;
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
        return status;
    }

    @NotNull
    public String getLocalized() {
        return localized;
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
