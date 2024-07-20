package su.nightexpress.nightcore.command.experimental.argument;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.builder.ArgumentBuilder;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;

public class ArgumentTypes {

    public static final ArgumentParser<String>           STRING         = (string, context) -> string;
    public static final ArgumentParser<Boolean>          BOOLEAN        = (string, context) -> Boolean.parseBoolean(string);
    public static final ArgumentParser<Integer>          INTEGER        = (string, context) -> NumberUtil.parseInteger(string).orElse(null);
    public static final ArgumentParser<Integer>          INTEGER_ABS    = (string, context) -> NumberUtil.parseInteger(string).map(Math::abs).orElse(null);
    public static final ArgumentParser<Double>           DOUBLE         = (string, context) -> NumberUtil.parseDouble(string).orElse(null);
    public static final ArgumentParser<Double>           DOUBLE_ABS     = (string, context) -> NumberUtil.parseDouble(string).map(Math::abs).orElse(null);
    public static final ArgumentParser<Player>           PLAYER         = (string, context) -> Players.getPlayer(string);
    public static final ArgumentParser<World>            WORLD          = (string, context) -> Bukkit.getWorld(string);
    public static final ArgumentParser<Material>         MATERIAL       = (string, context) -> BukkitThing.getMaterial(string);
    public static final ArgumentParser<Material>         ITEM_MATERIAL  = (string, context) -> {
        Material material = BukkitThing.getMaterial(string);
        return material == null || !material.isItem() ? null : material;
    };
    public static final ArgumentParser<Material>         BLOCK_MATERIAL = (string, context) -> {
        Material material = BukkitThing.getMaterial(string);
        return material == null || !material.isBlock() ? null : material;
    };
    public static final ArgumentParser<Enchantment>      ENCHANTMENT    = (string, context) -> BukkitThing.getEnchantment(string);
    //public static final ArgumentParser<PotionEffectType> POTION_EFFECT  = BukkitThing::getPotionEffect;
    //public static final ArgumentParser<Attribute>        ATTRIBUTE      = BukkitThing::getAttribute;

    @NotNull
    public static ArgumentBuilder<String> string(@NotNull String name) {
        return CommandArgument.builder(name, STRING).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NotNull
    public static ArgumentBuilder<Boolean> bool(@NotNull String name) {
        return CommandArgument.builder(name, BOOLEAN).localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC);
    }

    @NotNull
    public static ArgumentBuilder<Integer> integer(@NotNull String name) {
        return CommandArgument.builder(name, INTEGER)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_NUMBER_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Integer> integerAbs(@NotNull String name) {
        return CommandArgument.builder(name, INTEGER_ABS)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_NUMBER_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Double> decimal(@NotNull String name) {
        return CommandArgument.builder(name, DOUBLE)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_NUMBER_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Double> decimalAbs(@NotNull String name) {
        return CommandArgument.builder(name, DOUBLE_ABS)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_GENERIC)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_NUMBER_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Player> player(@NotNull String name) {
        return CommandArgument.builder(name, PLAYER)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_PLAYER_ARGUMENT)
            .withSamples(tabContext -> Players.playerNames(tabContext.getPlayer()));
    }

    @NotNull
    public static ArgumentBuilder<String> playerName(@NotNull String name) {
        return CommandArgument.builder(name, STRING)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_PLAYER)
            .withSamples(tabContext -> Players.playerNames(tabContext.getPlayer()));
    }

    @NotNull
    public static ArgumentBuilder<World> world(@NotNull String name) {
        return CommandArgument.builder(name, WORLD)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_WORLD)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_WORLD_ARGUMENT)
            .withSamples(tabContext -> Lists.worldNames());
    }

    @NotNull
    public static ArgumentBuilder<Material> material(@NotNull String name) {
        return CommandArgument.builder(name, MATERIAL)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Material> itemMaterial(@NotNull String name) {
        return CommandArgument.builder(name, ITEM_MATERIAL)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ITEM_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Material> blockMaterial(@NotNull String name) {
        return CommandArgument.builder(name, BLOCK_MATERIAL)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_BLOCK_MATERIAL)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_MATERIAL_ARGUMENT);
    }

    @NotNull
    public static ArgumentBuilder<Enchantment> enchantment(@NotNull String name) {
        return CommandArgument.builder(name, ENCHANTMENT)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ENCHANTMENT)
            .customFailure(CoreLang.ERROR_COMMAND_INVALID_ENCHANTMENT_ARGUMENT);
    }

    /*@NotNull
    public static ArgumentBuilder<PotionEffectType> potionEffect(@NotNull String name) {
        return CommandArgument.builder(name, POTION_EFFECT)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_POTION_EFFECT)
            .customFailure(CoreLang.ERROR_INVALID_POTION_EFFECT);
    }

    @NotNull
    public static ArgumentBuilder<Attribute> attribute(@NotNull String name) {
        return CommandArgument.builder(name, ATTRIBUTE)
            .localized(CoreLang.COMMAND_ARGUMENT_NAME_ATTRIBUTE)
            .customFailure(CoreLang.ERROR_INVALID_ATTRIBUTE);
    }*/
}
