package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.argument.CommandArgument;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class TabContext {

    private final CommandSender sender;
    private final Player        player;
    private final String        label;
    private final String[]      args;

    private final Map<String, String> argumentCache;
    private final Map<String, String> flagCache;

    private int lastCommandIndex;

    public TabContext(@NotNull CommandSender sender, @NotNull String label, String[] args, int lastCommandIndex) {
        this.sender = sender;
        this.player = sender instanceof Player user ? user : null;
        this.label = label;
        this.args = args;
        this.lastCommandIndex = lastCommandIndex;

        this.argumentCache = new HashMap<>();
        this.flagCache = new HashMap<>();
    }

    public int length() {
        return this.args.length;
    }

    public boolean hasCachedArgument(@NotNull CommandArgument<?> argument) {
        return this.argumentCache.containsKey(argument.getName());
    }

    public boolean hasCachedFlag(@NotNull CommandFlag flag) {
        return this.flagCache.containsKey(flag.getName());
    }

    public void cacheArgument(@NotNull CommandArgument<?> argument, @NotNull String value) {
        this.argumentCache.put(argument.getName(), value);
    }

    public void cacheFlag(@NotNull CommandFlag flag, @NotNull String value) {
        this.flagCache.put(flag.getName(), value);
    }

    public void appendArgumentCache(@NotNull CommandArgument<?> argument, @NotNull String value) {
        String cached = this.getCachedArgument(argument.getName());
        cached = cached == null ? value : cached + " " + value;
        this.cacheArgument(argument, cached);
    }

    @Nullable
    public String getCachedArgument(@NotNull String name) {
        return this.argumentCache.get(name.toLowerCase());
    }

    @Nullable
    public String getCachedFlag(@NotNull String name) {
        return this.flagCache.get(name.toLowerCase());
    }

    @NotNull
    public String getArg(int index) {
        return this.args[index];
    }

    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public Player getPlayerOrThrow() {
        if (this.player != null) return this.player;

        throw new IllegalStateException("TabContext is not bound to a player!");
    }

    @NotNull
    public String getLabel() {
        return this.label;
    }

    @NotNull
    public String[] getArgs() {
        return this.args;
    }

    @Deprecated
    public int getIndex() {
        return this.getLastCommandIndex();
    }

    @Deprecated
    public void setIndex(int index) {
        this.setLastCommandIndex(index);
    }

    public int getLastCommandIndex() {
        return this.lastCommandIndex;
    }

    public void setLastCommandIndex(int index) {
        this.lastCommandIndex = index;
    }

    @Deprecated
    public String getAtIndex() {
        return this.args[this.lastCommandIndex];
    }

    public String getInput() {
        return this.args[this.args.length - 1];
    }

    @NotNull
    public Map<String, String> getArgumentCache() {
        return this.argumentCache;
    }

    @NotNull
    public Map<String, String> getFlagCache() {
        return this.flagCache;
    }
}
