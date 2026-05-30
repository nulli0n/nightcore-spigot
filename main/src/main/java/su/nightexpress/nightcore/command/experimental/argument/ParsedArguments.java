package su.nightexpress.nightcore.command.experimental.argument;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ParsedArguments {

    private final Map<String, ParsedArgument<?>> argumentMap;
    private final Map<String, ParsedArgument<?>> flags;

    public ParsedArguments() {
        this.argumentMap = new HashMap<>();
        this.flags = new HashMap<>();
    }

    public void add(@NonNull CommandArgument<?> argument, @NonNull ParsedArgument<?> parsedArgument) {
        this.argumentMap.put(argument.getName(), parsedArgument);
    }

    public void addFlag(@NonNull CommandFlag flag, @NonNull ParsedArgument<?> content) {
        this.flags.put(flag.getName(), content);
    }

    @NonNull
    public Map<String, ParsedArgument<?>> getArgumentMap() {
        return argumentMap;
    }

    @NonNull
    public Map<String, ParsedArgument<?>> getFlags() {
        return flags;
    }

    public int getIntArgument(@NonNull String name, int defaultValue) {
        return this.getArgument(name, Integer.class, defaultValue);
    }

    public int getIntArgument(@NonNull String name) {
        return this.getArgument(name, Integer.class);
    }

    public double getDoubleArgument(@NonNull String name, double defaultValue) {
        return this.getArgument(name, Double.class, defaultValue);
    }

    public double getDoubleArgument(@NonNull String name) {
        return this.getArgument(name, Double.class);
    }

    public boolean getBooleanArgument(@NonNull String name, boolean defaultValue) {
        return this.getArgument(name, Boolean.class, defaultValue);
    }

    public boolean getBooleanArgument(@NonNull String name) {
        return this.getArgument(name, Boolean.class);
    }

    @NonNull
    public String getStringArgument(@NonNull String name, @NonNull String defaultValue) {
        return this.getArgument(name, String.class, defaultValue);
    }

    @NonNull
    public String getStringArgument(@NonNull String name) {
        return this.getArgument(name, String.class);
    }

    @NonNull
    public Material getMaterialArgument(@NonNull String name, @NonNull Material defaultValue) {
        return this.getArgument(name, Material.class, defaultValue);
    }

    @NonNull
    public Material getMaterialArgument(@NonNull String name) {
        return this.getArgument(name, Material.class);
    }

    @NonNull
    public World getWorldArgument(@NonNull String name, @NonNull World defaultValue) {
        return this.getArgument(name, World.class, defaultValue);
    }

    @NonNull
    public World getWorldArgument(@NonNull String name) {
        return this.getArgument(name, World.class);
    }

    @NonNull
    public Enchantment getEnchantmentArgument(@NonNull String name, @NonNull Enchantment defaultValue) {
        return this.getArgument(name, Enchantment.class, defaultValue);
    }

    @NonNull
    public Enchantment getEnchantmentArgument(@NonNull String name) {
        return this.getArgument(name, Enchantment.class);
    }

    @NonNull
    public Player getPlayerArgument(@NonNull String name) {
        return this.getArgument(name, Player.class);
    }

    public boolean hasArgument(@NonNull String name) {
        return this.argumentMap.containsKey(name);
    }

    @NonNull
    public <T> T getArgument(@NonNull String name, @NonNull Class<T> clazz, @NonNull T defaultValue) {
        if (!this.hasArgument(name)) return defaultValue;

        return this.getArgument(name, clazz);
    }

    @NonNull
    public <T> T getArgument(@NonNull String name, @NonNull Class<T> clazz) {
        ParsedArgument<?> argument = this.argumentMap.get(name);
        if (argument == null) {
            throw new IllegalArgumentException("No such argument '" + name + "' exists on this command");
        }

        Object result = argument.getResult();
        if (clazz.isAssignableFrom(result.getClass())) {
            return clazz.cast(result);
        }
        else {
            throw new IllegalArgumentException("Argument '" + name + "' is defined as " + result.getClass()
                .getSimpleName() + ", not " + clazz);
        }
    }

    public boolean hasFlag(@NonNull CommandFlag flag) {
        return this.hasFlag(flag.getName());
    }

    public boolean hasFlag(@NonNull String name) {
        return this.flags.containsKey(name);
    }

    public int getIntFlag(@NonNull String name, int defaultValue) {
        return this.getFlag(name, Integer.class, defaultValue);
    }

    public double getDoubleFlag(@NonNull String name, double defaultValue) {
        return this.getFlag(name, Double.class, defaultValue);
    }

    public boolean getBooleanFlag(@NonNull String name, boolean defaultValue) {
        return this.getFlag(name, Boolean.class, defaultValue);
    }

    @NonNull
    public String getStringFlag(@NonNull String name, @NonNull String defaultValue) {
        return this.getFlag(name, String.class, defaultValue);
    }

    @NonNull
    public <T> T getFlag(@NonNull String name, @NonNull Class<T> clazz, @NonNull T defaultValue) {
        if (!this.hasFlag(name)) return defaultValue;

        return this.getFlag(name, clazz);
    }

    @NonNull
    public <T> T getFlag(@NonNull String name, @NonNull Class<T> clazz) {
        ParsedArgument<?> parsed = this.flags.get(name);
        if (parsed == null) {
            throw new IllegalArgumentException("No such flag '" + name + "' exists on this command");
        }

        Object result = parsed.getResult();
        if (clazz.isAssignableFrom(result.getClass())) {
            return clazz.cast(result);
        }
        else {
            throw new IllegalArgumentException("Flag '" + name + "' is defined as " + result.getClass()
                .getSimpleName() + ", not " + clazz);
        }
    }
}
