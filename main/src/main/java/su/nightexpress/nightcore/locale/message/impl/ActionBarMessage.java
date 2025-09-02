package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.Players;

public class ActionBarMessage extends LangMessage {

    public ActionBarMessage(@NotNull String text, @NotNull MessageData data) {
        super(text, data);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    protected void send(@NotNull CommandSender sender, @NotNull String text) {
        if (!(sender instanceof Player player)) return;

        Players.sendActionBar(player, text);
    }
}
