package su.nightexpress.nightcore.ui.menu.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Deprecated
public class ItemOptions {

    private final Predicate<MenuViewer>             visibilityPolicy;
    private final BiConsumer<MenuViewer, NightItem> displayModifier;

    public ItemOptions(@Nullable Predicate<MenuViewer> visibilityPolicy,
                       @Nullable BiConsumer<MenuViewer, NightItem> displayModifier) {
        this.visibilityPolicy = visibilityPolicy;
        this.displayModifier = displayModifier;
    }

    public boolean canSee(@NotNull MenuViewer viewer) {
        return this.visibilityPolicy == null || this.visibilityPolicy.test(viewer);
    }

    public void modifyDisplay(@NotNull MenuViewer viewer, @NotNull NightItem item) {
        if (this.displayModifier != null) {
            this.displayModifier.accept(viewer, item);
        }
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public Predicate<MenuViewer> getVisibilityPolicy() {
        return this.visibilityPolicy;
    }

    @Nullable
    public BiConsumer<MenuViewer, NightItem> getDisplayModifier() {
        return this.displayModifier;
    }

    public static class Builder {

        private Predicate<MenuViewer>             visibilityPolicy;
        private BiConsumer<MenuViewer, NightItem> displayModifier;

        public Builder() {

        }

        @NotNull
        public ItemOptions build() {
            return new ItemOptions(this.visibilityPolicy, this.displayModifier);
        }

        @NotNull
        public Builder setVisibilityPolicy(@Nullable Predicate<MenuViewer> visibilityPolicy) {
            this.visibilityPolicy = visibilityPolicy;
            return this;
        }

        @NotNull
        public Builder setDisplayModifier(@Nullable BiConsumer<MenuViewer, NightItem> displayModifier) {
            this.displayModifier = displayModifier;
            return this;
        }
    }
}
