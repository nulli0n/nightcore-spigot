package su.nightexpress.nightcore.core.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.List;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class CheckPermCommand extends AbstractCommand<NightCore> {

    public CheckPermCommand(@NotNull NightCore plugin) {
        super(plugin, new String[]{"checkperm"}, CorePerms.COMMAND_CHECK_PERM);
        this.setDescription(CoreLang.COMMAND_CHECKPERM_DESC);
        this.setUsage(CoreLang.COMMAND_CHECKPERM_USAGE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return Players.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.errorUsage(sender);
            return;
        }

        Player player = Players.getPlayer(result.getArg(1));
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        String builder =
            BOLD.enclose(LIGHT_YELLOW.enclose("Permissions report for ") + LIGHT_ORANGE.enclose(player.getName() + ":")) +
            LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("Primary Group: ") + Colorizer.plain(VaultHook.getPermissionGroup(player))) +
            LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("All Groups: ") + Colorizer.plain(String.join(", ", VaultHook.getPermissionGroups(player)))) +
            LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("Prefix: ") + VaultHook.getPrefix(player)) +
            LIGHT_ORANGE.enclose("▪ " + LIGHT_YELLOW.enclose("Suffix: ") + VaultHook.getSuffix(player));
        NightMessage.create(builder).send(sender);
    }
}
