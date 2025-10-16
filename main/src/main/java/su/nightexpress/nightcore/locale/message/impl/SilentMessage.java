package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;

import java.util.Collection;

public class SilentMessage extends LangMessage {

    public SilentMessage(@NotNull String text, @NotNull MessageData data) {
        super(text, data);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    protected void send(@NotNull Collection<? extends CommandSender> receivers, @NotNull String text) {

    }
}
