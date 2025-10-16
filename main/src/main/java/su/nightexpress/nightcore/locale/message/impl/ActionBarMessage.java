package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.Collection;

public class ActionBarMessage extends LangMessage {

    public ActionBarMessage(@NotNull String text, @NotNull MessageData data) {
        super(text, data);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    protected void send(@NotNull Collection<? extends CommandSender> receivers, @NotNull String text) {
        NightComponent component = NightMessage.parse(text);
        receivers.forEach(sender -> {
            if (sender instanceof Player player) Players.sendActionBar(player, component);
        });
    }
}
