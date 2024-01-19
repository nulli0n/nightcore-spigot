package su.nightexpress.nightcore.language.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.tag.impl.NoPrefixTag;
import su.nightexpress.nightcore.language.tag.impl.PlaceholderTag;
import su.nightexpress.nightcore.language.tag.impl.SoundTag;
import su.nightexpress.nightcore.language.tag.impl.OutputTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MessageTags {

    private static final Map<String, Tag> TAG_MAP = new HashMap<>();

    public static final NoPrefixTag    NO_PREFIX   = new NoPrefixTag();
    public static final OutputTag      OUTPUT      = new OutputTag();
    public static final SoundTag       SOUND       = new SoundTag();
    public static final PlaceholderTag PLACEHOLDER = new PlaceholderTag();

    static {
        registerTags(NO_PREFIX, OUTPUT, SOUND, PLACEHOLDER);
    }

    public static void registerTags(@NotNull Tag... tags) {
        for (Tag tag : tags) {
            registerTag(tag);
        }
    }

    public static void registerTag(@NotNull Tag tag, @NotNull String... aliases) {
        TAG_MAP.put(tag.getName(), tag);
        for (String alias : aliases) {
            TAG_MAP.put(alias, tag);
        }
    }

    @NotNull
    public static Collection<Tag> getTags() {
        return TAG_MAP.values();
    }

    public static Tag getTag(@NotNull String name) {
        return TAG_MAP.get(name.toLowerCase());
    }
}
