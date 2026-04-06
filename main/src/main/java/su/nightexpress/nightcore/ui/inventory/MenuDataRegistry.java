package su.nightexpress.nightcore.ui.inventory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.action.NamedAction;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.condition.NamedCondition;
import su.nightexpress.nightcore.ui.inventory.item.DisplayModifier;
import su.nightexpress.nightcore.ui.inventory.display.NamedDisplayModifier;
import su.nightexpress.nightcore.util.registry.StringRegistry;

public class MenuDataRegistry {

    private final StringRegistry<MenuItemAction>     actionRegistry;
    private final StringRegistry<ItemStateCondition> conditionRegistry;
    private final StringRegistry<DisplayModifier>    displayModifierRegistry;

    public MenuDataRegistry() {
        this.actionRegistry = new StringRegistry<>();
        this.conditionRegistry = new StringRegistry<>();
        this.displayModifierRegistry = new StringRegistry<>();
    }

    public void registerAction(@NonNull NamedAction action) {
        this.registerAction(action.name(), action);
    }

    public void registerAction(@NonNull String id, @NonNull MenuItemAction action) {
        this.actionRegistry.register(id, action);
    }

    @Nullable
    public MenuItemAction getAction(@NonNull String id) {
        return this.actionRegistry.getByKey(id);
    }



    public void registerCondition(@NonNull NamedCondition condition) {
        this.registerCondition(condition.name(), condition);
    }

    public void registerCondition(@NonNull String id, @NonNull ItemStateCondition condition) {
        this.conditionRegistry.register(id, condition);
    }

    @Nullable
    public ItemStateCondition getCondition(@NonNull String id) {
        return this.conditionRegistry.getByKey(id);
    }



    public void registerDisplayModifier(@NonNull NamedDisplayModifier modifier) {
        this.registerDisplayModifier(modifier.name(), modifier);
    }

    public void registerDisplayModifier(@NonNull String id, @NonNull DisplayModifier modifier) {
        this.displayModifierRegistry.register(id, modifier);
    }

    @Nullable
    public DisplayModifier getDisplayModifier(@NonNull String id) {
        return this.displayModifierRegistry.getByKey(id);
    }




    @NonNull
    public StringRegistry<MenuItemAction> getActionRegistry() {
        return this.actionRegistry;
    }

    @NonNull
    public StringRegistry<ItemStateCondition> getConditionRegistry() {
        return this.conditionRegistry;
    }

    @NonNull
    public StringRegistry<DisplayModifier> getDisplayModifierRegistry() {
        return this.displayModifierRegistry;
    }
}
