package su.nightexpress.nightcore.locale.entry;

import org.bukkit.Sound;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.locale.message.impl.ChatMessage;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class MessageLocale extends LangEntry<LangMessage> {

    private static final ConfigType<LangMessage> CONFIG_TYPE = ConfigType.of(LangMessage::read, FileConfig::set);

    public MessageLocale(@NonNull String path, @NonNull LangMessage defaultValue) {
        super(CONFIG_TYPE, path, defaultValue);
    }

    @NonNull
    public static MessageLocale message(@NonNull String path, @NonNull MessageData data, @NonNull String... text) {
        return message(path, String.join(TagWrappers.BR, text), data);
    }

    @NonNull
    public static MessageLocale message(@NonNull String path, @NonNull String text, @NonNull MessageData data) {
        return new MessageLocale(path, LangMessage.createFromData(text, data));
    }

    @NonNull
    public static MessageLocale chat(@NonNull String path, @NonNull String text) {
        return chat(path, new String[]{text});
    }

    @NonNull
    public static MessageLocale chat(@NonNull String path, @NonNull String... text) {
        return chat(path, MessageData.chat().build(), text);
    }

    @NonNull
    public static MessageLocale chat(@NonNull String path, @NonNull Sound sound, @NonNull String... text) {
        return chat(path, MessageData.chat().sound(sound).build(), text);
    }

    @NonNull
    private static MessageLocale chat(@NonNull String path, @NonNull MessageData data, @NonNull String... text) {
        return message(path, String.join(TagWrappers.BR, text), data);
    }

    @NonNull
    public static MessageLocale title(@NonNull String path, @NonNull String title, @NonNull String subtitle) {
        return title(path, title, subtitle, MessageData.titles().build());
    }

    @NonNull
    public static MessageLocale title(@NonNull String path, @NonNull String title, @NonNull String subtitle, @NonNull Sound sound) {
        return title(path, title, subtitle, MessageData.titles().sound(sound).build());
    }

    @NonNull
    public static MessageLocale title(@NonNull String path, @NonNull String title, @NonNull String subtitle, int fade, int stay) {
        return title(path, title, subtitle, MessageData.titles().titleTimes(fade, stay, fade).build());
    }

    @NonNull
    public static MessageLocale title(@NonNull String path, @NonNull String title, @NonNull String subtitle, int fade, int stay, @NonNull Sound sound) {
        return title(path, title, subtitle, MessageData.titles().titleTimes(fade, stay, fade).sound(sound).build());
    }

    @NonNull
    private static MessageLocale title(@NonNull String path, @NonNull String title, @NonNull String subtitle, @NonNull MessageData data) {
        return message(path, title + TagWrappers.BR + subtitle, data);
    }



    @NonNull
    public static MessageLocale actionBar(@NonNull String path, @NonNull String text) {
        return message(path, text, MessageData.actionBar().build());
    }

    @NonNull
    public static MessageLocale actionBar(@NonNull String path, @NonNull String text, @NonNull Sound sound) {
        return message(path, text, MessageData.actionBar().sound(sound).build());
    }



    @Override
    public void load(@NonNull NightPlugin plugin, @NonNull FileConfig config) {
        super.load(plugin, config);
        this.value = this.withPrefix(plugin);
    }

    @NonNull
    public LangMessage withPrefix(@NonNull NightPlugin plugin) {
        return this.withPrefix(plugin.getPrefix());
    }

    @NonNull
    public LangMessage withPrefix(@Nullable String prefix) {
        if (this.value instanceof ChatMessage chatMessage) {
            return chatMessage.withPrefix(prefix);
        }
        return this.value;
    }

    @NonNull
    public LangMessage message() {
        return this.value;
    }
}
