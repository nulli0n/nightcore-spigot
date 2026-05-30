package su.nightexpress.nightcore.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.argument.type.BlockTypeArgumentType;
import su.nightexpress.nightcore.commands.argument.type.BoolArgumentType;
import su.nightexpress.nightcore.commands.argument.type.DoubleArgumentType;
import su.nightexpress.nightcore.commands.argument.type.EnchantmentArgumentType;
import su.nightexpress.nightcore.commands.argument.type.IntegerArgumentType;
import su.nightexpress.nightcore.commands.argument.type.ItemTypeArgumentType;
import su.nightexpress.nightcore.commands.argument.type.PlayerArgumentType;
import su.nightexpress.nightcore.commands.argument.type.PlayerExactArgumentType;
import su.nightexpress.nightcore.commands.argument.type.PlayerNameArgumentType;
import su.nightexpress.nightcore.commands.argument.type.StringArgumentType;
import su.nightexpress.nightcore.commands.argument.type.WorldArgumentType;
import su.nightexpress.nightcore.commands.builder.ArgumentNodeBuilder;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class Arguments {

    private Arguments() {
    }

    public static final BlockTypeArgumentType   BLOCK_TYPE    = new BlockTypeArgumentType();
    public static final BoolArgumentType        BOOLEAN       = new BoolArgumentType();
    public static final EnchantmentArgumentType ENCHANTMENT   = new EnchantmentArgumentType();
    public static final ItemTypeArgumentType    ITEM_TYPE     = new ItemTypeArgumentType();
    public static final PlayerArgumentType      PLAYER        = new PlayerArgumentType();
    public static final PlayerExactArgumentType PLAYER_EXACT  = new PlayerExactArgumentType();
    public static final PlayerNameArgumentType  PLAYER_NAME   = new PlayerNameArgumentType();
    public static final StringArgumentType      STRING        = new StringArgumentType(false);
    public static final StringArgumentType      GREEDY_STRING = new StringArgumentType(true);
    public static final WorldArgumentType       WORLD         = new WorldArgumentType();

    private static ArgumentRegistry registry;

    public static void init(ArgumentRegistry registry) {
        if (Arguments.registry != null) throw new IllegalStateException("Registry is already initialized!");

        Arguments.registry = registry;
    }

    public static ArgumentRegistry registry() {
        if (registry == null) {
            throw new IllegalStateException("ArgumentTypes is not initialized yet! Is NightCore loaded?");
        }
        return registry;
    }

    public static <T> void register(Class<T> type, ArgumentType<T> codec) {
        registry().register(type, codec);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable ArgumentType<T> getCodec(T object) {
        return (ArgumentType<T>) getCodec(object.getClass());
    }

    public static <T> @Nullable ArgumentType<T> getCodec(Class<T> type) {
        return registry().get(type);
    }

    public static boolean isRegistered(Class<?> type) {
        return registry().isRegistered(type);
    }

    public static <T> ArgumentNodeBuilder<T> argument(String name, Class<T> type) {
        ArgumentType<T> argType = getCodec(type);
        if (argType == null) throw new IllegalArgumentException("No registered argument type " + type);

        return Commands.argument(name, argType);
    }

    public static ArgumentNodeBuilder<Boolean> bool(String name) {
        return Commands.argument(name, BOOLEAN).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    public static ArgumentNodeBuilder<Double> decimal(String name) {
        return decimal(name, -Double.MAX_VALUE);
    }

    public static ArgumentNodeBuilder<Double> decimal(String name, double min) {
        return decimal(name, min, Double.MAX_VALUE);
    }

    public static ArgumentNodeBuilder<Double> decimal(String name, double min, double max) {
        return Commands.argument(name, new DoubleArgumentType(min, max, false))
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    public static ArgumentNodeBuilder<Double> decimalCompact(String name) {
        return decimalCompact(name, -Double.MAX_VALUE);
    }

    public static ArgumentNodeBuilder<Double> decimalCompact(String name, double min) {
        return decimalCompact(name, min, Double.MAX_VALUE);
    }

    public static ArgumentNodeBuilder<Double> decimalCompact(String name, double min, double max) {
        return Commands.argument(name, new DoubleArgumentType(min, max, true))
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    public static ArgumentNodeBuilder<Integer> integer(String name) {
        return integer(name, Integer.MIN_VALUE);
    }

    public static ArgumentNodeBuilder<Integer> integer(String name, int min) {
        return integer(name, min, Integer.MAX_VALUE);
    }

    public static ArgumentNodeBuilder<Integer> integer(String name, int min, int max) {
        return Commands.argument(name, new IntegerArgumentType(min, max, false))
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    public static ArgumentNodeBuilder<Integer> integerCompact(String name) {
        return integerCompact(name, Integer.MIN_VALUE);
    }

    public static ArgumentNodeBuilder<Integer> integerCompact(String name, int min) {
        return integerCompact(name, min, Integer.MAX_VALUE);
    }

    public static ArgumentNodeBuilder<Integer> integerCompact(String name, int min, int max) {
        return Commands.argument(name, new IntegerArgumentType(min, max, true))
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    public static ArgumentNodeBuilder<Player> player(String name) {
        return Commands.argument(name, PLAYER).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    public static ArgumentNodeBuilder<Player> playerExact(String name) {
        return Commands.argument(name, PLAYER_EXACT).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    public static ArgumentNodeBuilder<String> playerName(String name) {
        return Commands.argument(name, PLAYER_NAME).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    public static ArgumentNodeBuilder<String> string(String name) {
        return Commands.argument(name, STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    public static ArgumentNodeBuilder<String> greedyString(String name) {
        return Commands.argument(name, GREEDY_STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    public static ArgumentNodeBuilder<Material> itemType(String name) {
        return Commands.argument(name, ITEM_TYPE).localized(CoreLang.COMMAND_ARGUMENT_NAME_ITEM_TYPE);
    }

    public static ArgumentNodeBuilder<Material> blockType(String name) {
        return Commands.argument(name, BLOCK_TYPE).localized(CoreLang.COMMAND_ARGUMENT_NAME_BLOCK_TYPE);
    }

    public static ArgumentNodeBuilder<Enchantment> enchantment(String name) {
        return Commands.argument(name, ENCHANTMENT).localized(CoreLang.COMMAND_ARGUMENT_NAME_ENCHANTMENT);
    }

    public static ArgumentNodeBuilder<World> world(String name) {
        return Commands.argument(name, WORLD).localized(CoreLang.COMMAND_ARGUMENT_NAME_WORLD);
    }
}
