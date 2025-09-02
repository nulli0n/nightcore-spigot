package su.nightexpress.nightcore.util.text.night;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.tag.TagPool;

public class NightMessage {

    @NotNull
    public static NightComponent parse(@NotNull String string) {
        return TextParser.parse(string);
    }

    @NotNull
    public static NightComponent parse(@NotNull String string, @NotNull TagPool tagPool) {
        return TextParser.parse(string, tagPool);
    }

    /**
     * Removes all known tags from the given string. Does not affect legacy color codes.
     * @param string A string to remove tags from.
     * @return String with no tags formations.
     */
    @NotNull
    public static String stripTags(@NotNull String string) {
        return stripTags(string, TagPool.NONE);
    }

    /**
     * Removes tags from the given string according to a TagPool configuration. Does not affect legacy color codes.
     * @param string A string to remove tags from.
     * @param tagPool List of allowed tags.
     * @return String with allowed tags only in the original tag format <tag>Text</tag>.
     */
    @NotNull
    public static String stripTags(@NotNull String string, @NotNull TagPool tagPool) {
        return TextParser.strip(string, tagPool);
    }

    @NotNull
    public static String asJson(@NotNull String string) {
        return parse(string).toJson();
    }

    @NotNull
    public static String asLegacy(@NotNull String string) {
        return parse(string).toLegacy();
    }
}
