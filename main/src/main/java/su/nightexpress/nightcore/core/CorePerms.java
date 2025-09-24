package su.nightexpress.nightcore.core;

import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

public class CorePerms {

    public static final String PREFIX         = "nightcore.";
    public static final String PREFIX_COMMAND = PREFIX + "command.";

    public static final UniPermission PLUGIN  = new UniPermission(PREFIX + Placeholders.WILDCARD);
    public static final UniPermission COMMAND = new UniPermission(PREFIX_COMMAND + Placeholders.WILDCARD);

    public static final UniPermission COMMAND_RELOAD         = new UniPermission(PREFIX_COMMAND + "reload");
    public static final UniPermission COMMAND_CHECK_PERM     = new UniPermission(PREFIX_COMMAND + "checkperm");
    public static final UniPermission COMMAND_DUMP_ITEM      = new UniPermission(PREFIX_COMMAND + "dumpitem");
    public static final UniPermission COMMAND_ECONOMY_BRIDGE = new UniPermission(PREFIX_COMMAND + "economybridge");

    @Deprecated
    public static final UniPermission COMMAND_FLAGS = new UniPermission("nightcore.command.flags");

    static {
        PLUGIN.addChildren(COMMAND);

        COMMAND.addChildren(
            COMMAND_RELOAD,
            COMMAND_CHECK_PERM,
            COMMAND_DUMP_ITEM,
            COMMAND_ECONOMY_BRIDGE
        );
    }
}
