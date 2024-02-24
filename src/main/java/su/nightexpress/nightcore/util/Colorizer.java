package su.nightexpress.nightcore.util;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.regex.TimedMatcher;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Colorizer {

    public static final Pattern PATTERN_HEX      = Pattern.compile("#([A-Fa-f0-9]{6})");
    public static final Pattern PATTERN_HEX_LEGACY = Pattern.compile("(?:^|[^<])(#[A-Fa-f0-9]{6})(?:$|[^:>])");
    public static final Pattern PATTERN_GRADIENT = Pattern.compile("<gradient:" + PATTERN_HEX.pattern() + ">(.*?)</gradient:" + PATTERN_HEX.pattern() + ">");

    @NotNull
    public static String apply(@NotNull String str) {
        return hex(gradient(legacy(str)));
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

    private static ChatColor[] createGradient(@NotNull java.awt.Color start, @NotNull java.awt.Color end, int length) {
        ChatColor[] colors = new ChatColor[length];
        for (int index = 0; index < length; index++) {
            double percent = (double) index / (double) length;

            int red = (int) (start.getRed() + percent * (end.getRed() - start.getRed()));
            int green = (int) (start.getGreen() + percent * (end.getGreen() - start.getGreen()));
            int blue = (int) (start.getBlue() + percent * (end.getBlue() - start.getBlue()));

            java.awt.Color color = new java.awt.Color(red, green, blue);
            colors[index] = ChatColor.of(color);
        }
        return colors;
    }

    @NotNull
    public static String gradient(@NotNull String string) {
        TimedMatcher timedMatcher = TimedMatcher.create(PATTERN_GRADIENT, string);
        //Matcher matcher = PATTERN_GRADIENT.matcher(string);
        while (timedMatcher.find()) {
            Matcher matcher = timedMatcher.getMatcher();
            String start = matcher.group(1);
            String end = matcher.group(3);
            String content = matcher.group(2);
            System.out.println("content legacy = " + content);

            java.awt.Color colorStart = new java.awt.Color(Integer.parseInt(start, 16));
            java.awt.Color colorEnd = new java.awt.Color(Integer.parseInt(end, 16));
            ChatColor[] colors = createGradient(colorStart, colorEnd, strip(content).length());

            StringBuilder gradiented = new StringBuilder();
            StringBuilder specialColors = new StringBuilder();
            char[] characters = content.toCharArray();
            int outIndex = 0;
            for (int index = 0; index < characters.length; index++) {
                if (characters[index] == ChatColor.COLOR_CHAR) {
                    if (index + 1 < characters.length) {
                        if (characters[index + 1] == 'r') {
                            specialColors.setLength(0);
                        }
                        else {
                            specialColors.append(characters[index]);
                            specialColors.append(characters[index + 1]);
                        }
                        index++;
                    }
                    else gradiented.append(colors[outIndex++]).append(specialColors).append(characters[index]);
                }
                else gradiented.append(colors[outIndex++]).append(specialColors).append(characters[index]);
            }

            string = string.replace(matcher.group(0), gradiented.toString());
        }
        return string;
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
    public static String plainHex(@NotNull String str) {
        StringBuilder buffer = new StringBuilder(str);

        int index;
        while ((index = buffer.toString().indexOf(ChatColor.COLOR_CHAR + "x")) >= 0) {
            int count = 0;
            buffer.replace(index, index + 2, "#");

            for (int point = index + 1; count < 6; point += 1) {
                buffer.deleteCharAt(point);
                count++;
            }
        }

        return buffer.toString();
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
