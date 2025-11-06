package su.nightexpress.nightcore.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.argument.type.*;
import su.nightexpress.nightcore.commands.builder.ArgumentNodeBuilder;
import su.nightexpress.nightcore.core.config.CoreLang;

public class Arguments {

    public static final BlockTypeArgumentType   BLOCK_TYPE    = new BlockTypeArgumentType();
    public static final BoolArgumentType        BOOLEAN       = new BoolArgumentType();
    public static final EnchantmentArgumentType ENCHANTMENT   = new EnchantmentArgumentType();
    public static final ItemTypeArgumentType    ITEM_TYPE     = new ItemTypeArgumentType();
    public static final PlayerArgumentType      PLAYER        = new PlayerArgumentType();
    public static final PlayerNameArgumentType  PLAYER_NAME   = new PlayerNameArgumentType();
    public static final StringArgumentType      STRING        = new StringArgumentType(false);
    public static final StringArgumentType      GREEDY_STRING = new StringArgumentType(true);
    public static final WorldArgumentType       WORLD         = new WorldArgumentType();

    @NotNull
    public static ArgumentNodeBuilder<Boolean> bool(@NotNull String name) {
        return Commands.argument(name, BOOLEAN).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NotNull
    public static ArgumentNodeBuilder<Double> decimal(@NotNull String name) {
        return decimal(name, -Double.MAX_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Double> decimal(@NotNull String name, double min) {
        return decimal(name, min, Double.MAX_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Double> decimal(@NotNull String name, double min, double max) {
        return Commands.argument(name, new DoubleArgumentType(min, max, false)).localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }



    @NotNull
    public static ArgumentNodeBuilder<Double> decimalCompact(@NotNull String name) {
        return decimalCompact(name, -Double.MAX_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Double> decimalCompact(@NotNull String name, double min) {
        return decimalCompact(name, min, Double.MAX_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Double> decimalCompact(@NotNull String name, double min, double max) {
        return Commands.argument(name, new DoubleArgumentType(min, max, true)).localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }



    @NotNull
    public static ArgumentNodeBuilder<Integer> integer(@NotNull String name) {
        return integer(name, Integer.MIN_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Integer> integer(@NotNull String name, int min) {
        return integer(name, min, Integer.MAX_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Integer> integer(@NotNull String name, int min, int max) {
        return Commands.argument(name, new IntegerArgumentType(min, max, false)).localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }



    @NotNull
    public static ArgumentNodeBuilder<Integer> integerCompact(@NotNull String name) {
        return integerCompact(name, Integer.MIN_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Integer> integerCompact(@NotNull String name, int min) {
        return integerCompact(name, min, Integer.MAX_VALUE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Integer> integerCompact(@NotNull String name, int min, int max) {
        return Commands.argument(name, new IntegerArgumentType(min, max, true)).localized(CoreLang.COMMAND_ARGUMENT_NAME_NUMBER);
    }



    @NotNull
    public static ArgumentNodeBuilder<Player> player(@NotNull String name) {
        return Commands.argument(name, PLAYER).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    @NotNull
    public static ArgumentNodeBuilder<String> playerName(@NotNull String name) {
        return Commands.argument(name, PLAYER_NAME).localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER);
    }

    @NotNull
    public static ArgumentNodeBuilder<String> string(@NotNull String name) {
        return Commands.argument(name, STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NotNull
    public static ArgumentNodeBuilder<String> greedyString(@NotNull String name) {
        return Commands.argument(name, GREEDY_STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NotNull
    public static ArgumentNodeBuilder<Material> itemType(@NotNull String name) {
        return Commands.argument(name, ITEM_TYPE).localized(CoreLang.COMMAND_ARGUMENT_NAME_ITEM_TYPE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Material> blockType(@NotNull String name) {
        return Commands.argument(name, BLOCK_TYPE).localized(CoreLang.COMMAND_ARGUMENT_NAME_BLOCK_TYPE);
    }

    @NotNull
    public static ArgumentNodeBuilder<Enchantment> enchantment(@NotNull String name) {
        return Commands.argument(name, ENCHANTMENT).localized(CoreLang.COMMAND_ARGUMENT_NAME_ENCHANTMENT);
    }

    @NotNull
    public static ArgumentNodeBuilder<World> world(@NotNull String name) {
        return Commands.argument(name, WORLD).localized(CoreLang.COMMAND_ARGUMENT_NAME_WORLD);
    }
}
