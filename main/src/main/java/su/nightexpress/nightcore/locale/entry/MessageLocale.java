package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.locale.message.impl.ActionBarMessage;
import su.nightexpress.nightcore.locale.message.impl.ChatMessage;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.impl.TitleMessage;

public class MessageLocale extends LangEntry<LangMessage> {

    public MessageLocale(@NotNull String path, @NotNull LangMessage defaultValue) {
        super(LangMessage::read, path, defaultValue);
    }

    @NotNull
    public static MessageLocale chat(@NotNull String path, @NotNull String text) {
        return chat(path, text, null);
    }

    @NotNull
    public static MessageLocale chat(@NotNull String path, @NotNull String text, @Nullable MessageData data) {
        return new MessageLocale(path, new ChatMessage(text, data));
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String text) {
        return title(path, text, null);
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String text, int fade, int stay) {
        return title(path, text, MessageData.builder().titleTimes(fade, stay, fade).build());
    }

    @NotNull
    public static MessageLocale title(@NotNull String path, @NotNull String text, @Nullable MessageData data) {
        return new MessageLocale(path, new TitleMessage(text, data));
    }

    @NotNull
    public static MessageLocale actionBar(@NotNull String path, @NotNull String text) {
        return actionBar(path, text, null);
    }

    @NotNull
    public static MessageLocale actionBar(@NotNull String path, @NotNull String text, @Nullable MessageData data) {
        return new MessageLocale(path, new ActionBarMessage(text, data));
    }

    @Override
    public void load(@NotNull NightPlugin plugin, @NotNull FileConfig config, @NotNull String langCode) {
        super.load(plugin, config, langCode);

        this.value = this.withPrefix(plugin.getPrefix());
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
