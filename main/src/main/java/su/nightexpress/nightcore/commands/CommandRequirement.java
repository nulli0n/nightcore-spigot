package su.nightexpress.nightcore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

import java.util.function.Predicate;

public class CommandRequirement {

    private final Predicate<CommandSender> predicate;
    private final MessageLocale            message;

    public CommandRequirement(@NonNull Predicate<CommandSender> predicate, @NonNull MessageLocale message) {
        this.predicate = predicate;
        this.message = message;
    }

    @NonNull
    public static CommandRequirement playerOnly() {
        return new CommandRequirement(sender -> sender instanceof Player, CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
    }

    @NonNull
    public static CommandRequirement custom(@NonNull Predicate<CommandSender> predicate,
                                            @NonNull MessageLocale message) {
        return new CommandRequirement(predicate, message);
    }

    public boolean test(@NonNull CommandSender sender) {
        return this.predicate.test(sender);
    }

    @NonNull
    public MessageLocale getMessage() {
        return this.message;
    }
}
