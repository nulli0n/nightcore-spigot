package su.nightexpress.nightcore.util.text;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.TagPool;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
public class NightMessage {

    @NotNull
    public static TextRoot from(@NotNull String string) {
        return from(string, TagPool.ALL);
    }

    @NotNull
    public static TextRoot from(@NotNull String string, @NotNull TagPool tagPool) {
        return new TextRoot(string, tagPool);
    }

    /**
     *
     * @param string Text to deserialize.
     * @return Precompiled TextRoot object.
     */
    @NotNull
    public static TextRoot create(@NotNull String string) {
        return create(string, TagPool.ALL);
    }

    @NotNull
    public static TextRoot create(@NotNull String string, @NotNull TagPool tagPool) {
        return from(string, tagPool).compile();
    }



    @NotNull
    public static NightComponent parse(@NotNull String string) {
        return parse(string, TagPool.ALL);
    }

    @NotNull
    public static NightComponent parse(@NotNull String string, @NotNull TagPool tagPool) {
        return from(string, tagPool).parseIfAbsent();
    }

    @NotNull
    @Deprecated
    public static List<String> splitLineTag(@NotNull List<String> list) {
        List<String> segmented = new ArrayList<>();

        list.forEach(line -> {
            Collections.addAll(segmented, Tags.LINE_BREAK.split(line));
        });

        return segmented;
    }



    @NotNull
    @Deprecated
    public static String clean(@NotNull String string) {
        return create(string, TagPool.NONE).toLegacy();
    }

    @NotNull
    @Deprecated
    public static String stripAll(@NotNull String string) {
        return stripTags(string);
        //return clean(TagUtils.tagPlainHex(LegacyColors.plainColors(string)));
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
        return from(string, tagPool).strip();
    }

    @NotNull
    public static String asJson(@NotNull String string) {
        return create(string).toJson();
    }

    @NotNull
    public static String asLegacy(@NotNull String string) {
        return create(string).toLegacy();
    }

    @Deprecated
    public static NightComponent asComponent(@NotNull String string) {
        return create(string).toComponent();
    }

    @NotNull
    @Deprecated
    public static List<String> asLegacy(@NotNull List<String> string) {
        List<String> list = new ArrayList<>();
        for (String str : string) {
            for (String br : Tags.LINE_BREAK.split(str)) {
                list.add(asLegacy(br));
            }
        }
        return list;
    }
}
