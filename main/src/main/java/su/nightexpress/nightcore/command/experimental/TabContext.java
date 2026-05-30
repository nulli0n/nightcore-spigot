package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    public TabContext(@NonNull CommandSender sender, @NonNull String label, String[] args, int lastCommandIndex) {
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

    public boolean hasCachedArgument(@NonNull CommandArgument<?> argument) {
        return this.argumentCache.containsKey(argument.getName());
    }

    public boolean hasCachedFlag(@NonNull CommandFlag flag) {
        return this.flagCache.containsKey(flag.getName());
    }

    public void cacheArgument(@NonNull CommandArgument<?> argument, @NonNull String value) {
        this.argumentCache.put(argument.getName(), value);
    }

    public void cacheFlag(@NonNull CommandFlag flag, @NonNull String value) {
        this.flagCache.put(flag.getName(), value);
    }

    public void appendArgumentCache(@NonNull CommandArgument<?> argument, @NonNull String value) {
        String cached = this.getCachedArgument(argument.getName());
        cached = cached == null ? value : cached + " " + value;
        this.cacheArgument(argument, cached);
    }

    @Nullable
    public String getCachedArgument(@NonNull String name) {
        return this.argumentCache.get(name.toLowerCase());
    }

    @Nullable
    public String getCachedFlag(@NonNull String name) {
        return this.flagCache.get(name.toLowerCase());
    }

    @NonNull
    public String getArg(int index) {
        return this.args[index];
    }

    @NonNull
    public CommandSender getSender() {
        return this.sender;
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @NonNull
    public Player getPlayerOrThrow() {
        if (this.player != null) return this.player;

        throw new IllegalStateException("TabContext is not bound to a player!");
    }

    @NonNull
    public String getLabel() {
        return this.label;
    }

    @NonNull
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

    @NonNull
    public Map<String, String> getArgumentCache() {
        return this.argumentCache;
    }

    @NonNull
    public Map<String, String> getFlagCache() {
        return this.flagCache;
    }
}
