package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;

public class SilentMessage extends LangMessage {

    public SilentMessage(@NotNull String text, @Nullable MessageData data) {
        super(text, data);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    protected void send(@NotNull CommandSender sender, @NotNull String text) {

    }
}
