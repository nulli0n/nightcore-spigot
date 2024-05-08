package su.nightexpress.nightcore.util.message;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.ItemNbt;

import java.util.List;

@Deprecated
public class NexComponent {

    private final String text;

    public HoverEvent hoverEvent;
    public ClickEvent clickEvent;

    private String font;
    private String insertion;

    public NexComponent(@NotNull String text) {
        this.text = Colorizer.apply(text);
    }

    @NotNull
    public String getText() {
        return this.text;
    }

    @Nullable
    public String getFont() {
        return font;
    }

    public void setFont(@Nullable String font) {
        this.font = font;
    }

    @Nullable
    public String getInsertion() {
        return insertion;
    }

    public void setInsertion(@Nullable String insertion) {
        this.insertion = insertion;
    }

    @Nullable
    public HoverEvent getHoverEvent() {
        return this.hoverEvent;
    }

    @Nullable
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    @NotNull
    public NexComponent addClickEvent(@NotNull ClickEvent.Action action, @NotNull String value) {
        return switch (action) {
            case OPEN_URL -> this.openURL(value);
            case OPEN_FILE, CHANGE_PAGE -> this;
            case RUN_COMMAND -> this.runCommand(value);
            case SUGGEST_COMMAND -> this.suggestCommand(value);
            case COPY_TO_CLIPBOARD -> this.copyToClipboard(value);
        };
    }

    @NotNull
    public NexComponent addHoverEvent(@NotNull HoverEvent.Action action, @NotNull String value) {
        return switch (action) {
            case SHOW_ITEM -> {
                ItemStack item = ItemNbt.decompress(value);
                yield this.showItem(item == null ? new ItemStack(Material.AIR) : item);
            }
            case SHOW_TEXT -> this.showText(value);
            default -> this;
        };
    }

    @NotNull
    public NexComponent showText(@NotNull String text) {
        return this.showText(text.split(NexParser.TAG_NEWLINE));
    }

    @NotNull
    public NexComponent showText(@NotNull List<String> text) {
        return this.showText(text.toArray(new String[0]));
    }

    @NotNull
    public NexComponent showText(@NotNull String... text) {
        BaseComponent[] base = NexMessage.fromLegacyText(Colorizer.apply(String.join("\n", text)));
        this.hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(base));
        return this;
    }

    @NotNull
    public NexComponent showItem(@NotNull ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        Item item = new Item(is.getType().getKey().getKey(), is.getAmount(), ItemTag.ofNbt(meta == null ? null : meta.getAsString()));
        this.hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, item);
        return this;
    }

    @NotNull
    public NexComponent runCommand(@NotNull String command) {
        this.clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        return this;
    }

    @NotNull
    public NexComponent suggestCommand(@NotNull String command) {
        this.clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
        return this;
    }

    @NotNull
    public NexComponent openURL(@NotNull String url) {
        this.clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        return this;
    }

    @NotNull
    public NexComponent copyToClipboard(@NotNull String text) {
        this.clickEvent = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text);
        return this;
    }

    @NotNull
    public TextComponent build() {
        TextComponent component = new TextComponent(NexMessage.fromLegacyText(this.getText()));
        if (this.hoverEvent != null) {
            component.setHoverEvent(this.getHoverEvent());
        }
        if (this.clickEvent != null) {
            component.setClickEvent(this.getClickEvent());
        }
        component.setFont(this.getFont());
        component.setInsertion(this.getInsertion());
        return component;
    }

    @Override
    public String toString() {
        return "NexComponent{" +
            "text='" + text + '\'' +
            ", hoverEvent=" + hoverEvent +
            ", clickEvent=" + clickEvent +
            '}';
    }
}
