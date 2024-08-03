package su.nightexpress.nightcore.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.function.Function;

@Deprecated
public class CommandFlag<T> {

    public static final char PREFIX = '-';

    private final String name;
    private final Function<String, T> parser;

    public CommandFlag(@NotNull String name, @NotNull Function<String, T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @NotNull
    public static CommandFlag<World> worldFlag(@NotNull String name) {
        return new CommandFlag<>(name, Bukkit::getWorld);
    }

    @NotNull
    public static CommandFlag<String> stringFlag(@NotNull String name) {
        return new CommandFlag<>(name, Function.identity());
    }

    @NotNull
    public static CommandFlag<String> textFlag(@NotNull String name) {
        return new CommandFlag<>(name, Colorizer::apply);
    }

    @NotNull
    public static CommandFlag<Integer> intFlag(@NotNull String name) {
        return new CommandFlag<>(name, str -> NumberUtil.getAnyInteger(str, 0));
    }

    @NotNull
    public static CommandFlag<Double> doubleFlag(@NotNull String name) {
        return new CommandFlag<>(name, str -> NumberUtil.getAnyDouble(str, 0D));
    }

    @NotNull
    public static CommandFlag<Boolean> booleanFlag(@NotNull String name) {
        return new CommandFlag<>(name, str -> true);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getNamePrefixed() {
        return PREFIX + this.getName();
    }

    @NotNull
    public Function<String, T> getParser() {
        return parser;
    }

    @Override
    public String toString() {
        return "CommandFlag{" +
            "name='" + name + '\'' +
            '}';
    }
}
