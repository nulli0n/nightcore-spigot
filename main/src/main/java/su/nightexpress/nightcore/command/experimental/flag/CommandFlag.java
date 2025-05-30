package su.nightexpress.nightcore.command.experimental.flag;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.Placeholders;

public abstract class CommandFlag {

    public static final char PREFIX    = '-';
    public static final char DELIMITER = '=';

    private final String name;
    private final String permission;

    public CommandFlag(@NotNull String name, @Nullable String permission) {
        this.name = name.toLowerCase();
        this.permission = permission;
    }

    public boolean hasPermission(@NotNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getPrefixed() {
        return PREFIX + this.getName();
    }

    @NotNull
    public String getPrefixedFormatted() {
        return CoreLang.COMMAND_FLAG_FORMAT.getString().replace(Placeholders.GENERIC_NAME, this.getPrefixed());
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "CommandFlag{" +
            "name='" + name + '\'' +
            '}';
    }
}
