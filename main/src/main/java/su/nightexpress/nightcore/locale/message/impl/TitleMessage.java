package su.nightexpress.nightcore.locale.message.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;
import su.nightexpress.nightcore.util.text.night.ParserUtils;

import java.util.Collection;

public class TitleMessage extends LangMessage {

    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleMessage(@NotNull String text, @NotNull MessageData data) {
        super(text, data);

        int[] titleTimes = data.titleTimes();
        this.fadeIn = titleTimes != null && titleTimes.length >= 1 ? titleTimes[0] : 20;
        this.stay = titleTimes != null && titleTimes.length >= 2 ? titleTimes[1] : 60;
        this.fadeOut = titleTimes != null && titleTimes.length >= 3 ? titleTimes[2] : 20;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    protected void send(@NotNull Collection<? extends CommandSender> receivers, @NotNull String text) {
        String[] split = ParserUtils.breakDownLineSplitters(text);

        String title = split[0];
        String subTitle = split.length >= 2 ? split[1] : "";

        NightComponent titleComp = NightMessage.parse(title);
        NightComponent subtitleComp = NightMessage.parse(subTitle);

        receivers.forEach(sender -> {
            if (sender instanceof Player player) Players.sendTitles(player, titleComp, subtitleComp, this.fadeIn, this.stay, this.fadeOut);
        });
    }
}
