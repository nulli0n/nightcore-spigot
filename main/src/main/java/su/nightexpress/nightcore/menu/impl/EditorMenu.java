package su.nightexpress.nightcore.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogHandler;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.text.TextRoot;

import java.util.function.Consumer;

@Deprecated
public abstract class EditorMenu<P extends NightCorePlugin, T> extends AbstractMenu<P> implements Linked<T> {

    protected final ViewLink<T> link;

    @Deprecated
    public EditorMenu(@NonNull P plugin, @NonNull String title, int size) {
        super(plugin, title, size);
        this.link = new ViewLink<>();
    }

    public EditorMenu(@NonNull P plugin, @NonNull String title, @NonNull MenuSize size) {
        super(plugin, title, size);
        this.link = new ViewLink<>();
    }

    @NonNull
    @Override
    public ViewLink<T> getLink() {
        return link;
    }

    @NonNull
    protected MenuItem addNextPage(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("f32ca66056b72863e98f7f32bd7d94c7a0d796af691c9ac3a9136331352288f9");
        return this.addItem(item, CoreLang.EDITOR_ITEM_NEXT_PAGE, slots).setHandler(ItemHandler.forNextPage(this));
    }

    @NonNull
    protected MenuItem addPreviousPage(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("86971dd881dbaf4fd6bcaa93614493c612f869641ed59d1c9363a3666a5fa6");
        return this.addItem(item, CoreLang.EDITOR_ITEM_PREVIOUS_PAGE, slots).setHandler(ItemHandler.forPreviousPage(
            this));
    }

    @NonNull
    @Deprecated
    protected MenuItem addReturn(int... slots) {
        ItemStack item = ItemUtil.getSkinHead("be9ae7a4be65fcbaee65181389a2f7d47e2e326db59ea3eb789a92c85ea46");
        //ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        //ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=");
        return this.addItem(item, CoreLang.EDITOR_ITEM_RETURN, slots);
    }

    @NonNull
    @Deprecated
    protected MenuItem addCreation(@NonNull LangItem locale, int... slots) {
        ItemStack item = ItemUtil.getSkinHead("5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1");
        //ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        //ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19");
        return this.addItem(item, locale, slots);
    }

    @NonNull
    protected MenuItem addReturn(int slot, @NonNull EditorHandler<T> handler) {
        ItemStack item = ItemUtil.getSkinHead("be9ae7a4be65fcbaee65181389a2f7d47e2e326db59ea3eb789a92c85ea46");
        return this.addItem(item, CoreLang.EDITOR_ITEM_RETURN, slot, handler);
    }

    @NonNull
    protected MenuItem addCreation(@NonNull LangItem locale, int slot, @NonNull EditorHandler<T> handler) {
        ItemStack item = ItemUtil.getSkinHead("5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1");
        return this.addItem(item, locale, slot, handler);
    }

    @NonNull
    @Deprecated
    protected MenuItem addExit(int[] slots) {
        //ItemStack item = ItemUtil.getSkinHead("27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065");
        //return this.addItem(item, CoreLang.EDITOR_ITEM_CLOSE, slots).setHandler(ItemHandler.forClose(this));
        return this.addExit(slots[0]);
    }

    @NonNull
    protected MenuItem addExit(int slot) {
        ItemStack item = ItemUtil.getSkinHead("27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065");
        return this.addItem(item, CoreLang.EDITOR_ITEM_CLOSE, slot, (viewer, event, obj) -> this.runNextTick(
            () -> viewer.getPlayer().closeInventory()));
    }

    @NonNull
    @Deprecated
    public MenuItem addItem(@NonNull Material material, @NonNull LangItem locale, int... slots) {
        return this.addItem(new ItemStack(material), locale, slots);
    }

    @NonNull
    @Deprecated
    public MenuItem addItem(@NonNull ItemStack item, @NonNull LangItem locale, int... slots) {
        ItemReplacer.create(item).trimmed().hideFlags().readLocale(locale).writeMeta();
        MenuItem menuItem = new MenuItem(item).setPriority(100).setSlots(slots);
        this.addItem(menuItem);
        return menuItem;
    }

    @NonNull
    public MenuItem addItem(@NonNull Material material, @NonNull LangItem locale, int slot,
                            @NonNull EditorHandler<T> handler) {
        return this.addItem(new ItemStack(material), locale, slot, handler);
    }

    @NonNull
    public MenuItem addItem(@NonNull ItemStack item, @NonNull LangItem locale, int slot,
                            @NonNull EditorHandler<T> handler) {
        ItemReplacer.create(item).trimmed().hideFlags().readLocale(locale).writeMeta();
        MenuItem menuItem = new MenuItem(item).setPriority(100).setSlots(slot)
            .setHandler((viewer, event) -> handler.handle(viewer, event, this.getLink(viewer)));
        this.addItem(menuItem);
        return menuItem;
    }

    @NonNull
    public Dialog handleInput(@NonNull MenuViewer viewer, @NonNull LangString prompt, @NonNull DialogHandler handler) {
        return this.handleInput(viewer.getPlayer(), prompt, handler);
    }

    @NonNull
    public Dialog handleInput(@NonNull Player player, @NonNull LangString prompt, @NonNull DialogHandler handler) {
        return this.handleInput(player, prompt.getMessage(), handler);
    }

    @NonNull
    public Dialog handleInput(@NonNull Player player, @NonNull TextRoot prompt, @NonNull DialogHandler handler) {
        this.runNextTick(player::closeInventory);

        Dialog dialog = Dialog.create(player, handler);
        dialog.prompt(prompt);
        return dialog;
    }

    @NonNull
    @Deprecated
    public Dialog handleInput(@NonNull Player player, @NonNull String prompt, @NonNull DialogHandler handler) {
        this.runNextTick(player::closeInventory);

        Dialog dialog = Dialog.create(player, handler);
        dialog.prompt(prompt);
        return dialog;
    }

    @Deprecated
    public void editObject(@NonNull MenuViewer viewer, @NonNull Consumer<T> consumer) {
        this.editObject(viewer.getPlayer(), consumer);
    }

    @Deprecated
    public void editObject(@NonNull Player player, @NonNull Consumer<T> consumer) {
        consumer.accept(this.getLink(player));
    }

    @NonNull
    @Deprecated
    public T getObject(@NonNull MenuViewer viewer) {
        return this.getLink().get(viewer);
    }

    @NonNull
    @Deprecated
    public T getObject(@NonNull Player player) {
        return this.getLink().get(player);
    }

    public interface EditorHandler<T> {

        void handle(@NonNull MenuViewer viewer, @NonNull InventoryClickEvent event, @NonNull T obj);
    }
}
