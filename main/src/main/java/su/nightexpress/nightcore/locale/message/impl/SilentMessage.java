package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;

import java.util.Collection;

public class SilentMessage extends LangMessage {

    public SilentMessage(@NonNull String text, @NonNull MessageData data) {
        super(text, data);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    protected void send(@NonNull Collection<? extends CommandSender> receivers, @NonNull String text) {

    }
}
