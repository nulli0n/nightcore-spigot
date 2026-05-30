package su.nightexpress.nightcore.ui.inventory.action;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Deprecated
public class ActionRegistry {

    private final Map<String, MenuItemAction> actionMap;

    public ActionRegistry() {
        this.actionMap = new HashMap<>();
    }

    public void register(@NonNull String name, @NonNull MenuItemAction action) {
        String id = LowerCase.INTERNAL.apply(name);
        if (this.actionMap.containsKey(id)) {
            // TODO Warn or exception
            return;
        }
        this.actionMap.put(id, action);
    }

    @Nullable
    public MenuItemAction getById(@NonNull String id) {
        return this.actionMap.get(LowerCase.INTERNAL.apply(id));
    }

    @NonNull
    public Optional<MenuItemAction> byId(@NonNull String id) {
        return Optional.ofNullable(this.getById(id));
    }
}
