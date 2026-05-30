package su.nightexpress.nightcore.bridge.text.event;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class NightHoverEvent<V> {

    @NonNull
    public static NightHoverEvent<NightComponent> showText(@NonNull NightComponent text) {
        return new NightHoverEvent<>(Action.SHOW_TEXT, text);
    }

    @NonNull
    public static NightHoverEvent<ItemStack> showItem(@NonNull ItemStack itemStack) {
        return new NightHoverEvent<>(Action.SHOW_ITEM, itemStack);
    }

    @NonNull
    public static <V> NightHoverEvent<V> hoverEvent(@NonNull Action<V> action, @NonNull V value) {
        return new NightHoverEvent<>(action, value);
    }

    private final Action<V> action;
    private final V         value;

    private NightHoverEvent(@NonNull Action<V> action, @NonNull V value) {
        this.action = action;
        this.value = value;
    }

    @NonNull
    public Action<V> action() {
        return this.action;
    }

    @NonNull
    public V value() {
        return this.value;
    }

    /*    @NonNull
    public NightHoverEvent<V> value(@NonNull V value) {
        return new NightHoverEvent<>(this.action, value);
    }*/

    /*    public static final class ShowItem {
    
        private final ItemStack itemStack;
    
        @NonNull
        public static ShowItem showItem(@NonNull ItemStack itemStack) {
            return new ShowItem(itemStack);
        }
    
        private ShowItem(@NonNull ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    
        @NonNull
        public ItemStack item() {
            return this.itemStack;
        }
    }*/

    public static final class Action<V> {

        public static final Action<NightComponent> SHOW_TEXT = new Action<>(NightComponent.class, "show_text");
        public static final Action<ItemStack>      SHOW_ITEM = new Action<>(ItemStack.class, "show_item");

        public static Action<?>[] values() {
            return new Action[]{SHOW_TEXT, SHOW_ITEM};
        }

        @Nullable
        public static Action<?> byName(@NonNull String name) {
            for (Action<?> action : Action.values()) {
                if (name.equalsIgnoreCase(action.name())) {
                    return action;
                }
            }
            return null;
        }

        private final Class<V> type;
        private final String   name;

        Action(@NonNull Class<V> type, @NonNull String name) {
            this.type = type;
            this.name = name;
        }

        @NonNull
        public String name() {
            return this.name;
        }

        @NonNull
        public Class<V> type() {
            return this.type;
        }
    }
}
