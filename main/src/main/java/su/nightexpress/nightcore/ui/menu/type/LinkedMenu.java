package su.nightexpress.nightcore.ui.menu.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

@SuppressWarnings("UnstableApiUsage")
public abstract class LinkedMenu<P extends NightPlugin, T> extends AbstractMenu<P> implements Linked<T> {

    protected final LinkCache<T> cache;

    public LinkedMenu(@NotNull P plugin, @NotNull MenuType menuType, @NotNull String title) {
        super(plugin, menuType, title);

        this.cache = new LinkCache<>();
    }

    @Override
    public void clear() {
        super.clear();
        this.cache.clear();
    }

    @NotNull
    @Override
    public LinkCache<T> getCache() {
        return this.cache;
    }

    @Override
    public boolean isCached(@NotNull Player player) {
        return this.cache.contains(player);
    }

    @Override
    public T getLink(@NotNull MenuViewer viewer) {
        return this.getLink(viewer.getPlayer());
    }

    @Override
    public T getLink(@NotNull Player player) {
        return this.cache.get(player);
    }

    @Override
    public ItemClick manageLink(@NotNull LinkHandler<T> handler) {
        return (viewer, event) -> {
            handler.handle(viewer, event, this.getLink(viewer));
        };
    }

    @Override
    public void flush(@NotNull Player player, @NotNull Consumer<MenuViewer> consumer) {
        if (!this.isCached(player)) {
            this.plugin.warn("Null link reference in menu: " + player.getName() + " / " + this);
            this.close(player);
            return;
        }
        super.flush(player, consumer);
    }

    @Override
    public boolean open(@NotNull Player player, @NotNull T obj) {
        return this.open(player, obj, viewer -> {});
    }

    @Override
    public boolean open(@NotNull Player player, @NotNull T obj, @NotNull Consumer<MenuViewer> onViewSet) {
        return this.open(player, viewer -> {
            this.cache.set(player, obj);
            onViewSet.accept(viewer);
        });
    }

    @Override
    @Deprecated
    public void handleInput(@NotNull Dialog.Builder builder) {
        this.cache.addAnchor(builder.getPlayer());
        super.handleInput(builder);
    }

    @Override
    protected void onClose(@NotNull MenuViewer viewer) {
        if (!this.cache.hasAnchor(viewer.getPlayer())) {
            this.cache.clear(viewer);
        }
        else this.cache.removeAnchor(viewer.getPlayer());

        super.onClose(viewer);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull Material material, @NotNull LangItem locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(material, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull Material material, @NotNull LangItem locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(material).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull ItemStack itemStack, @NotNull LangItem locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(itemStack, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull ItemStack itemStack, @NotNull LangItem locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(itemStack).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull NightItem item, @NotNull LangItem locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(item, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull NightItem item, @NotNull LangItem locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(item.localized(locale), slot, handler, options);
    }


    @Override
    @Deprecated
    public void addItem(@NotNull Material material, @NotNull LangUIButton locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(material, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull Material material, @NotNull LangUIButton locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(material).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull ItemStack itemStack, @NotNull LangUIButton locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(itemStack, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull ItemStack itemStack, @NotNull LangUIButton locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(itemStack).localized(locale), slot, handler, options);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull NightItem item, @NotNull LangUIButton locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(item, locale, slot, handler, null);
    }

    @Override
    @Deprecated
    public void addItem(@NotNull NightItem item, @NotNull LangUIButton locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(item.localized(locale), slot, handler, options);
    }



    @Override
    public void addItem(@NotNull Material material, @NotNull IconLocale locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(material, locale, slot, handler, null);
    }

    @Override
    public void addItem(@NotNull Material material, @NotNull IconLocale locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(material).localized(locale), slot, handler, options);
    }

    @Override
    public void addItem(@NotNull ItemStack itemStack, @NotNull IconLocale locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(itemStack, locale, slot, handler, null);
    }

    @Override
    public void addItem(@NotNull ItemStack itemStack, @NotNull IconLocale locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(new NightItem(itemStack).localized(locale), slot, handler, options);
    }

    @Override
    public void addItem(@NotNull NightItem item, @NotNull IconLocale locale, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(item, locale, slot, handler, null);
    }

    @Override
    public void addItem(@NotNull NightItem item, @NotNull IconLocale locale, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
        this.addItem(item.localized(locale), slot, handler, options);
    }



    @Override
    public void addItem(@NotNull NightItem item, int slot, @NotNull LinkHandler<T> handler) {
        this.addItem(item, slot, handler, null);
    }

    @Override
    public void addItem(@NotNull NightItem item, int slot, @NotNull LinkHandler<T> handler, @Nullable ItemOptions options) {
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
