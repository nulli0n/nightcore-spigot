package su.nightexpress.nightcore.ui.menu.confirmation;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@Deprecated
public class Confirmation {

    private final ItemClick onAccept;
    private final     ItemClick onReturn;
    private final     NightItem icon;
    private final     boolean   returnOnAccept;

    public Confirmation(@Nullable ItemClick onAccept, @Nullable ItemClick onReturn, @Nullable NightItem icon, boolean returnOnAccept) {
        this.onAccept = onAccept;
        this.onReturn = onReturn;
        this.icon = icon;
        this.returnOnAccept = returnOnAccept;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public void handleAccept(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        if (this.onAccept == null) return;

        this.onAccept.onClick(viewer, event);

        if (this.returnOnAccept) {
            this.handleReturn(viewer, event);
        }
    }

    public void handleReturn(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        if (this.onReturn == null) return;

        this.onReturn.onClick(viewer, event);
    }

    @Nullable
    public NightItem getIcon() {
        return this.icon;
    }

    public static class Builder {

        private ItemClick onAccept;
        private ItemClick onReturn;
        private NightItem icon;
        private boolean returnOnAccept;

        @NotNull
        public Confirmation build() {
            return new Confirmation(this.onAccept, this.onReturn, this.icon, this.returnOnAccept);
        }

        @NotNull
        public Builder onAccept(@Nullable ItemClick onAccept) {
            this.onAccept = onAccept;
            return this;
        }

        @NotNull
        public Builder onReturn(@Nullable ItemClick onReturn) {
            this.onReturn = onReturn;
            return this;
        }

        @NotNull
        public Builder setIcon(@Nullable NightItem icon) {
            this.icon = icon;
            return this;
        }

        @NotNull
        public Builder returnOnAccept(boolean returnOnAccept) {
            this.returnOnAccept = returnOnAccept;
            return this;
        }
    }
}
