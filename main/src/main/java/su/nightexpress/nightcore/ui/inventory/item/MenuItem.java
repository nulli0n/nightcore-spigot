package su.nightexpress.nightcore.ui.inventory.item;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    private final ItemState       defaultState;
    private final List<ItemState> conditionalStates;
    private final int[]           slots;

    private MenuItem(@NotNull ItemState defaultState, @NotNull List<ItemState> conditionalStates, int[] slots) {
        this.defaultState = defaultState;
        this.conditionalStates = conditionalStates;
        this.slots = slots;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public static MenuItem background(@NotNull Material material, int... slots) {
        return builder().defaultState(NightItem.fromType(material), null).slots(slots).build();
    }

    @NotNull
    public ItemState resolveState(@NotNull ViewerContext context) {
        for (ItemState state : this.conditionalStates) {
            if (state.isVisibleFor(context)) {
                return state;
            }
        }
        if (this.defaultState.isVisibleFor(context)) {
            return this.defaultState;
        }
        return ItemState.INVISIBLE;
    }

    @NotNull
    public List<ItemState> getAllStates() {
        List<ItemState> states = new ArrayList<>();
        states.add(this.defaultState);
        states.addAll(this.conditionalStates);
        return states;
    }

    @NotNull
    public ItemState getDefaultState() {
        return this.defaultState;
    }

    @NotNull
    public List<ItemState> getConditionalStates() {
        return this.conditionalStates;
    }

    public int[] getSlots() {
        return this.slots;
    }

    public static class Builder {

        private final List<ItemState> states = new ArrayList<>();

        private NightItem          defaultIcon;
        private MenuItemAction     defaultAction;
        private ItemStateCondition defaultCondition       = context -> true;
        private DisplayModifier    defaultDisplayModifier = null;
        private int[]              slots                  = new int[0];

        Builder() {}

        @NotNull
        public Builder state(@NotNull ItemState state) {
            this.states.add(state);
            return this;
        }

        @NotNull
        public Builder defaultState(@NotNull ItemState state) {
            this.defaultIcon = state.getIcon();
            this.defaultAction = state.getAction();
            this.defaultCondition = state.getCondition();
            this.defaultDisplayModifier = state.getDisplayModifier();
            return this;
        }

        @NotNull
        public Builder defaultState(@NotNull NightItem icon) {
            return this.defaultState(icon, null);
        }

        @NotNull
        public Builder defaultState(@NotNull NightItem icon, @Nullable MenuItemAction action) {
            return this.defaultState(icon, action, null);
        }

        @NotNull
        public Builder defaultState(@NotNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition) {
            return this.defaultState(icon, action, condition, null);
        }

        @NotNull
        public Builder defaultState(@NotNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition, @Nullable DisplayModifier displayModifier) {
            return this.defaultState(ItemState.defaultState(icon, action, condition, displayModifier));
        }

        @NotNull
        public Builder defaultVisibility(@NotNull ItemStateCondition condition) {
            this.defaultCondition = condition;
            return this;
        }

        @NotNull
        public Builder slots(int... slots) {
            this.slots = slots;
            return this;
        }

        @NotNull
        public MenuItem build() {
            return new MenuItem(new ItemState(ItemState.ID_DEFAULT, this.defaultIcon, this.defaultAction, this.defaultCondition, this.defaultDisplayModifier), this.states, this.slots);
        }
    }
}
