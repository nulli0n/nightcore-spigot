package su.nightexpress.nightcore.ui.menu.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.LinkCache;
import su.nightexpress.nightcore.ui.menu.data.LinkHandler;
import su.nightexpress.nightcore.ui.menu.data.Linked;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.function.Consumer;

@Deprecated
public abstract class LinkedMenu<P extends NightPlugin, T> extends AbstractMenu<P> implements Linked<T> {

    protected final LinkCache<T> cache;

    public LinkedMenu(@NonNull P plugin, @NonNull MenuType menuType, @NonNull String title) {
        super(plugin, menuType, title);

        this.cache = new LinkCache<>();
    }

    @Override
    public void clear() {
        super.clear();
        this.cache.clear();
    }

    @NonNull
    @Override
    public LinkCache<T> getCache() {
        return this.cache;
    }

    @Override
    public boolean isCached(@NonNull Player player) {
        return this.cache.contains(player);
    }

    @Override
    public T getLink(@NonNull MenuViewer viewer) {
        return this.getLink(viewer.getPlayer());
    }

    @Override
    public T getLink(@NonNull Player player) {
        return this.cache.get(player);
    }

    @Override
    public ItemClick manageLink(@NonNull LinkHandler<T> handler) {
        return (viewer, event) -> {
            handler.handle(viewer, event, this.getLink(viewer));
        };
    }

    @Override
    public void flush(@NonNull Player player, @NonNull Consumer<MenuViewer> consumer) {
        if (!this.isCached(player)) {
            this.plugin.warn("Null link reference in menu: " + player.getName() + " / " + this);
            this.close(player);
            return;
        }
        super.flush(player, consumer);
    }

    @Override
    public boolean open(@NonNull Player player, @NonNull T obj) {
        return this.open(player, obj, viewer -> {
        });
    }

    @Override
    public boolean open(@NonNull Player player, @NonNull T obj, @NonNull Consumer<MenuViewer> onViewSet) {
        return this.open(player, viewer -> {
            this.cache.set(player, obj);
            onViewSet.accept(viewer);
        });
    }

    @Override
    @Deprecated
    public void handleInput(Dialog.@NonNull Builder builder) {
        this.cache.addAnchor(builder.getPlayer());
        super.handleInput(builder);
    }

    @Override
    protected void onClose(@NonNull MenuViewer viewer) {
        if (!this.cache.hasAnchor(viewer.getPlayer())) {
            this.cache.clear(viewer);
        }
        else this.cache.removeAnchor(viewer.getPlayer());

        super.onClose(viewer);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull Material material, @NonNull LangItem locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(material, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull Material material, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler,
                        @Nullable ItemOptions options) {
        this.addItem(new NightItem(material).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull ItemStack itemStack, @NonNull LangItem locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(itemStack, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull ItemStack itemStack, @NonNull LangItem locale, int slot,
                        @NonNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(itemStack).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull NightItem item, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler) {
        this.addItem(item, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull NightItem item, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler,
                        @Nullable ItemOptions options) {
        this.addItem(item.localized(locale), slot, handler, options);
    }


    @Override
    @Deprecated
    public void addItem(@NonNull Material material, @NonNull LangUIButton locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(material, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull Material material, @NonNull LangUIButton locale, int slot,
                        @NonNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(material).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull ItemStack itemStack, @NonNull LangUIButton locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(itemStack, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull ItemStack itemStack, @NonNull LangUIButton locale, int slot,
                        @NonNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(itemStack).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull NightItem item, @NonNull LangUIButton locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(item, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NonNull NightItem item, @NonNull LangUIButton locale, int slot,
                        @NonNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(item.localized(locale), slot, handler, options);
    }


    @Override
    public void addItem(@NonNull Material material, @NonNull IconLocale locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(material, locale, slot, handler, null);
    }

    @Override
    public void addItem(@NonNull Material material, @NonNull IconLocale locale, int slot,
                        @NonNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(material).localized(locale), slot, handler, options);
    }

    @Override
    public void addItem(@NonNull ItemStack itemStack, @NonNull IconLocale locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(itemStack, locale, slot, handler, null);
    }

    @Override
    public void addItem(@NonNull ItemStack itemStack, @NonNull IconLocale locale, int slot,
                        @NonNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(itemStack).localized(locale), slot, handler, options);
    }

    @Override
    public void addItem(@NonNull NightItem item, @NonNull IconLocale locale, int slot,
                        @NonNull LinkHandler<T> handler) {
        this.addItem(item, locale, slot, handler, null);
    }

    @Override
    public void addItem(@NonNull NightItem item, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler,
                        @Nullable ItemOptions options) {
        this.addItem(item.localized(locale), slot, handler, options);
    }


    @Override
    public void addItem(@NonNull NightItem item, int slot, @NonNull LinkHandler<T> handler) {
        this.addItem(item, slot, handler, null);
    }

    @Override
    public void addItem(@NonNull NightItem item, int slot, @NonNull LinkHandler<T> handler,
                        @Nullable ItemOptions options) {
        MenuItem menuItem = item
            .hideAllComponents()
            .toMenuItem()
            .setPriority(MenuItem.HIGH_PRIORITY)
            .setSlots(slot)
            .setHandler(ItemHandler.forLink(this, handler, options))
            .build();

        this.addItem(menuItem);
    }
}
