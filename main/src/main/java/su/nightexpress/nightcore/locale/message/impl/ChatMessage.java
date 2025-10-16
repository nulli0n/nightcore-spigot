package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.Collection;

public class ChatMessage extends LangMessage {

    private final boolean noPrefix;
    private final String  prefix;

    public ChatMessage(@NotNull String text, @Nullable MessageData data) {
        this(text, data, null);
    }

    public ChatMessage(@NotNull String text, @Nullable MessageData data, @Nullable String prefix) {
        super(text, data);
        this.noPrefix = data != null && !data.usePrefix();
        this.prefix = prefix;
    }

    /**
     * @param prefix New prefix.
     * @return A new ChatMessage instance with the given prefix (or null).
     */
    @NotNull
    public ChatMessage withPrefix(@Nullable String prefix) {
        if (this.noPrefix) return this;

        return new ChatMessage(this.text, this.data, prefix);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    protected void send(@NotNull Collection<? extends CommandSender> receivers, @NotNull String text) {
        String message = (this.prefix != null && !this.noPrefix) ? (this.prefix + text) : text;
        NightComponent component = NightMessage.parse(message);

        receivers.forEach(player -> Players.sendMessage(player, component));
    }
}
