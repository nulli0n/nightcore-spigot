package su.nightexpress.nightcore.language.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.tag.impl.NoPrefixTag;
import su.nightexpress.nightcore.language.tag.impl.OutputTag;
import su.nightexpress.nightcore.language.tag.impl.PlaceholderTag;
import su.nightexpress.nightcore.language.tag.impl.SoundTag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MessageTags {

    private static final Map<String, MessageTag> REGISTRY = new HashMap<>();

    public static final NoPrefixTag    NO_PREFIX   = new NoPrefixTag();
    public static final OutputTag      OUTPUT      = new OutputTag();
    public static final SoundTag       SOUND       = new SoundTag();
    public static final PlaceholderTag PLACEHOLDER = new PlaceholderTag();

    static {
        registerTags(NO_PREFIX, OUTPUT, SOUND, PLACEHOLDER);
    }

    public static void registerTags(@NotNull MessageTag... tags) {
        for (MessageTag tag : tags) {
            registerTag(tag);
        }
    }

    public static void registerTag(@NotNull MessageTag tag, @NotNull String... aliases) {
        REGISTRY.put(tag.getName(), tag);
        for (String alias : aliases) {
            REGISTRY.put(alias, tag);
        }
    }

    @NotNull
    public static Collection<MessageTag> getTags() {
        return REGISTRY.values();
    }

    public static MessageTag getTag(@NotNull String name) {
        return REGISTRY.get(name.toLowerCase());
    }
}
