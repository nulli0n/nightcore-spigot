package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum Version {

    V1_18_R2("1.18.2"),
    V1_19_R3("1.19.4"),
    V1_20_R1("1.20.1", true),
    V1_20_R2("1.20.2", true),
    V1_20_R3("1.20.4"),
    UNKNOWN("Unknown"),
    ;

    public static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    private static Version current;

    private final boolean deprecated;
    private final String  localized;

    Version(@NotNull String localized) {
        this(localized, false);
    }

    Version(@NotNull String localized, boolean deprecated) {
        this.localized = localized;
        this.deprecated = deprecated;
    }

    @NotNull
    @Deprecated
    public static String getProtocol() {
        return Bukkit.getServer().getBukkitVersion();//.split("-")[0];

        //String[] split = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        //return split[split.length - 1];
    }

    @NotNull
    public static Version getCurrent() {
        if (current == null) {
            String protocol = Bukkit.getServer().getBukkitVersion();
            current = Stream.of(values()).filter(version -> protocol.startsWith(version.getLocalized())).findFirst().orElse(UNKNOWN);
            //current = StringUtil.getEnum(getProtocol(), Version.class).orElse(UNKNOWN);
        }
        return current;
    }

    public boolean isDeprecated() {
        return deprecated;
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
