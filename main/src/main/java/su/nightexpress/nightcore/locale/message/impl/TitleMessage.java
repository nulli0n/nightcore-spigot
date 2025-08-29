package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.text.night.ParserUtils;

public class TitleMessage extends LangMessage {

    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleMessage(@NotNull String text, @NotNull MessageData data) {
        super(text, data);
        this.fadeIn = data.titleTimes().length >= 1 ? data.titleTimes()[0] : 20;
        this.stay = data.titleTimes().length >= 2 ? data.titleTimes()[1] : 60;
        this.fadeOut = data.titleTimes().length >= 3 ? data.titleTimes()[2] : 20;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    protected void send(@NotNull CommandSender sender, @NotNull String text) {
        if (!(sender instanceof Player player)) return;

        String[] split = ParserUtils.breakDownLineSplitters(text);

        String title = split[0];
        String subTitle = split.length >= 2 ? split[1] : "";

        Players.sendTitles(player, title, subTitle, this.fadeIn, this.stay, this.fadeOut);
    }
}
