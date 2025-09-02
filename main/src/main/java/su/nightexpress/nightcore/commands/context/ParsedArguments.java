package su.nightexpress.nightcore.commands.context;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ParsedArguments {

    private final Map<String, ParsedArgument<?>> arguments;

    public ParsedArguments() {
        this.arguments = new LinkedHashMap<>();
    }

    @NotNull
    public ParsedArguments add(@NotNull String name, @NotNull ParsedArgument<?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    @NotNull
    public Map<String, ParsedArgument<?>> map() {
        return this.arguments;
    }

    public int getInt(@NotNull String name, int defaultValue) {
        return this.getOr(name, Integer.class, defaultValue);
    }

    public int getInt(@NotNull String name) {
        return this.get(name, Integer.class);
    }

    public double getFloat(@NotNull String name, float defaultValue) {
        return this.getOr(name, Float.class, defaultValue);
    }

    public double getFloat(@NotNull String name) {
        return this.get(name, Float.class);
    }

    public double getDouble(@NotNull String name, double defaultValue) {
        return this.getOr(name, Double.class, defaultValue);
    }

    public double getDouble(@NotNull String name) {
        return this.get(name, Double.class);
    }

    public boolean getBoolean(@NotNull String name, boolean defaultValue) {
        return this.getOr(name, Boolean.class, defaultValue);
    }

    public boolean getBoolean(@NotNull String name) {
        return this.get(name, Boolean.class);
    }

    @NotNull
    public String getString(@NotNull String name, @NotNull String defaultValue) {
        return this.getOr(name, String.class, defaultValue);
    }

    @NotNull
    public String getString(@NotNull String name) {
        return this.get(name, String.class);
    }

    @NotNull
    public Material getMaterial(@NotNull String name, @NotNull Material defaultValue) {
        return this.getOr(name, Material.class, defaultValue);
    }

    @NotNull
    public Material getMaterial(@NotNull String name) {
        return this.get(name, Material.class);
    }

    @NotNull
    public World getWorld(@NotNull String name, @NotNull World defaultValue) {
        return this.getOr(name, World.class, defaultValue);
    }

    @NotNull
    public World getWorld(@NotNull String name) {
        return this.get(name, World.class);
    }

    @NotNull
    public Enchantment getEnchantment(@NotNull String name, @NotNull Enchantment defaultValue) {
        return this.getOr(name, Enchantment.class, defaultValue);
    }

    @NotNull
    public Enchantment getEnchantment(@NotNull String name) {
        return this.get(name, Enchantment.class);
    }

    @NotNull
    public Player getPlayer(@NotNull String name) {
        return this.get(name, Player.class);
    }

    public boolean contains(@NotNull String name) {
        return this.arguments.containsKey(name);
    }

    @NotNull
    public <T> Optional<T> lookup(@NotNull String name, @NotNull Class<T> clazz) {
        return this.contains(name) ? Optional.of(this.get(name, clazz)) : Optional.empty();
    }

    @Nullable
    public <T> T getOrNull(@NotNull String name, @NotNull Class<T> clazz) {
        return this.lookup(name, clazz).orElse(null);
    }

    @NotNull
    public <T> T getOr(@NotNull String name, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        return this.lookup(name, clazz).orElse(defaultValue);
    }

    @NotNull
    public <T> T get(@NotNull String name, @NotNull Class<T> clazz) {
        ParsedArgument<?> argument = this.arguments.get(name);
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
}
