package su.nightexpress.nightcore.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

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

public class Arguments {

    public static final BlockTypeArgumentType BLOCK_TYPE = new BlockTypeArgumentType();
    public static final BoolArgumentType BOOLEAN = new BoolArgumentType();
    public static final EnchantmentArgumentType ENCHANTMENT = new EnchantmentArgumentType();
    public static final ItemTypeArgumentType ITEM_TYPE = new ItemTypeArgumentType();
    public static final PlayerArgumentType PLAYER = new PlayerArgumentType();
    public static final PlayerExactArgumentType PLAYER_EXACT = new PlayerExactArgumentType();
    public static final PlayerNameArgumentType PLAYER_NAME = new PlayerNameArgumentType();
    public static final StringArgumentType STRING = new StringArgumentType(false);
    public static final StringArgumentType GREEDY_STRING = new StringArgumentType(true);
    public static final WorldArgumentType WORLD = new WorldArgumentType();

    @NonNull
    public static ArgumentNodeBuilder<Boolean> bool(@NonNull String name) {
        return Commands.argument(name, BOOLEAN).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NonNull
    public static ArgumentNodeBuilder<Double> decimal(@NonNull String name) {
        return decimal(name, -Double.MAX_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Double> decimal(@NonNull String name, double min) {
        return decimal(name, min, Double.MAX_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Double> decimal(@NonNull String name, double min, double max) {
        return Commands.argument(name, new DoubleArgumentType(min, max, false))
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    @NonNull
    public static ArgumentNodeBuilder<Double> decimalCompact(@NonNull String name) {
        return decimalCompact(name, -Double.MAX_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Double> decimalCompact(@NonNull String name, double min) {
        return decimalCompact(name, min, Double.MAX_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Double> decimalCompact(@NonNull String name, double min, double max) {
        return Commands.argument(name, new DoubleArgumentType(min, max, true))
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    @NonNull
    public static ArgumentNodeBuilder<Integer> integer(@NonNull String name) {
        return integer(name, Integer.MIN_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Integer> integer(@NonNull String name, int min) {
        return integer(name, min, Integer.MAX_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Integer> integer(@NonNull String name, int min, int max) {
        return Commands.argument(name, new IntegerArgumentType(min, max, false))
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    @NonNull
    public static ArgumentNodeBuilder<Integer> integerCompact(@NonNull String name) {
        return integerCompact(name, Integer.MIN_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Integer> integerCompact(@NonNull String name, int min) {
        return integerCompact(name, min, Integer.MAX_VALUE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Integer> integerCompact(@NonNull String name, int min, int max) {
        return Commands.argument(name, new IntegerArgumentType(min, max, true))
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }

    @NonNull
    public static ArgumentNodeBuilder<Player> player(@NonNull String name) {
        return Commands.argument(name, PLAYER).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    @NonNull
    public static ArgumentNodeBuilder<Player> playerExact(@NonNull String name) {
        return Commands.argument(name, PLAYER_EXACT).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    @NonNull
    public static ArgumentNodeBuilder<String> playerName(@NonNull String name) {
        return Commands.argument(name, PLAYER_NAME).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    @NonNull
    public static ArgumentNodeBuilder<String> string(@NonNull String name) {
        return Commands.argument(name, STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NonNull
    public static ArgumentNodeBuilder<String> greedyString(@NonNull String name) {
        return Commands.argument(name, GREEDY_STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NonNull
    public static ArgumentNodeBuilder<Material> itemType(@NonNull String name) {
        return Commands.argument(name, ITEM_TYPE).localized(CoreLang.COMMAND_ARGUMENT_NAME_ITEM_TYPE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Material> blockType(@NonNull String name) {
        return Commands.argument(name, BLOCK_TYPE).localized(CoreLang.COMMAND_ARGUMENT_NAME_BLOCK_TYPE);
    }

    @NonNull
    public static ArgumentNodeBuilder<Enchantment> enchantment(@NonNull String name) {
        return Commands.argument(name, ENCHANTMENT).localized(CoreLang.COMMAND_ARGUMENT_NAME_ENCHANTMENT);
    }

    @NonNull
    public static ArgumentNodeBuilder<World> world(@NonNull String name) {
        return Commands.argument(name, WORLD).localized(CoreLang.COMMAND_ARGUMENT_NAME_WORLD);
    }
}
