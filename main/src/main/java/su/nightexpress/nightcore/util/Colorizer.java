package su.nightexpress.nightcore.util;

import net.md_5.bungee.api.ChatColor;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.regex.TimedMatcher;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Deprecated
public class Colorizer {

    @Deprecated
    public static final Pattern PATTERN_HEX = Pattern.compile("#([A-Fa-f0-9]{6})");

    @NonNull
    @Deprecated
    public static String apply(@NonNull String str) {
        return hex(legacy(str));
    }

    @NonNull
    @Deprecated
    public static List<String> apply(@NonNull List<String> list) {
        list.replaceAll(Colorizer::apply);
        return list;
    }

    @NonNull
    @Deprecated
    public static Set<String> apply(@NonNull Set<String> set) {
        return set.stream().map(Colorizer::apply).collect(Collectors.toSet());
    }

    @NonNull
    @Deprecated
    public static String legacyHex(@NonNull String str) {
        return hex(legacy(str));
    }

    @NonNull
    @Deprecated
    public static String legacy(@NonNull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @NonNull
    @Deprecated
    public static String hex(@NonNull String str) {
        TimedMatcher timedMatcher = TimedMatcher.create(PATTERN_HEX, str);
        //Matcher matcher = PATTERN_HEX.matcher(str);
        StringBuilder buffer = new StringBuilder(str.length() + 4 * 8);
        while (timedMatcher.find()) {
            Matcher matcher = timedMatcher.getMatcher();
            String group = matcher.group(1);
            matcher.appendReplacement(buffer,
                ChatColor.COLOR_CHAR + "x" + ChatColor.COLOR_CHAR + group.charAt(0) +
                    ChatColor.COLOR_CHAR + group.charAt(1) + ChatColor.COLOR_CHAR + group.charAt(2) +
                    ChatColor.COLOR_CHAR + group.charAt(3) + ChatColor.COLOR_CHAR + group.charAt(4) +
                    ChatColor.COLOR_CHAR + group.charAt(5));
        }
        return timedMatcher.getMatcher().appendTail(buffer).toString();
    }

    @NonNull
    @Deprecated
    public static String plain(@NonNull String str) {
        return LegacyColors.plainColors(str);
        //return plainLegacy(plainHex(str));
    }

    @NonNull
    @Deprecated
    public static String plainLegacy(@NonNull String str) {
        return LegacyColors.plainDefaultColors(str);
        //return str.replace(ChatColor.COLOR_CHAR, '&');
    }

    @NonNull
    @Deprecated
    public static String plainHex(@NonNull String str) {
        return LegacyColors.plainHEXColors(str);
        //        StringBuilder builder = new StringBuilder(str);
        //
        //        int index;
        //        while ((index = builder.toString().indexOf(ChatColor.COLOR_CHAR + "x")) >= 0) {
        //            int count = 0;
        //            builder.replace(index, index + 2, "#");
        //
        //            for (int point = index + 1; count < 6; point += 1) {
        //                builder.deleteCharAt(point);
        //                count++;
        //            }
        //        }
        //
        //        return builder.toString();
    }

    @NonNull
    @Deprecated
    public static String strip(@NonNull String str) {
        String stripped = ChatColor.stripColor(str);
        return stripped == null ? "" : stripped;
    }

    @NonNull
    @Deprecated
    public static String restrip(@NonNull String str) {
        return strip(apply(str));
    }
}
