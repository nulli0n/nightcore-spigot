package su.nightexpress.nightcore.ui.inventory.item;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class ItemState {

    public static final String ID_DEFAULT = "default";
    public static final ItemState INVISIBLE = new ItemState("_invisible_", NightItem.fromType(Material.AIR), null, context -> false, null);

    private final String             name;
    private final NightItem          icon;
    private final MenuItemAction     action;
    private final ItemStateCondition condition;
    private final DisplayModifier displayModifier;

    public ItemState(@NotNull String name, @NotNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition, @Nullable DisplayModifier displayModifier) {
        this.name = name;
        this.icon = icon;
        this.action = action;
        this.condition = condition == null ? context -> true : condition;
        this.displayModifier = displayModifier;
    }

    @NotNull
    public static ItemState defaultState(@NotNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition, @Nullable DisplayModifier displayModifier) {
        return new ItemState(ID_DEFAULT, icon, action, condition, displayModifier);
    }

    @NotNull
    public static Builder defaultBuilder() {
        return builder(ID_DEFAULT);
    }

    @NotNull
    public static Builder builder(@NotNull String name) {
        return new Builder(name);
    }

    public void performAction(@NotNull ActionContext context) {
        if (this.action != null) {
            this.action.execute(context);
        }
    }

    public boolean hasAction() {
        return this.action != null;
    }

    public boolean isVisible() {
        return this != INVISIBLE;
    }

    public boolean isVisibleFor(@NotNull ViewerContext context) {
        return this.isVisible() && this.condition.canSeenBy(context);
    }

    public void modifyDisplay(@NotNull ViewerContext context, @NotNull NightItem item) {
        if (this.displayModifier != null) {
            this.displayModifier.modify(context, item);
        }
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public NightItem getIcon() {
        return this.icon.copy();
    }

    @Nullable
    public MenuItemAction getAction() {
        return this.action;
    }

    @NotNull
    public ItemStateCondition getCondition() {
        return this.condition;
    }

    @Nullable
    public DisplayModifier getDisplayModifier() {
        return this.displayModifier;
    }

    public static class Builder {

        private final String name;

        private NightItem          icon;
        private MenuItemAction     action;
        private ItemStateCondition condition;
        private DisplayModifier displayModifier;

        Builder(@NotNull String name) {
            this.name = name;
        }

        @NotNull
        public Builder icon(@NotNull NightItem icon) {
            this.icon = icon;
            return this;
        }

        @NotNull
        public Builder action(@NotNull MenuItemAction action) {
            this.action = action;
            return this;
        }

        @NotNull
        public Builder condition(@NotNull ItemStateCondition condition) {
            this.condition = condition;
            return this;
        }

        @NotNull
        public Builder displayModifier(@NotNull DisplayModifier displayModifier) {
            this.displayModifier = displayModifier;
            return this;
        }

        @NotNull
        public ItemState build() {
            return new ItemState(this.name, this.icon, this.action, this.condition, this.displayModifier);
        }
    }
}
