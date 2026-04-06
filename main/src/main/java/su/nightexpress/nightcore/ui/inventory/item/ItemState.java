package su.nightexpress.nightcore.ui.inventory.item;

import org.bukkit.Material;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class ItemState {

    @Deprecated
    public static final String ID_DEFAULT = "default";

    public static final ItemState INVISIBLE = new ItemState(NightItem.fromType(Material.AIR), null, context -> false, null);

    private final NightItem          icon;
    private final MenuItemAction     action;
    private final ItemStateCondition condition;
    private final DisplayModifier    displayModifier;

    @Deprecated
    private String name;

    @Deprecated
    public ItemState(@NonNull String name, @NonNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition, @Nullable DisplayModifier displayModifier) {
        this(icon, action, condition, displayModifier);
        this.name = name;
    }

    public ItemState(@NonNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition, @Nullable DisplayModifier displayModifier) {
        this.icon = icon;
        this.action = action;
        this.condition = condition;
        this.displayModifier = displayModifier;
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static ItemState defaultState(@NonNull NightItem icon, @Nullable MenuItemAction action, @Nullable ItemStateCondition condition, @Nullable DisplayModifier displayModifier) {
        return new ItemState(icon, action, condition, displayModifier);
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Builder defaultBuilder() {
        return builder(ID_DEFAULT);
    }

    @NonNull
    @Deprecated(forRemoval = true)
    public static Builder builder(@NonNull String name) {
        return new Builder(name);
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public void performAction(@NonNull ActionContext context) {
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

    public boolean isVisibleFor(@NonNull ViewerContext context) {
        return this.isVisible() && (this.condition == null || this.condition.canSeenBy(context));
    }

    public void modifyDisplay(@NonNull ViewerContext context, @NonNull NightItem item) {
        if (this.displayModifier != null) {
            this.displayModifier.modify(context, item);
        }
    }

    @Nullable
    @Deprecated
    public String getName() {
        return this.name;
    }

    @NonNull
    public NightItem getIcon() {
        return this.icon.copy();
    }

    @Nullable
    public MenuItemAction getAction() {
        return this.action;
    }

    @Nullable
    public ItemStateCondition getCondition() {
        return this.condition;
    }

    @Nullable
    public DisplayModifier getDisplayModifier() {
        return this.displayModifier;
    }

    public static class Builder {

        private String name;

        private NightItem          icon;
        private MenuItemAction     action;
        private ItemStateCondition condition;
        private DisplayModifier displayModifier;

        @Deprecated
        Builder(@NonNull String name) {
            this.name = name;
        }

        Builder() {

        }

        @NonNull
        public Builder icon(@NonNull NightItem icon) {
            this.icon = icon;
            return this;
        }

        @NonNull
        public Builder action(@Nullable MenuItemAction action) {
            this.action = action;
            return this;
        }

        @NonNull
        public Builder condition(@Nullable ItemStateCondition condition) {
            this.condition = condition;
            return this;
        }

        @NonNull
        public Builder displayModifier(@Nullable DisplayModifier displayModifier) {
            this.displayModifier = displayModifier;
            return this;
        }

        @NonNull
        public ItemState build() {
            return new ItemState(this.name, this.icon, this.action, this.condition, this.displayModifier);
        }
    }
}
