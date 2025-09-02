package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.function.Consumer;

@Deprecated
public class CommandContext {

    private final NightCorePlugin plugin;
    private final CommandSender   sender;
    private final Player          executor;
    private final String          label;
    private final String[]        args;

    private int argumentIndex;

    public CommandContext(@NotNull NightCorePlugin plugin, @NotNull CommandSender sender, @NotNull String label, String[] args) {
        this.plugin = plugin;
        this.sender = sender;
        this.executor = sender instanceof Player player ? player : null;
        this.label = label;
        this.args = args;
        this.argumentIndex = 0;
    }

    public void send(@NotNull String string) {
        this.sender.sendMessage(string);
    }

    @Deprecated
    public boolean sendSuccess(@NotNull String string) {
        this.send(string);
        return true;
    }

    @Deprecated
    public boolean sendFailure(@NotNull String string) {
        this.send(string);
        return false;
    }

    public void send(@NotNull LangText text) {
        this.send(text, null);
    }

    public void send(@NotNull LangText text, @Nullable Consumer<Replacer> consumer) {
        this.send(text.getMessage(), consumer);
    }

    public void send(@NotNull LangMessage message) {
        this.send(message, null);
    }

    public void send(@NotNull LangMessage message, @Nullable Consumer<Replacer> consumer) {
        message.send(this.sender, consumer);
    }

//    public void send(@NotNull LangMessage message) {
//        message.send(this.sender);
//    }

    @Deprecated
    public boolean sendSuccess(@NotNull LangMessage message) {
        this.send(message);
        return true;
    }

    @Deprecated
    public boolean sendFailure(@NotNull LangMessage message) {
        this.send(message);
        return false;
    }

    public boolean checkPermission(@NotNull Permission permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean checkPermission(@NotNull String permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean isPlayer() {
        return this.executor != null;
    }

    public int getArgumentIndex() {
        return this.argumentIndex;
    }

    public void setArgumentIndex(int argumentIndex) {
        this.argumentIndex = argumentIndex;
    }

    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    @Nullable
    public Player getExecutor() {
        return this.executor;
    }

    @NotNull
    public Player getPlayerOrThrow() {
        if (this.isPlayer()) return this.executor;

        throw new IllegalStateException("CommandContext is not bound to a player!");
    }

    public int length() {
        return this.args.length;
    }

    @NotNull
    public String getLabel() {
        return this.label;
    }

    public String[] getArgs() {
        return this.args;
    }

    public void errorPermission() {
        this.send(CoreLang.ERROR_NO_PERMISSION.getMessage(plugin));
    }

    public void errorBadPlayer() {
        this.send(CoreLang.ERROR_INVALID_PLAYER.getMessage(plugin));
    }

    public void errorPlayerOnly() {
        this.send(CoreLang.ERROR_COMMAND_PLAYER_ONLY.getMessage(plugin));
    }
}
