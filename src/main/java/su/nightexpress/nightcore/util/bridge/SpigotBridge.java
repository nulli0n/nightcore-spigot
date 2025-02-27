package su.nightexpress.nightcore.util.bridge;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("UnstableApiUsage")
public class SpigotBridge implements Software {

    private static final String FIELD_COMMAND_MAP    = "commandMap";
    private static final String FIELD_KNOWN_COMMANDS = "knownCommands";

    private static SimpleCommandMap commandMap;
    private static AtomicInteger entityCounter;

    @Override
    public boolean initialize(@NotNull NightCore core) {
        loadCommandMap();
        loadEntityCounter(core);
        return true;
    }

    private static void loadCommandMap() {
        commandMap = (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer(), FIELD_COMMAND_MAP);
    }

    private static void loadEntityCounter(NightCore core) {
        Class<?> entityClass = Reflex.getClass("net.minecraft.world.entity", "Entity");
        if (entityClass == null) {
            core.error("Could not find NMS Entity class!");
            return;
        }

        String fieldName = "c";
        if (Version.isAtLeast(Version.V1_19_R3) && Version.isBehind(Version.MC_1_20_6)) {
            fieldName = "d";
        }

        Object object = Reflex.getFieldValue(entityClass, fieldName);
        if (!(object instanceof AtomicInteger atomicInteger)) {
            if (object == null) {
                core.error("Could not find entity counter field!");
            }
            else core.error("Field '" + fieldName + "' in " + entityClass.getName() + " class is " + object.getClass().getName()  + " (expected AtomicInteger)");
            return;
        }

        entityCounter = atomicInteger;
    }

    @Override
    @NotNull
    public String getName() {
        return "spigot-bridge";
    }

    @Override
    public boolean isSpigot() {
        return true;
    }

    @Override
    public boolean isPaper() {
        return false;
    }

    @NotNull
    @Override
    public InventoryView createView(@NotNull InventoryViewBuilder<?> builder, @NotNull String title, @NotNull Player player) {
        Reflex.setFieldValue(builder, "title", NightMessage.asLegacy(title));
        return builder.build(player);//menuType.typed().builder().title(title).build(player);
    }

    @NotNull
    @Override
    public SimpleCommandMap getCommandMap() {
        //SimpleCommandMap commandMap = (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer(), FIELD_COMMAND_MAP);
        if (commandMap == null) throw new IllegalStateException("Command map is null!");

        return commandMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public Map<String, Command> getKnownCommands(@NotNull SimpleCommandMap commandMap) {
        Map<String, Command> knownCommands = (Map<String, Command>) Reflex.getFieldValue(commandMap, FIELD_KNOWN_COMMANDS);
        return knownCommands == null ? Collections.emptyMap() : knownCommands;
    }

    @Override
    public int nextEntityId() {
        return entityCounter.incrementAndGet();
    }
}
