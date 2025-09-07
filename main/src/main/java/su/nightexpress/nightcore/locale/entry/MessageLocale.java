package su.nightexpress.nightcore.locale.entry;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.locale.message.impl.ChatMessage;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class MessageLocale extends LangEntry<LangMessage> {

    public MessageLocale(@NotNull String path, @NotNull LangMessage defaultValue) {
        super(LangMessage::read, path, defaultValue);
    }

    @NotNull
    public static MessageLocale message(@NotNull String path, @NotNull MessageData data, @NotNull String... text) {
        return message(path, String.join(TagWrappers.BR, text), data);
    }

    @NotNull
    public static MessageLocale message(@NotNull String path, @NotNull String text, @NotNull MessageData data) {
        return new MessageLocale(path, LangMessage.createFromData(text, data));
    }

    @NotNull
    public static MessageLocale chat(@NotNull String path, @NotNull String text) {
        return chat(path, new String[]{text});
    }

    @NotNull
    public static MessageLocale chat(@NotNull String path, @NotNull String... text) {
        return chat(path, MessageData.chat().build(), text);
    }

    @NotNull
    public static MessageLocale chat(@NotNull String path, @NotNull Sound sound, @NotNull String... text) {
        return chat(path, MessageData.chat().sound(sound).build(), text);
    }

    @NotNull
    private static MessageLocale chat(@NotNull String path, @NotNull MessageData data, @NotNull String... text) {
        return message(path, String.join(TagWrappers.BR, text), data);
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String title, @NotNull String subtitle) {
        return title(path, title, subtitle, MessageData.titles().build());
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String title, @NotNull String subtitle, @NotNull Sound sound) {
        return title(path, title, subtitle, MessageData.titles().sound(sound).build());
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String title, @NotNull String subtitle, int fade, int stay) {
        return title(path, title, subtitle, MessageData.titles().titleTimes(fade, stay, fade).build());
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String title, @NotNull String subtitle, int fade, int stay, @NotNull Sound sound) {
        return title(path, title, subtitle, MessageData.titles().titleTimes(fade, stay, fade).sound(sound).build());
    }

    @NotNull
    private static MessageLocale title(@NotNull String path, @NotNull String title, @NotNull String subtitle, @NotNull MessageData data) {
        return message(path, title + TagWrappers.BR + subtitle, data);
    }



    @NotNull
    public static MessageLocale actionBar(@NotNull String path, @NotNull String text) {
        return message(path, text, MessageData.actionBar().build());
    }

    @NotNull
    public static MessageLocale actionBar(@NotNull String path, @NotNull String text, @NotNull Sound sound) {
        return message(path, text, MessageData.actionBar().sound(sound).build());
    }



    @Override
    public void load(@NotNull NightPlugin plugin, @NotNull FileConfig config) {
        super.load(plugin, config);
        this.value = this.withPrefix(plugin);
    }

    @NotNull
    public LangMessage withPrefix(@NotNull NightPlugin plugin) {
        return this.withPrefix(plugin.getPrefix());
    }

    @NotNull
    public LangMessage withPrefix(@Nullable String prefix) {
        if (this.value instanceof ChatMessage chatMessage) {
            return chatMessage.withPrefix(prefix);
        }
        return this.value;
    }

    @NotNull
    public LangMessage message() {
        return this.value;
    }
}
