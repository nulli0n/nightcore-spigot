package su.nightexpress.nightcore.command.experimental.argument;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.builder.ArgumentBuilder;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;

@Deprecated
public class ArgumentTypes {

    public static final ArgumentParser<String>   STRING              = (string, context) -> string;
    public static final ArgumentParser<Boolean>  BOOLEAN             = (string, context) -> Boolean.parseBoolean(
        string);
    public static final ArgumentParser<Integer>  INTEGER             = (string, context) -> NumberUtil.parseInteger(
        string).orElse(null);
    public static final ArgumentParser<Integer>  INTEGER_ABS         = (string, context) -> NumberUtil.parseInteger(
        string).map(Math::abs).orElse(null);
    public static final ArgumentParser<Integer>  INTEGER_COMPACT     = (string, context) -> NumberUtil.parseIntCompact(
        string).orElse(null);
    public static final ArgumentParser<Integer>  INTEGER_COMPACT_ABS = (string, context) -> NumberUtil.parseIntCompact(
        string).map(Math::abs).orElse(null);
    public static final ArgumentParser<Double>   DOUBLE              = (string, context) -> NumberUtil.parseDouble(
        string).orElse(null);
    public static final ArgumentParser<Double>   DOUBLE_ABS          = (string, context) -> NumberUtil.parseDouble(
        string).map(Math::abs).orElse(null);
    public static final ArgumentParser<Double>   DOUBLE_COMPACT      = (string, context) -> NumberUtil.parseCompact(
        string).orElse(null);
    public static final ArgumentParser<Double>   DOUBLE_COMPACT_ABS  = (string, context) -> NumberUtil.parseCompact(
        string).map(Math::abs).orElse(null);
    public static final ArgumentParser<Player>   PLAYER              = (string, context) -> Players.getPlayer(string);
    public static final ArgumentParser<World>    WORLD               = (string, context) -> Bukkit.getWorld(string);
    @Deprecated
    public static final ArgumentParser<Material> MATERIAL            = (string, context) -> BukkitThing.getMaterial(
        string);

    public static final ArgumentParser<Material> ITEM_MATERIAL = (string, context) -> {
        Material material = BukkitThing.getMaterial(string);
        return material == null || !material.isItem() ? null : material;
    };

    public static final ArgumentParser<Material> BLOCK_MATERIAL = (string, context) -> {
        Material material = BukkitThing.getMaterial(string);
        return material == null || !material.isBlock() ? null : material;
    };

    public static final ArgumentParser<Enchantment> ENCHANTMENT = (string, context) -> BukkitThing.getEnchantment(
        string);
    //public static final ArgumentParser<PotionEffectType> POTION_EFFECT  = BukkitThing::getPotionEffect;
    //public static final ArgumentParser<Attribute>        ATTRIBUTE      = BukkitThing::getAttribute;

    @NonNull
    public static ArgumentBuilder<String> string(@NonNull String name) {
        return CommandArgument.builder(name, STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NonNull
    public static ArgumentBuilder<Boolean> bool(@NonNull String name) {
        return CommandArgument.builder(name, BOOLEAN).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NonNull
    public static ArgumentBuilder<Integer> integer(@NonNull String name) {
        return numeric(name, INTEGER);
    }

    @NonNull
    public static ArgumentBuilder<Integer> integerAbs(@NonNull String name) {
        return numeric(name, INTEGER_ABS);
    }

    @NonNull
    public static ArgumentBuilder<Integer> integerCompact(@NonNull String name) {
        return numeric(name, INTEGER_COMPACT);
    }

    @NonNull
    public static ArgumentBuilder<Integer> integerCompactAbs(@NonNull String name) {
        return numeric(name, INTEGER_COMPACT_ABS);
    }

    @NonNull
    public static ArgumentBuilder<Double> decimal(@NonNull String name) {
        return numeric(name, DOUBLE);
    }

    @NonNull
    public static ArgumentBuilder<Double> decimalAbs(@NonNull String name) {
        return numeric(name, DOUBLE_ABS);
    }

    @NonNull
    public static ArgumentBuilder<Double> decimalCompact(@NonNull String name) {
        return numeric(name, DOUBLE_COMPACT);
    }

    @NonNull
    public static ArgumentBuilder<Double> decimalCompactAbs(@NonNull String name) {
        return numeric(name, DOUBLE_COMPACT_ABS);
    }

    @NonNull
    private static <T extends Number> ArgumentBuilder<T> numeric(@NonNull String name,
                                                                 @NonNull ArgumentParser<T> parser) {
        return CommandArgument.builder(name, parser)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_NUMBER_ARGUMENT);
    }

    @NonNull
    public static ArgumentBuilder<Player> player(@NonNull String name) {
        return CommandArgument.builder(name, PLAYER)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_PLAYER_ARGUMENT)
            .withSamples(tabContext -> Players.playerNames(tabContext.getPlayer()));
    }

    @NonNull
    public static ArgumentBuilder<String> playerName(@NonNull String name) {
        return CommandArgument.builder(name, STRING)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER)
            .withSamples(tabContext -> Players.playerNames(tabContext.getPlayer()));
    }

    @NonNull
    public static ArgumentBuilder<World> world(@NonNull String name) {
        return CommandArgument.builder(name, WORLD)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_WORLD)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_WORLD_ARGUMENT)
            .withSamples(tabContext -> BukkitThing.worldNames());
    }

    @NonNull
    public static ArgumentBuilder<Material> material(@NonNull String name) {
        return CommandArgument.builder(name, MATERIAL)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NonNull
    public static ArgumentBuilder<Material> itemMaterial(@NonNull String name) {
        return CommandArgument.builder(name, ITEM_MATERIAL)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ITEM_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NonNull
    public static ArgumentBuilder<Material> blockMaterial(@NonNull String name) {
        return CommandArgument.builder(name, BLOCK_MATERIAL)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_BLOCK_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NonNull
    public static ArgumentBuilder<ItemType> itemType(@NonNull String name) {
        return CommandArgument.builder(name, (string, context) -> BukkitThing.getItemType(string))
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ITEM_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NonNull
    public static ArgumentBuilder<BlockType> blockType(@NonNull String name) {
        return CommandArgument.builder(name, (string, context) -> BukkitThing.getBlockType(string))
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_BLOCK_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NonNull
    public static ArgumentBuilder<Enchantment> enchantment(@NonNull String name) {
        return CommandArgument.builder(name, ENCHANTMENT)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ENCHANTMENT)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_ENCHANTMENT_ARGUMENT);
    }

    /*@NonNull
    public static ArgumentBuilder<PotionEffectType> potionEffect(@NonNull String name) {
        return CommandArgument.builder(name, POTION_EFFECT)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_POTION_EFFECT)
            .customFailure(CoreLang.ERROR_INVALID_POTION_EFFECT);
    }
    
    @NonNull
    public static ArgumentBuilder<Attribute> attribute(@NonNull String name) {
        return CommandArgument.builder(name, ATTRIBUTE)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ATTRIBUTE)
            .customFailure(CoreLang.ERROR_INVALID_ATTRIBUTE);
    }*/
}
