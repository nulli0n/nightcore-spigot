package su.nightexpress.nightcore.command.experimental.argument;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;

import java.util.HashMap;
import java.util.Map;

public class ParsedArguments {

    private final Map<String, ParsedArgument<?>> argumentMap;
    private final Map<String, ParsedArgument<?>> flags;

    public ParsedArguments() {
        this.argumentMap = new HashMap<>();
        this.flags = new HashMap<>();
    }

    public void add(@NotNull CommandArgument<?> argument, @NotNull ParsedArgument<?> parsedArgument) {
        this.argumentMap.put(argument.getName(), parsedArgument);
    }

    public void addFlag(@NotNull CommandFlag flag, @NotNull ParsedArgument<?> content) {
        this.flags.put(flag.getName(), content);
    }

    @NotNull
    public Map<String, ParsedArgument<?>> getArgumentMap() {
        return argumentMap;
    }

    @NotNull
    public Map<String, ParsedArgument<?>> getFlags() {
        return flags;
    }

    public int getIntArgument(@NotNull String name, int defaultValue) {
        return this.getArgument(name, Integer.class, defaultValue);
    }

    public int getIntArgument(@NotNull String name) {
        return this.getArgument(name, Integer.class);
    }

    public double getDoubleArgument(@NotNull String name, double defaultValue) {
        return this.getArgument(name, Double.class, defaultValue);
    }

    public double getDoubleArgument(@NotNull String name) {
        return this.getArgument(name, Double.class);
    }

    public boolean getBooleanArgument(@NotNull String name, boolean defaultValue) {
        return this.getArgument(name, Boolean.class, defaultValue);
    }

    public boolean getBooleanArgument(@NotNull String name) {
        return this.getArgument(name, Boolean.class);
    }

    @NotNull
    public String getStringArgument(@NotNull String name, @NotNull String defaultValue) {
        return this.getArgument(name, String.class, defaultValue);
    }

    @NotNull
    public String getStringArgument(@NotNull String name) {
        return this.getArgument(name, String.class);
    }

    @NotNull
    public Material getMaterialArgument(@NotNull String name, @NotNull Material defaultValue) {
        return this.getArgument(name, Material.class, defaultValue);
    }

    @NotNull
    public Material getMaterialArgument(@NotNull String name) {
        return this.getArgument(name, Material.class);
    }

    @NotNull
    public World getWorldArgument(@NotNull String name, @NotNull World defaultValue) {
        return this.getArgument(name, World.class, defaultValue);
    }

    @NotNull
    public World getWorldArgument(@NotNull String name) {
        return this.getArgument(name, World.class);
    }

    @NotNull
    public Enchantment getEnchantmentArgument(@NotNull String name, @NotNull Enchantment defaultValue) {
        return this.getArgument(name, Enchantment.class, defaultValue);
    }

    @NotNull
    public Enchantment getEnchantmentArgument(@NotNull String name) {
        return this.getArgument(name, Enchantment.class);
    }

    @NotNull
    public Player getPlayerArgument(@NotNull String name) {
        return this.getArgument(name, Player.class);
    }

    public boolean hasArgument(@NotNull String name) {
        return this.argumentMap.containsKey(name);
    }

    @NotNull
    public <T> T getArgument(@NotNull String name, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        if (!this.hasArgument(name)) return defaultValue;

        return this.getArgument(name, clazz);
    }

    @NotNull
    public <T> T getArgument(@NotNull String name, @NotNull Class<T> clazz) {
        ParsedArgument<?> argument = this.argumentMap.get(name);
        if (argument == null) {
            throw new IllegalArgumentException("No such argument '" + name + "' exists on this command");
        }

        Object result = argument.getResult();
        if (clazz.isAssignableFrom(result.getClass())) {
            return clazz.cast(result);
        }
        else {
            throw new IllegalArgumentException("Argument '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + clazz);
        }
    }

    public boolean hasFlag(@NotNull CommandFlag flag) {
        return this.hasFlag(flag.getName());
    }

    public boolean hasFlag(@NotNull String name) {
        return this.flags.containsKey(name);
    }

    public int getIntFlag(@NotNull String name, int defaultValue) {
        return this.getFlag(name, Integer.class, defaultValue);
    }

    public double getDoubleFlag(@NotNull String name, double defaultValue) {
        return this.getFlag(name, Double.class, defaultValue);
    }

    public boolean getBooleanFlag(@NotNull String name, boolean defaultValue) {
        return this.getFlag(name, Boolean.class, defaultValue);
    }

    @NotNull
    public String getStringFlag(@NotNull String name, @NotNull String defaultValue) {
        return this.getFlag(name, String.class, defaultValue);
    }

    @NotNull
    public <T> T getFlag(@NotNull String name, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        if (!this.hasFlag(name)) return defaultValue;

        return this.getFlag(name, clazz);
    }

    @NotNull
    public <T> T getFlag(@NotNull String name, @NotNull Class<T> clazz) {
        ParsedArgument<?> parsed = this.flags.get(name);
        if (parsed == null) {
            throw new IllegalArgumentException("No such flag '" + name + "' exists on this command");
        }

        Object result = parsed.getResult();
        if (clazz.isAssignableFrom(result.getClass())) {
            return clazz.cast(result);
        }
        else {
            throw new IllegalArgumentException("Flag '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + clazz);
        }
    }
}
