package su.nightexpress.nightcore.ui.menu.data;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.function.Consumer;

@Deprecated
public interface Linked<T> extends Menu {

    boolean open(@NonNull Player player, @NonNull T obj);

    boolean open(@NonNull Player player, @NonNull T obj, @NonNull Consumer<MenuViewer> onViewSet);

    @NonNull
    LinkCache<T> getCache();

    boolean isCached(@NonNull Player player);

    T getLink(@NonNull MenuViewer viewer);

    T getLink(@NonNull Player player);

    ItemClick manageLink(@NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull Material material, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull Material material, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);

    @Deprecated
    void addItem(@NonNull ItemStack itemStack, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull ItemStack itemStack, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);

    @Deprecated
    void addItem(@NonNull NightItem item, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull NightItem item, @NonNull LangItem locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);


    @Deprecated
    void addItem(@NonNull Material material, @NonNull LangUIButton locale, int slot, @NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull Material material, @NonNull LangUIButton locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);

    @Deprecated
    void addItem(@NonNull ItemStack itemStack, @NonNull LangUIButton locale, int slot, @NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull ItemStack itemStack, @NonNull LangUIButton locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);

    @Deprecated
    void addItem(@NonNull NightItem item, @NonNull LangUIButton locale, int slot, @NonNull LinkHandler<T> handler);

    @Deprecated
    void addItem(@NonNull NightItem item, @NonNull LangUIButton locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);


    void addItem(@NonNull Material material, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler);

    void addItem(@NonNull Material material, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);

    void addItem(@NonNull ItemStack itemStack, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler);

    void addItem(@NonNull ItemStack itemStack, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);

    void addItem(@NonNull NightItem item, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler);

    void addItem(@NonNull NightItem item, @NonNull IconLocale locale, int slot, @NonNull LinkHandler<T> handler,
                 @Nullable ItemOptions options);


    void addItem(@NonNull NightItem item, int slot, @NonNull LinkHandler<T> handler);

    void addItem(@NonNull NightItem item, int slot, @NonNull LinkHandler<T> handler, @Nullable ItemOptions options);
}
