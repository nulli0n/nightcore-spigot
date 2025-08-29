package su.nightexpress.nightcore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

import java.util.function.Predicate;

public class CommandRequirement {

    private final Predicate<CommandSender> predicate;
    private final MessageLocale            message;

    public CommandRequirement(@NotNull Predicate<CommandSender> predicate, @NotNull MessageLocale message) {
        this.predicate = predicate;
        this.message = message;
    }

    @NotNull
    public static CommandRequirement playerOnly() {
        return new CommandRequirement(sender -> sender instanceof Player, CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
    }

    @NotNull
    public static CommandRequirement custom(@NotNull Predicate<CommandSender> predicate, @NotNull MessageLocale message) {
        return new CommandRequirement(predicate, message);
    }

    public boolean test(@NotNull CommandSender sender) {
        return this.predicate.test(sender);
    }

    @NotNull
    public MessageLocale getMessage() {
        return this.message;
    }
}
