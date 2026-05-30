package su.nightexpress.nightcore.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.function.Function;

@Deprecated
public class CommandFlag<T> {

    public static final char PREFIX = '-';

    private final String              name;
    private final Function<String, T> parser;

    public CommandFlag(@NonNull String name, @NonNull Function<String, T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @NonNull
    public static CommandFlag<World> worldFlag(@NonNull String name) {
        return new CommandFlag<>(name, Bukkit::getWorld);
    }

    @NonNull
    public static CommandFlag<String> stringFlag(@NonNull String name) {
        return new CommandFlag<>(name, Function.identity());
    }

    @NonNull
    public static CommandFlag<String> textFlag(@NonNull String name) {
        return new CommandFlag<>(name, s -> s);
    }

    @NonNull
    public static CommandFlag<Integer> intFlag(@NonNull String name) {
        return new CommandFlag<>(name, str -> NumberUtil.getAnyInteger(str, 0));
    }

    @NonNull
    public static CommandFlag<Double> doubleFlag(@NonNull String name) {
        return new CommandFlag<>(name, str -> NumberUtil.getAnyDouble(str, 0D));
    }

    @NonNull
    public static CommandFlag<Boolean> booleanFlag(@NonNull String name) {
        return new CommandFlag<>(name, str -> true);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getNamePrefixed() {
        return PREFIX + this.getName();
    }

    @NonNull
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
