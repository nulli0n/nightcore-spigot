package su.nightexpress.nightcore.locale;

import org.bukkit.Keyed;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.entry.*;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.function.Function;

public class LangEntry<T extends LangValue> implements LangElement {

    protected final ConfigValue.Loader<T> loader;

    protected final String path;
    protected final T      defaultValue;

    //protected final Map<String, T> translations;

    protected T value;

    public LangEntry(@NotNull ConfigValue.Loader<T> loader, @NotNull String path, @NotNull T defaultValue) {
        this.loader = loader;
        this.path = path;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        //this.translations = new HashMap<>();
        //this.withDefault(LangRegistry.DEFAULT_LANGUAGE, defaultValue);
    }

    /*@NotNull
    public LangEntry<T> withDefault(@NotNull String langCode, @NotNull T value) {
        langCode.toLowerCase();
        this.translations.put(LowerCase.INTERNAL.apply(langCode), value);
        return this;
    }*/

    @Override
    public void load(@NotNull NightPlugin plugin, @NotNull FileConfig config/*, @NotNull String langCode*/) {
        this.value = ConfigValue.create(this.path, this.loader, this.getDefaultValue(/*langCode*/)).read(config);
    }

    /*@Override
    public boolean isSupportedLocale(@NotNull String locale) {
        return this.translations.containsKey(locale);
    }*/

    @NotNull
    public static Builder builder(@NotNull String path) {
        return new Builder(path);
    }

    @NotNull
    public static IconLocale.Builder iconBuilder(@NotNull String path) {
        return new IconLocale.Builder(path);
    }

    /*@Override
    @NotNull
    public Set<String> getSupportedLocales() {
        return Collections.unmodifiableSet(this.translations.keySet());
    }*/

    @Override
    @NotNull
    public String getPath() {
        return this.path;
    }

    @NotNull
    public T getDefaultValue() {
        return this.defaultValue;
    }

    /*@Override
    @NotNull
    public T getDefaultValue(@NotNull String langCode) {
        T translated = this.getTranslation(langCode);
        return translated == null ? this.getDefaultValue() : translated;
    }*/

    /*@Nullable
    public T getTranslation(@NotNull String langCode) {
        return this.translations.get(langCode);
    }*/

    @NotNull
    public T value() {
        return this.value == null ? this.defaultValue : this.value;
    }

    public static class Builder {

        private final String path;

        public Builder(@NotNull String path) {
            this.path = path;
        }

        @NotNull
        public MessageLocale message(@NotNull MessageData data, @NotNull String... text) {
            return MessageLocale.message(this.path, data, text);
        }

        @NotNull
        public MessageLocale chatMessage(@NotNull String text) {
            return chatMessage(new String[]{text});
        }

        @NotNull
        public MessageLocale chatMessage(@NotNull String... text) {
            return MessageLocale.chat(this.path, text);
        }

        @NotNull
        public MessageLocale chatMessage(@NotNull Sound sound, @NotNull String... text) {
            return MessageLocale.chat(this.path, sound, text);
        }

        @NotNull
        public MessageLocale titleMessage(@NotNull String title, @NotNull String subtitle) {
            return MessageLocale.title(this.path, title, subtitle);
        }

        @NotNull
        public MessageLocale titleMessage(@NotNull String title, @NotNull String subtitle, @NotNull Sound sound) {
            return MessageLocale.title(this.path, title, subtitle, sound);
        }

        @NotNull
        public MessageLocale titleMessage(@NotNull String title, @NotNull String subtitle, int fade, int stay) {
            return MessageLocale.title(this.path, title, subtitle, fade, stay);
        }

        @NotNull
        public MessageLocale titleMessage(@NotNull String title, @NotNull String subtitle, int fade, int stay, @NotNull Sound sound) {
            return MessageLocale.title(this.path, title, subtitle, fade, stay, sound);
        }

        @NotNull
        public MessageLocale actionBarMessage(@NotNull String text) {
            return MessageLocale.actionBar(this.path, text);
        }

        @NotNull
        public MessageLocale actionBarMessage(@NotNull String text, @NotNull Sound sound) {
            return MessageLocale.actionBar(this.path, text, sound);
        }

        @NotNull
        public BooleanLocale bool(@NotNull String onTrue, @NotNull String onFalse) {
            return BooleanLocale.create(this.path, onTrue, onFalse);
        }

        @NotNull
        public TextLocale text(@NotNull String text) {
            return TextLocale.create(this.path, text);
        }

        @NotNull
        public TextLocale text(@NotNull String... text) {
            return TextLocale.create(this.path, text);
        }

        @NotNull
        public <E extends Enum<E>> EnumLocale<E> enumeration(@NotNull Class<E> clazz) {
            return EnumLocale.create(this.path, clazz);
        }

        @NotNull
        public <E extends Enum<E>> EnumLocale<E> enumeration(@NotNull Class<E> clazz, @NotNull Function<E, String> defaultMapper) {
            return EnumLocale.create(this.path, clazz, defaultMapper);
        }

        @NotNull
        public <E extends Keyed> RegistryLocale<E> registry(@NotNull RegistryType<E> type) {
            return RegistryLocale.create(this.path, type);
        }

        @NotNull
        public ButtonLocale button(@NotNull String label) {
            return ButtonLocale.create(this.path, label);
        }

        @NotNull
        public ButtonLocale button(@NotNull String label, int width) {
            return ButtonLocale.create(this.path, label, width);
        }

        @NotNull
        public ButtonLocale button(@NotNull String label, @Nullable String tooltip) {
            return ButtonLocale.create(this.path, label, tooltip);
        }

        @NotNull
        public ButtonLocale button(@NotNull String label, @Nullable String tooltip, int width) {
            return ButtonLocale.create(this.path, label, tooltip, width);
        }

        @NotNull
        public DialogElementLocale dialogElement(@NotNull String... contents) {
            return DialogElementLocale.create(this.path, contents);
        }

        @NotNull
        public DialogElementLocale dialogElement(int width, @NotNull String... contents) {
            return DialogElementLocale.create(this.path, width, contents);
        }
    }
}
