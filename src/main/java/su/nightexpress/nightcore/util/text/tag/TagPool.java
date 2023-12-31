package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagPool {

    public static final TagPool ALL = new TagPool(Mode.BLACKLIST, Collections.emptySet());
    public static final TagPool NONE = new TagPool(Mode.WHITELIST, Collections.emptySet());

    private final Mode mode;
    private final Set<String> tags;

    @NotNull
    public static TagPool whitelisted(@NotNull Tag... tags) {
        return create(Mode.WHITELIST, tags);
    }

    @NotNull
    public static TagPool blacklisted(@NotNull Tag... tags) {
        return create(Mode.BLACKLIST, tags);
    }

    @NotNull
    public static TagPool create(@NotNull Mode mode, @NotNull Tag... tags) {
        return new TagPool(mode, Stream.of(tags).map(Tag::getName).collect(Collectors.toSet()));
    }

    public TagPool(@NotNull Mode mode, @NotNull Set<String> tags) {
        this.mode = mode;
        this.tags = new HashSet<>(tags);
    }

    public enum Mode {
        WHITELIST, BLACKLIST
    }

    public boolean isWhitelist() {
        return this.getMode() == Mode.WHITELIST;
    }

    public boolean isBlacklist() {
        return this.getMode() == Mode.BLACKLIST;
    }

    public boolean isGoodTag(@NotNull Tag tag) {
        return this.isGoodTag(tag.getName());
    }

    public boolean isGoodTag(@NotNull String name) {
        name = name.toLowerCase();

        if (this.isWhitelist()) {
            return this.getTags().contains(name) || this.getTags().contains(Placeholders.WILDCARD);
        }
        else {
            return !this.getTags().contains(name);
        }
    }

    @NotNull
    public Mode getMode() {
        return mode;
    }

    @NotNull
    public Set<String> getTags() {
        return tags;
    }
}
