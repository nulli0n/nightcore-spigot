package su.nightexpress.nightcore.menu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.menu.MenuViewer;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ItemOptions {

    private Predicate<MenuViewer> visibilityPolicy;
    private Predicate<MenuViewer> weakPolicy;
    private BiConsumer<MenuViewer, ItemStack> displayModifier;

    public ItemOptions() {
        this(null, null, null);
    }

    public ItemOptions(@Nullable Predicate<MenuViewer> visibilityPolicy,
                       @Nullable Predicate<MenuViewer> weakPolicy,
                       @Nullable BiConsumer<MenuViewer, ItemStack> displayModifier) {
        this.setVisibilityPolicy(visibilityPolicy);
        this.setWeakPolicy(weakPolicy);
        this.setDisplayModifier(displayModifier);
    }

    @NotNull
    public static ItemOptions personalWeak(@NotNull Player player) {
        Predicate<MenuViewer> visibility = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        Predicate<MenuViewer> weak = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        return new ItemOptions(visibility, weak, null);
    }

    @NotNull
    public static ItemOptions personalPermanent(@NotNull Player player) {
        Predicate<MenuViewer> visibility = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        return new ItemOptions(visibility, null, null);
    }

    public boolean canSee(@NotNull MenuViewer viewer) {
        Predicate<MenuViewer> policy = this.getVisibilityPolicy();
        return policy == null || policy.test(viewer);
    }

    public boolean canBeDestroyed(@NotNull MenuViewer viewer) {
        Predicate<MenuViewer> policy = this.getWeakPolicy();
        return policy != null && policy.test(viewer);
    }

    public void modifyDisplay(@NotNull MenuViewer viewer, @NotNull ItemStack item) {
        BiConsumer<MenuViewer, ItemStack> displayModifier = this.getDisplayModifier();
        if (displayModifier != null) {
            displayModifier.accept(viewer, item);
        }
    }

    @Nullable
    public Predicate<MenuViewer> getVisibilityPolicy() {
        return visibilityPolicy;
    }

    @NotNull
    public ItemOptions setVisibilityPolicy(@Nullable Predicate<MenuViewer> visibilityPolicy) {
        this.visibilityPolicy = visibilityPolicy;
        return this;
    }

    @NotNull
    public ItemOptions addVisibilityPolicy(@NotNull Predicate<MenuViewer> visibilityPolicy) {
        if (this.visibilityPolicy == null) {
            this.setVisibilityPolicy(visibilityPolicy);
        }
        else {
            this.visibilityPolicy = this.visibilityPolicy.and(visibilityPolicy);
        }

        return this;
    }

    @Nullable
    public Predicate<MenuViewer> getWeakPolicy() {
        return weakPolicy;
    }

    @NotNull
    public ItemOptions setWeakPolicy(@Nullable Predicate<MenuViewer> weakPolicy) {
        this.weakPolicy = weakPolicy;
        return this;
    }

    @Nullable
    public BiConsumer<MenuViewer, ItemStack> getDisplayModifier() {
        return displayModifier;
    }

    @NotNull
    public ItemOptions setDisplayModifier(@Nullable BiConsumer<MenuViewer, ItemStack> displayModifier) {
        this.displayModifier = displayModifier;
        return this;
    }

    @NotNull
    public ItemOptions addDisplayModifier(@NotNull BiConsumer<MenuViewer, ItemStack> displayModifier) {
        if (this.displayModifier == null) {
            this.setDisplayModifier(displayModifier);
        }
        else {
            this.displayModifier = this.displayModifier.andThen(displayModifier);
        }
        return this;
    }
}
