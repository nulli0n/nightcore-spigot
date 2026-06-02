package su.nightexpress.nightcore.commands;

import su.nightexpress.nightcore.commands.argument.type.BlockTypeArgumentType;
import su.nightexpress.nightcore.commands.argument.type.BoolArgumentType;
import su.nightexpress.nightcore.commands.argument.type.EnchantmentArgumentType;
import su.nightexpress.nightcore.commands.argument.type.ItemTypeArgumentType;
import su.nightexpress.nightcore.commands.argument.type.PlayerArgumentType;
import su.nightexpress.nightcore.commands.argument.type.PlayerExactArgumentType;
import su.nightexpress.nightcore.commands.argument.type.PlayerNameArgumentType;
import su.nightexpress.nightcore.commands.argument.type.StringArgumentType;
import su.nightexpress.nightcore.commands.argument.type.WorldArgumentType;

public class ArgumentTypes {

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

    private ArgumentTypes() {
    }

}
