package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrapper;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.*;

public class IconLocale extends LangEntry<IconLocale.Value> {

    public IconLocale(@NotNull String path, @NotNull Value defaultValue) {
        super(Value::read, path, defaultValue);
    }

    @NotNull
    public static IconLocale create(@NotNull String path, @NotNull String name, @NotNull List<String> description) {
        return new IconLocale(path, new Value(name, description));
    }

    @NotNull
    public String getName() {
        return this.value.name();
    }

    @NotNull
    public List<String> getLore() {
        return this.value.lore();
    }

    public record Value(@NotNull String name, @NotNull List<String> lore) implements LangValue {

        @NotNull
        public static Value read(@NotNull FileConfig config, @NotNull String path) {
            String name = config.getString(path + ".Name", "null");
            List<String> description = config.getStringList(path + ".Lore");

            return new Value(name, description);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            config.set(path + ".Name", this.name);
            config.set(path + ".Lore", this.lore);
        }
    }

    public static class Builder {

        private final String       path;
        private final List<String> lore;

        private String name;
        private TagWrapper accentColor;

        public Builder(@NotNull String path) {
            this.path = path;
            this.lore = new ArrayList<>();
            this.accentColor = TagWrappers.SOFT_YELLOW;
        }

        @NotNull
        public IconLocale build() {
            Value value = new Value(this.name, this.lore);
            return new IconLocale(this.path, value);
        }

        @NotNull
        public Builder accentColor(@NotNull TagWrapper wrapper) {
            this.accentColor = wrapper;
            return this;
        }

        @NotNull
        public Builder br() {
            this.lore.add(" ");
            return this;
        }

        @NotNull
        public Builder name(@NotNull String name) {
            return this.name(name, this.accentColor);
        }

        @NotNull
        public Builder name(@NotNull String name, @NotNull TagWrapper color) {
            return this.rawName(color.and(TagWrappers.BOLD).wrap(name));
        }

        @NotNull
        public Builder rawName(@NotNull String name) {
            this.name = name;
            return this;
        }

        @NotNull
        public Builder appendInfo(@NotNull String... text) {
            return this.appendInfo(Lists.newList(text));
        }

        @NotNull
        public Builder appendInfo(@NotNull List<String> text) {
            this.lore.addAll(Lists.modify(text, TagWrappers.GRAY::wrap));
            return this;
        }

        @NotNull
        public Builder rawLore(@NotNull String... text) {
            this.lore.addAll(Lists.newList(text));
            return this;
        }

        @NotNull
        public Builder appendCurrent(@NotNull String type, @NotNull String value) {
            this.appendInfo(TagWrappers.DARK_GRAY.wrap("» ") + type + ": " + TagWrappers.WHITE.wrap(value));
            return this;
        }

        @NotNull
        @Deprecated
        public Builder appendCurrent(@NotNull String type, @NotNull String value, @NotNull TagWrapper color) {
            //this.appendInfo(TagWrappers.DARK_GRAY.wrap("» ") + type + ": " + TagWrappers.WHITE.wrap(value));
            return this.appendCurrent(type, value);
        }

        @NotNull
        public Builder appendClick(@NotNull String clickText) {
            return this.appendClick(clickText, this.accentColor);
        }

        @NotNull
        public Builder appendClick(@NotNull String clickText, @NotNull TagWrapper color) {
            this.lore.add(color.wrap("→ " + TagWrappers.UNDERLINED.wrap(clickText)));
            return this;
        }
    }
}
