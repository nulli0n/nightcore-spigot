package su.nightexpress.nightcore.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.dialog.DialogHandler;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.legacy.LegacyLangString;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.text.WrappedMessage;

public abstract class EditorMenu<P extends NightCorePlugin, T> extends AbstractMenu<P> implements Linked<T> {

    protected final ViewLink<T> link;

    public EditorMenu(@NotNull P plugin, @NotNull String title, int size) {
        super(plugin, title, size);
        this.link = new ViewLink<>();
    }

    @NotNull
    @Override
    public ViewLink<T> getLink() {
        return link;
    }

    @NotNull
    protected MenuItem addNextPage(int... slots) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19");
        return this.addItem(item, CoreLang.EDITOR_ITEM_NEXT_PAGE, slots).setHandler(ItemHandler.forNextPage(this));
    }

    @NotNull
    protected MenuItem addPreviousPage(int... slots) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=");
        return this.addItem(item, CoreLang.EDITOR_ITEM_PREVIOUS_PAGE, slots).setHandler(ItemHandler.forPreviousPage(this));
    }

    @NotNull
    protected MenuItem addReturn(int... slots) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=");
        return this.addItem(item, CoreLang.EDITOR_ITEM_RETURN, slots);
    }

    @NotNull
    protected MenuItem addCreation(@NotNull LangItem locale, int... slots) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19");
        return this.addItem(item, locale, slots);
    }

    @NotNull
    protected MenuItem addExit(int... slots) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemUtil.setSkullTexture(item, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==");
        return this.addItem(item, CoreLang.EDITOR_ITEM_CLOSE, slots).setHandler(ItemHandler.forClose(this));
    }

    @NotNull
    public MenuItem addItem(@NotNull Material material, @NotNull LangItem locale, int... slots) {
        return this.addItem(new ItemStack(material), locale, slots);
    }

    @NotNull
    public MenuItem addItem(@NotNull ItemStack item, @NotNull LangItem locale, int... slots) {
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(locale.getLocalizedName());
            meta.setLore(locale.getLocalizedLore());
            meta.addItemFlags(ItemFlag.values());
        });

        MenuItem menuItem = new MenuItem(item).setPriority(100).setSlots(slots);
        this.addItem(menuItem);
        return menuItem;
    }

    @Deprecated
    public void handleInput(@NotNull MenuViewer viewer, @NotNull LegacyLangString prompt, @NotNull DialogHandler handler) {
        this.handleInput(viewer.getPlayer(), prompt, handler);
    }

    @Deprecated
    public void handleInput(@NotNull Player player, @NotNull LegacyLangString prompt, @NotNull DialogHandler handler) {
        this.handleInput(player, prompt.getString(), handler);
    }

    public void handleInput(@NotNull MenuViewer viewer, @NotNull LangString prompt, @NotNull DialogHandler handler) {
        this.handleInput(viewer.getPlayer(), prompt, handler);
    }

    public void handleInput(@NotNull Player player, @NotNull LangString prompt, @NotNull DialogHandler handler) {
        this.handleInput(player, prompt.getString(), handler);
    }

    @NotNull
    public Dialog handleInput(@NotNull Player player, @NotNull WrappedMessage prompt, @NotNull DialogHandler handler) {
        this.runNextTick(player::closeInventory);

        Dialog dialog = Dialog.create(player, handler);
        dialog.prompt(prompt);
        return dialog;
    }

    @NotNull
    public Dialog handleInput(@NotNull Player player, @NotNull String prompt, @NotNull DialogHandler handler) {
        this.runNextTick(player::closeInventory);

        Dialog dialog = Dialog.create(player, handler);
        dialog.prompt(prompt);
        return dialog;
    }

    @NotNull
    public T getObject(@NotNull MenuViewer viewer) {
        return this.getLink().get(viewer);
    }
}
