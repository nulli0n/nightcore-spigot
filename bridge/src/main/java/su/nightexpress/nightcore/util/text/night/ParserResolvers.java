package su.nightexpress.nightcore.util.text.night;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.text.night.tag.TagHandler;
import su.nightexpress.nightcore.util.text.night.tag.TagHandlerRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserResolvers {

    // TODO Probably not needed anymore
    static class Index {

        private final List<TagHandler> resolvers;

        public Index() {
            this.resolvers = new ArrayList<>();
        }

        @NotNull
        public TagHandler add(@NotNull TagHandler resolver) {
            this.resolvers.add(resolver);
            return resolver;
        }

        @Nullable
        public TagHandler removeLast() {
            return this.resolvers.isEmpty() ? null : this.resolvers.removeLast();
        }
    }

    private final Map<String, Index> indexes;

    public ParserResolvers() {
        this.indexes = new HashMap<>();
    }

    public void clear() {
        this.indexes.clear();
    }

    @NotNull
    public List<TagHandler> removeAll() {
        List<TagHandler> resolvers = this.indexes.values().stream().flatMap(index -> index.resolvers.stream()).toList();
        this.clear();
        return resolvers;
    }

    @Nullable
    public TagHandler createFor(@NotNull String tagName) {
        String name = LowerCase.INTERNAL.apply(tagName);

        TagHandler resolver = TagHandlerRegistry.create(name);
        if (resolver == null) return null;

        Index index = this.indexes.computeIfAbsent(name, k -> new Index());

        return index.add(resolver);
    }

    @Nullable
    public TagHandler removeFor(@NotNull String tagName) {
        String name = LowerCase.INTERNAL.apply(tagName);

        Index index = this.indexes.get(name);

        return index == null ? null : index.removeLast();
    }
}
