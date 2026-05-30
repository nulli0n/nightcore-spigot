package su.nightexpress.nightcore.command.experimental.flag;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.Placeholders;

@Deprecated
public abstract class CommandFlag {

    public static final char PREFIX    = '-';
    public static final char DELIMITER = '=';

    private final String name;
    private final String permission;

    public CommandFlag(@NonNull String name, @Nullable String permission) {
        this.name = name.toLowerCase();
        this.permission = permission;
    }

    public boolean hasPermission(@NonNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPrefixed() {
        return PREFIX + this.getName();
    }

    @NonNull
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
