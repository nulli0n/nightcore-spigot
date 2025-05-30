package su.nightexpress.nightcore.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class CommandResult {

    private final String label;
    private final String[] args;
    private final Map<CommandFlag<?>, String> flags;

    public CommandResult(@NotNull String label, String[] args, @NotNull Map<CommandFlag<?>, StringBuilder> flags) {
        this.label = label;
        this.flags = new HashMap<>();
        this.args = args;

        flags.forEach((flag, content) -> this.flags.put(flag, content.toString()));
    }

    public int length() {
        return this.args.length;
    }

    @NotNull
    public String getArg(int index) {
        return this.getArgs()[index];
    }

    @NotNull
    public String getArg(int index, @NotNull String def) {
        if (index >= this.length()) return def;

        return this.getArgs()[index];
    }

    public int getInt(int index, int def) {
        return NumberUtil.getAnyInteger(this.getArg(index, ""), def);
    }

    public double getDouble(int index, double def) {
        return NumberUtil.getAnyDouble(this.getArg(index, ""), def);
    }

    public boolean hasFlag(@NotNull CommandFlag<?> flag) {
        return this.getFlags().containsKey(flag);
    }

    @Nullable
    public <T> T getFlag(@NotNull CommandFlag<T> flag) {
        String value = this.getFlags().get(flag);
        if (value == null) return null;

        return flag.getParser().apply(value);
    }

    @NotNull
    public <T> T getFlag(@NotNull CommandFlag<T> flag, @NotNull T def) {
        T value = this.getFlag(flag);
        return value == null ? def : value;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    @NotNull
    public Map<CommandFlag<?>, String> getFlags() {
        return flags;
    }
}
