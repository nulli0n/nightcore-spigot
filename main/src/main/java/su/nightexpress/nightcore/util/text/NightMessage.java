package su.nightexpress.nightcore.util.text;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.tag.TagPool;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
public class NightMessage {

    @NonNull
    public static TextRoot from(@NonNull String string) {
        return from(string, TagPool.ALL);
    }

    @NonNull
    public static TextRoot from(@NonNull String string, @NonNull TagPool tagPool) {
        return new TextRoot(string, tagPool);
    }

    /**
     *
     * @param string Text to deserialize.
     * @return Precompiled TextRoot object.
     */
    @NonNull
    public static TextRoot create(@NonNull String string) {
        return create(string, TagPool.ALL);
    }

    @NonNull
    public static TextRoot create(@NonNull String string, @NonNull TagPool tagPool) {
        return from(string, tagPool).compile();
    }


    @NonNull
    public static NightComponent parse(@NonNull String string) {
        return parse(string, TagPool.ALL);
    }

    @NonNull
    public static NightComponent parse(@NonNull String string, @NonNull TagPool tagPool) {
        return from(string, tagPool).parseIfAbsent();
    }

    @NonNull
    @Deprecated
    public static List<String> splitLineTag(@NonNull List<String> list) {
        List<String> segmented = new ArrayList<>();

        list.forEach(line -> {
            Collections.addAll(segmented, Tags.LINE_BREAK.split(line));
        });

        return segmented;
    }


    @NonNull
    @Deprecated
    public static String clean(@NonNull String string) {
        return create(string, TagPool.NONE).toLegacy();
    }

    @NonNull
    @Deprecated
    public static String stripAll(@NonNull String string) {
        return stripTags(string);
        //return clean(TagUtils.tagPlainHex(LegacyColors.plainColors(string)));
    }

    /**
     * Removes all known tags from the given string. Does not affect legacy color codes.
     * 
     * @param string A string to remove tags from.
     * @return String with no tags formations.
     */
    @NonNull
    public static String stripTags(@NonNull String string) {
        return stripTags(string, TagPool.NONE);
    }

    /**
     * Removes tags from the given string according to a TagPool configuration. Does not affect legacy color codes.
     * 
     * @param string  A string to remove tags from.
     * @param tagPool List of allowed tags.
     * @return String with allowed tags only in the original tag format <tag>Text</tag>.
     */
    @NonNull
    public static String stripTags(@NonNull String string, @NonNull TagPool tagPool) {
        return from(string, tagPool).strip();
    }

    @NonNull
    public static String asJson(@NonNull String string) {
        return create(string).toJson();
    }

    @NonNull
    public static String asLegacy(@NonNull String string) {
        return create(string).toLegacy();
    }

    @Deprecated
    public static NightComponent asComponent(@NonNull String string) {
        return create(string).toComponent();
    }

    @NonNull
    @Deprecated
    public static List<String> asLegacy(@NonNull List<String> string) {
        List<String> list = new ArrayList<>();
        for (String str : string) {
            for (String br : Tags.LINE_BREAK.split(str)) {
                list.add(asLegacy(br));
            }
        }
        return list;
    }
}
