package su.nightexpress.nightcore.ui.inventory.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ActionRegistry {

    private final Map<String, MenuItemAction> actionMap;

    public ActionRegistry() {
        this.actionMap = new HashMap<>();
    }

    public void register(@NotNull String name, @NotNull MenuItemAction action) {
        String id = LowerCase.INTERNAL.apply(name);
        if (this.actionMap.containsKey(id)) {
            // TODO Warn or exception
            return;
        }
        this.actionMap.put(id, action);
    }

    @Nullable
    public MenuItemAction getById(@NotNull String id) {
        return this.actionMap.get(LowerCase.INTERNAL.apply(id));
    }

    @NotNull
    public Optional<MenuItemAction> byId(@NotNull String id) {
        return Optional.ofNullable(this.getById(id));
    }
}
