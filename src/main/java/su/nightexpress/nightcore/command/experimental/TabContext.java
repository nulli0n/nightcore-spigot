package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabContext {

    private final CommandSender sender;
    private final Player        player;
    private final String        label;
    private final String[]      args;

    private int index;

    public TabContext(@NotNull CommandSender sender, @NotNull String label, String[] args, int index) {
        this.sender = sender;
        this.player = sender instanceof Player user ? user : null;
        this.label = label;
        this.args = args;
        this.index = index;
    }

    @NotNull
    public CommandSender getSender() {
        return sender;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public Player getPlayerOrThrow() {
        if (this.player != null) return this.player;

        throw new IllegalStateException("TabContext is not bound to a player!");
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @NotNull
    public String[] getArgs() {
        return args;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAtIndex() {
        return this.args[this.index];
    }

    public String getInput() {
        return this.args[this.args.length - 1];
    }
}
