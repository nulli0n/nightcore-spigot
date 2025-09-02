package su.nightexpress.nightcore.commands.argument.type;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.List;

public class EnchantmentArgumentType implements ArgumentType<Enchantment> {

    private static final List<String> EXAMPLES = BukkitThing.getAsStrings(RegistryType.ENCHANTMENT);

    @Override
    @NotNull
    public Enchantment parse(@NotNull CommandContextBuilder contextBuilder, @NotNull String string) throws CommandSyntaxException {
        Enchantment enchantment = BukkitThing.getEnchantment(string);
        if (enchantment == null) throw CommandSyntaxException.custom(CoreLang.COMMAND_SYNTAX_INVALID_ENCHANTMENT);

        return enchantment;
    }

    @Override
    @NotNull
    public List<String> suggest(@NotNull ArgumentReader reader, @NotNull CommandContext context) {
        return EXAMPLES;
    }
}
