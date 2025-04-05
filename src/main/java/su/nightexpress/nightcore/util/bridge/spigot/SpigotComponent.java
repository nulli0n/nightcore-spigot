package su.nightexpress.nightcore.util.bridge.spigot;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.HoverEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.ComponentBuildable;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SpigotComponent implements NightComponent {

    private final BaseComponent parent;

    public SpigotComponent(BaseComponent parent) {
        this.parent = parent;
    }

    @NotNull
    public static SpigotComponent text(@NotNull String text) {
        return new SpigotComponent(new TextComponent(text));
    }

    @NotNull
    public static SpigotComponent translate(@NotNull String key) {
        return new SpigotComponent(new TranslatableComponent(key));
    }

    public static SpigotComponent builder(@NotNull List<ComponentBuildable> childrens) {
        ComponentBuilder builder = new ComponentBuilder();

        childrens.forEach(child -> {
            SpigotComponent childComponent = (SpigotComponent) child.toComponent();
            builder.append(childComponent.parent);
        });

        return new SpigotComponent(build(builder));
    }

    private static BaseComponent build(@NotNull ComponentBuilder builder) {
        if (Version.isAtLeast(Version.MC_1_20_6)) {
            return builder.build();
        }

        TextComponent base = new TextComponent();
        if (!builder.getParts().isEmpty()) {
            List<BaseComponent> cloned = new ArrayList<>(builder.getParts());
            cloned.replaceAll(BaseComponent::duplicate);
            base.setExtra(cloned);
        }
        return base;
    }

    @NotNull
    public BaseComponent getParent() {
        return this.parent;
    }

    @Override
    @NotNull
    public NightComponent duplicate() {
        return new SpigotComponent(this.parent.duplicate());
    }

    @Override
    @NotNull
    public String toJson() {
        return ComponentSerializer.toString(this.parent);
    }

    @Override
    @NotNull
    public String toLegacy() {
        return TextComponent.toLegacyText(this.parent);
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        sender.spigot().sendMessage(this.parent);
    }

    @Override
    public void sendActionBar(@NotNull Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, this.parent);
    }

    @Override
    public boolean isEmpty() {
        return this.parent instanceof TextComponent textComponent && textComponent.getText().isBlank() && textComponent.getExtra() == null;
    }

    @Override
    public boolean isText() {
        return this.parent instanceof TextComponent;
    }

    @Override
    @NotNull
    public String getText() {
        return this.parent instanceof TextComponent textComponent ? textComponent.getText() : "";
    }

    @Override
    @NotNull
    public List<SpigotComponent> getChildrens() {
        return this.parent.getExtra().stream().map(SpigotComponent::new).toList();
    }

    @Override
    public void setChildrens(@NotNull List<NightComponent> childrens) {
        this.parent.setExtra(childrens.stream().map(child -> ((SpigotComponent)child).parent).toList());
    }

    @Override
    public void addChildren(@NotNull NightComponent other) {
        if (other instanceof SpigotComponent spigotComponent) {
            this.parent.addExtra(spigotComponent.getParent());
        }
    }

    @Override
    public void setText(@NotNull String text) {
        if (this.parent instanceof TextComponent textComponent) {
            textComponent.setText(text);
        }
    }

    @Override
    public void setColor(@NotNull Color color) {
        this.parent.setColor(ChatColor.of(color));
    }

    @Override
    public void setClickEvent(@NotNull ClickEventType type, @NotNull String value) {
        ClickEvent.Action action = switch (type) {
            case OPEN_URL -> ClickEvent.Action.OPEN_URL;
            case OPEN_FILE -> ClickEvent.Action.OPEN_FILE;
            case CHANGE_PAGE -> ClickEvent.Action.CHANGE_PAGE;
            case RUN_COMMAND -> ClickEvent.Action.RUN_COMMAND;
            case SUGGEST_COMMAND -> ClickEvent.Action.SUGGEST_COMMAND;
            case COPY_TO_CLIPBOARD -> ClickEvent.Action.COPY_TO_CLIPBOARD;
        };

        this.parent.setClickEvent(new ClickEvent(action, value));
    }

    @Override
    public void setHoverEvent(@NotNull HoverEventType type, @NotNull String value) {
        switch (type) {
            case SHOW_TEXT -> {
                SpigotComponent component = (SpigotComponent) NightMessage.parse(value);

                Text text;
                if (Version.isAtLeast(Version.MC_1_20_6)) {
                    text = new Text(component.parent);
                }
                else {
                    text = new Text(new ComponentBuilder().append(component.parent).create());
                }

                this.parent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
            }
            case SHOW_ITEM -> {
                ItemStack itemStack = ItemNbt.getHoverEventItem(value);

                String key = BukkitThing.toString(itemStack.getType());
                ItemMeta meta = itemStack.getItemMeta();
                String nbt = meta == null ? "{}" : meta.getAsString();

                Item item = new Item(key, itemStack.getAmount(), ItemTag.ofNbt(nbt));
                this.parent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, item));
            }
        }
    }

    @Override
    public void setFont(@NotNull String font) {
        this.parent.setFont(font);
    }

    @Override
    public void setBold(boolean bold) {
        this.parent.setBold(bold);
    }

    @Override
    public void setItalic(boolean italic) {
        this.parent.setItalic(italic);
    }

    @Override
    public void setObfuscated(boolean obfuscated) {
        this.parent.setObfuscated(obfuscated);
    }

    @Override
    public void setUnderlined(boolean underlined) {
        this.parent.setUnderlined(underlined);
    }

    @Override
    public void setStrikethrough(boolean strikethrough) {
        this.parent.setStrikethrough(strikethrough);
    }
}
