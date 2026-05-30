package su.nightexpress.nightcore.commands.context;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ParsedArguments {

    private final Map<String, ParsedArgument<?>> arguments;

    public ParsedArguments() {
        this.arguments = new LinkedHashMap<>();
    }

    @NonNull
    public ParsedArguments add(@NonNull String name, @NonNull ParsedArgument<?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    @NonNull
    public Map<String, ParsedArgument<?>> map() {
        return this.arguments;
    }

    public int getInt(@NonNull String name, int defaultValue) {
        return this.getOr(name, Integer.class, defaultValue);
    }

    public int getInt(@NonNull String name) {
        return this.get(name, Integer.class);
    }

    public double getFloat(@NonNull String name, float defaultValue) {
        return this.getOr(name, Float.class, defaultValue);
    }

    public double getFloat(@NonNull String name) {
        return this.get(name, Float.class);
    }

    public double getDouble(@NonNull String name, double defaultValue) {
        return this.getOr(name, Double.class, defaultValue);
    }

    public double getDouble(@NonNull String name) {
        return this.get(name, Double.class);
    }

    public boolean getBoolean(@NonNull String name, boolean defaultValue) {
        return this.getOr(name, Boolean.class, defaultValue);
    }

    public boolean getBoolean(@NonNull String name) {
        return this.get(name, Boolean.class);
    }

    @NonNull
    public String getString(@NonNull String name, @NonNull String defaultValue) {
        return this.getOr(name, String.class, defaultValue);
    }

    @NonNull
    public String getString(@NonNull String name) {
        return this.get(name, String.class);
    }

    @NonNull
    public Material getMaterial(@NonNull String name, @NonNull Material defaultValue) {
        return this.getOr(name, Material.class, defaultValue);
    }

    @NonNull
    public Material getMaterial(@NonNull String name) {
        return this.get(name, Material.class);
    }

    @NonNull
    public World getWorld(@NonNull String name, @NonNull World defaultValue) {
        return this.getOr(name, World.class, defaultValue);
    }

    @NonNull
    public World getWorld(@NonNull String name) {
        return this.get(name, World.class);
    }

    @NonNull
    public Enchantment getEnchantment(@NonNull String name, @NonNull Enchantment defaultValue) {
        return this.getOr(name, Enchantment.class, defaultValue);
    }

    @NonNull
    public Enchantment getEnchantment(@NonNull String name) {
        return this.get(name, Enchantment.class);
    }

    @NonNull
    public Player getPlayer(@NonNull String name) {
        return this.get(name, Player.class);
    }

    public boolean contains(@NonNull String name) {
        return this.arguments.containsKey(name);
    }

    @NonNull
    public <T> Optional<T> lookup(@NonNull String name, @NonNull Class<T> clazz) {
        return this.contains(name) ? Optional.of(this.get(name, clazz)) : Optional.empty();
    }

    @Nullable
    public <T> T getOrNull(@NonNull String name, @NonNull Class<T> clazz) {
        return this.lookup(name, clazz).orElse(null);
    }

    @NonNull
    public <T> T getOr(@NonNull String name, @NonNull Class<T> clazz, @NonNull T defaultValue) {
        return this.lookup(name, clazz).orElse(defaultValue);
    }

    @NonNull
    public <T> T get(@NonNull String name, @NonNull Class<T> clazz) {
        ParsedArgument<?> argument = this.arguments.get(name);
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
}
