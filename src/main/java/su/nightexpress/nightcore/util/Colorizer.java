package su.nightexpress.nightcore.util;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.regex.TimedMatcher;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Deprecated
public class Colorizer {

    @Deprecated public static final Pattern PATTERN_HEX      = Pattern.compile("#([A-Fa-f0-9]{6})");

    @NotNull
    public static String apply(@NotNull String str) {
        return hex(legacy(str));
    }

    @NotNull
    public static List<String> apply(@NotNull List<String> list) {
        list.replaceAll(Colorizer::apply);
        return list;
    }

    @NotNull
    public static Set<String> apply(@NotNull Set<String> set) {
        return set.stream().map(Colorizer::apply).collect(Collectors.toSet());
    }

    @NotNull
    public static String legacyHex(@NotNull String str) {
        return hex(legacy(str));
    }

    @NotNull
    public static String legacy(@NotNull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @NotNull
    @Deprecated
    public static String hex(@NotNull String str) {
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

    @NotNull
    public static String plain(@NotNull String str) {
        return plainLegacy(plainHex(str));
    }

    @NotNull
    public static String plainLegacy(@NotNull String str) {
        return str.replace(ChatColor.COLOR_CHAR, '&');
    }

    @NotNull
    @Deprecated
    public static String plainHex(@NotNull String str) {
        StringBuilder builder = new StringBuilder(str);

        int index;
        while ((index = builder.toString().indexOf(ChatColor.COLOR_CHAR + "x")) >= 0) {
            int count = 0;
            builder.replace(index, index + 2, "#");

            for (int point = index + 1; count < 6; point += 1) {
                builder.deleteCharAt(point);
                count++;
            }
        }

        return builder.toString();//tagPlainHex(builder.toString());
    }

    @NotNull
    public static String strip(@NotNull String str) {
        String stripped = ChatColor.stripColor(str);
        return stripped == null ? "" : stripped;
    }

    @NotNull
    public static String restrip(@NotNull String str) {
        return strip(apply(str));
    }
}
