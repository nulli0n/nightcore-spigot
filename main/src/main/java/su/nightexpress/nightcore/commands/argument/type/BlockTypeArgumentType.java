package su.nightexpress.nightcore.commands.argument.type;

import org.bukkit.Material;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.BukkitThing;

import java.util.List;

public class BlockTypeArgumentType implements ArgumentType<Material>, SuggestionsProvider {

    private static final List<String> EXAMPLES = BukkitThing.getMaterials().stream().filter(Material::isBlock).map(
        BukkitThing::getValue).toList();

    @Override
    @NonNull
    public Material parse(@NonNull CommandContextBuilder contextBuilder,
                          @NonNull String string) throws CommandSyntaxException {
        Material material = BukkitThing.getMaterial(string);
        if (material == null || !material.isBlock()) throw CommandSyntaxException.custom(
            CoreLang.COMMAND_SYNTAX_INVALID_BLOCK);

        return material;
    }

    @Override
    @NonNull
    public List<String> suggest(@NonNull ArgumentReader reader, @NonNull CommandContext context) {
        return EXAMPLES;
    }
}
