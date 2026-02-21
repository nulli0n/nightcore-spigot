package su.nightexpress.nightcore.locale.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrapper;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.util.ArrayList;
import java.util.List;

public class IconLocale extends LangEntry<IconLocale.Value> {

    private static final ConfigType<Value> CONFIG_TYPE = ConfigType.of(Value::read, FileConfig::set);

    public IconLocale(@NonNull String path, @NonNull Value defaultValue) {
        super(CONFIG_TYPE, path, defaultValue);
    }

    @NonNull
    public static IconLocale create(@NonNull String path, @NonNull String name, @NonNull List<String> description) {
        return new IconLocale(path, new Value(name, description));
    }

    @NonNull
    public String getName() {
        return this.value.name();
    }

    @NonNull
    public List<String> getLore() {
        return this.value.lore();
    }

    public record Value(@NonNull String name, @NonNull List<String> lore) implements LangValue {

        @NonNull
        public static Value read(@NonNull FileConfig config, @NonNull String path) {
            String name = config.getString(path + ".Name", "null");
            List<String> description = config.getStringList(path + ".Lore");

            return new Value(name, description);
        }

        @Override
        public void write(@NonNull FileConfig config, @NonNull String path) {
            config.set(path + ".Name", this.name);
            config.set(path + ".Lore", this.lore);
        }
    }

    public static class Builder {

        private final String       path;
        private final List<String> lore;

        private String name;
        private TagWrapper accentColor;

        public Builder(@NonNull String path) {
            this.path = path;
            this.lore = new ArrayList<>();
            this.accentColor = TagWrappers.SOFT_YELLOW;
        }

        @NonNull
        public IconLocale build() {
            Value value = new Value(this.name, this.lore);
            return new IconLocale(this.path, value);
        }

        @NonNull
        public Builder accentColor(@NonNull TagWrapper wrapper) {
            this.accentColor = wrapper;
            return this;
        }

        @NonNull
        public Builder br() {
            this.lore.add(" ");
            return this;
        }

        @NonNull
        public Builder name(@NonNull String name) {
            return this.name(name, this.accentColor);
        }

        @NonNull
        public Builder name(@NonNull String name, @NonNull TagWrapper color) {
            return this.rawName(color.and(TagWrappers.BOLD).wrap(name));
        }

        @NonNull
        public Builder rawName(@NonNull String name) {
            this.name = name;
            return this;
        }

        @NonNull
        public Builder appendInfo(@NonNull String... text) {
            return this.appendInfo(Lists.newList(text));
        }

        @NonNull
        public Builder appendInfo(@NonNull List<String> text) {
            this.lore.addAll(Lists.modify(text, TagWrappers.GRAY::wrap));
            return this;
        }

        @NonNull
        public Builder rawLore(@NonNull String... text) {
            this.lore.addAll(Lists.newList(text));
            return this;
        }

        @NonNull
        public Builder appendCurrent(@NonNull String type, @NonNull String value) {
            this.appendInfo(TagWrappers.DARK_GRAY.wrap("» ") + type + ": " + TagWrappers.WHITE.wrap(value));
            return this;
        }

        @NonNull
        @Deprecated
        public Builder appendCurrent(@NonNull String type, @NonNull String value, @NonNull TagWrapper color) {
            //this.appendInfo(TagWrappers.DARK_GRAY.wrap("» ") + type + ": " + TagWrappers.WHITE.wrap(value));
            return this.appendCurrent(type, value);
        }

        @NonNull
        public Builder appendClick(@NonNull String clickText) {
            return this.appendClick(clickText, this.accentColor);
        }

        @NonNull
        public Builder appendClick(@NonNull String clickText, @NonNull TagWrapper color) {
            this.lore.add(color.wrap("→ " + TagWrappers.UNDERLINED.wrap(clickText)));
            return this;
        }
    }
}
