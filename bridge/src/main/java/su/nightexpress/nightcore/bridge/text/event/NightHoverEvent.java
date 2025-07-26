package su.nightexpress.nightcore.bridge.text.event;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class NightHoverEvent<V> {

    @NotNull
    public static NightHoverEvent<NightComponent> showText(@NotNull NightComponent text) {
        return new NightHoverEvent<>(Action.SHOW_TEXT, text);
    }

    @NotNull
    public static NightHoverEvent<ItemStack> showItem(@NotNull ItemStack itemStack) {
        return new NightHoverEvent<>(Action.SHOW_ITEM, itemStack);
    }

    @NotNull
    public static <V> NightHoverEvent<V> hoverEvent(@NotNull Action<V> action, @NotNull V value) {
        return new NightHoverEvent<>(action, value);
    }

    private final Action<V> action;
    private final V         value;

    private NightHoverEvent(@NotNull Action<V> action, @NotNull V value) {
        this.action = action;
        this.value = value;
    }

    @NotNull
    public Action<V> action() {
        return this.action;
    }

    @NotNull
    public V value() {
        return this.value;
    }

/*    @NotNull
    public NightHoverEvent<V> value(@NotNull V value) {
        return new NightHoverEvent<>(this.action, value);
    }*/

/*    public static final class ShowItem {

        private final ItemStack itemStack;

        @NotNull
        public static ShowItem showItem(@NotNull ItemStack itemStack) {
            return new ShowItem(itemStack);
        }

        private ShowItem(@NotNull ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @NotNull
        public ItemStack item() {
            return this.itemStack;
        }
    }*/

    public static final class Action<V> {

        public static final Action<NightComponent> SHOW_TEXT = new Action<>(NightComponent.class, "show_text");
        public static final Action<ItemStack>               SHOW_ITEM = new Action<>(ItemStack.class, "show_item");

        public static Action<?>[] values() {
            return new Action[]{SHOW_TEXT, SHOW_ITEM};
        }

        @Nullable
        public static Action<?> byName(@NotNull String name) {
            for (Action<?> action : Action.values()) {
                if (name.equalsIgnoreCase(action.name())) {
                    return action;
                }
            }
            return null;
        }

        private final Class<V> type;
        private final String name;

        Action(@NotNull Class<V> type, @NotNull String name) {
            this.type = type;
            this.name = name;
        }

        @NotNull
        public String name() {
            return this.name;
        }

        @NotNull
        public Class<V> type() {
            return this.type;
        }
    }
}
