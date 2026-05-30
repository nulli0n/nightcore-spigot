package su.nightexpress.nightcore.menu.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.menu.MenuViewer;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Deprecated
public class ItemOptions {

    private Predicate<MenuViewer>             visibilityPolicy;
    private Predicate<MenuViewer>             weakPolicy;
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

    @NonNull
    public static ItemOptions personalWeak(@NonNull Player player) {
        Predicate<MenuViewer> visibility = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        Predicate<MenuViewer> weak = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        return new ItemOptions(visibility, weak, null);
    }

    @NonNull
    public static ItemOptions personalPermanent(@NonNull Player player) {
        Predicate<MenuViewer> visibility = (viewer -> viewer.getPlayer().getUniqueId().equals(player.getUniqueId()));
        return new ItemOptions(visibility, null, null);
    }

    public boolean canSee(@NonNull MenuViewer viewer) {
        Predicate<MenuViewer> policy = this.getVisibilityPolicy();
        return policy == null || policy.test(viewer);
    }

    public boolean canBeDestroyed(@NonNull MenuViewer viewer) {
        Predicate<MenuViewer> policy = this.getWeakPolicy();
        return policy != null && policy.test(viewer);
    }

    public void modifyDisplay(@NonNull MenuViewer viewer, @NonNull ItemStack item) {
        BiConsumer<MenuViewer, ItemStack> displayModifier = this.getDisplayModifier();
        if (displayModifier != null) {
            displayModifier.accept(viewer, item);
        }
    }

    @Nullable
    public Predicate<MenuViewer> getVisibilityPolicy() {
        return visibilityPolicy;
    }

    @NonNull
    public ItemOptions setVisibilityPolicy(@Nullable Predicate<MenuViewer> visibilityPolicy) {
        this.visibilityPolicy = visibilityPolicy;
        return this;
    }

    @NonNull
    public ItemOptions addVisibilityPolicy(@NonNull Predicate<MenuViewer> visibilityPolicy) {
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

    @NonNull
    public ItemOptions setWeakPolicy(@Nullable Predicate<MenuViewer> weakPolicy) {
        this.weakPolicy = weakPolicy;
        return this;
    }

    @Nullable
    public BiConsumer<MenuViewer, ItemStack> getDisplayModifier() {
        return displayModifier;
    }

    @NonNull
    public ItemOptions setDisplayModifier(@Nullable BiConsumer<MenuViewer, ItemStack> displayModifier) {
        this.displayModifier = displayModifier;
        return this;
    }

    @NonNull
    public ItemOptions addDisplayModifier(@NonNull BiConsumer<MenuViewer, ItemStack> displayModifier) {
        if (this.displayModifier == null) {
            this.setDisplayModifier(displayModifier);
        }
        else {
            this.displayModifier = this.displayModifier.andThen(displayModifier);
        }
        return this;
    }
}
