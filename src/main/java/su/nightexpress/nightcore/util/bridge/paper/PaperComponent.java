package su.nightexpress.nightcore.util.bridge.paper;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.ItemNbt;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bridge.wrapper.ClickEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.HoverEventType;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.ComponentBuildable;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.awt.*;
import java.util.List;

public class PaperComponent implements NightComponent {

    private Component parent;

    public PaperComponent(@NotNull Component parent) {
        this.parent = parent;
    }

    @NotNull
    public static PaperComponent text(@NotNull String text) {
        return new PaperComponent(Component.text(text));
    }

    @NotNull
    public static PaperComponent translate(@NotNull String key) {
        return new PaperComponent(Component.translatable(key));
    }

    @NotNull
    public static PaperComponent builder(@NotNull List<ComponentBuildable> childrens) {
        // Force remove all decortaions, otherwise it'll be decorated by default (italic on items, etc).
        var builder = Component.text().decorations(Lists.newSet(TextDecoration.values()), false);

        childrens.forEach(child -> {
            PaperComponent childComponent = (PaperComponent) child.toComponent();
            builder.append(childComponent.parent);
        });

        return new PaperComponent(builder.build());
    }

    @NotNull
    public Component getParent() {
        return this.parent;
    }

    @Override
    @NotNull
    public NightComponent duplicate() {
        return new PaperComponent(Component.empty().append(this.parent).asComponent());
    }

    @Override
    @NotNull
    public String toJson() {
        return JSONComponentSerializer.json().serialize(this.parent);
    }

    @Override
    @NotNull
    public String toLegacy() {
        return LegacyComponentSerializer.legacy('§').serialize(this.parent);
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        sender.sendMessage(this.parent);
    }

    @Override
    public void sendActionBar(@NotNull Player player) {
        player.sendActionBar(this.parent);
    }

    @Override
    public boolean isEmpty() {
        return this.parent instanceof TextComponent textComponent && textComponent.content().isEmpty() && textComponent.children().isEmpty();
    }

    @Override
    public boolean isText() {
        return this.parent instanceof TextComponent;
    }

    @Override
    @NotNull
    public String getText() {
        return this.parent instanceof TextComponent textComponent ? textComponent.content() : "";
    }

    @Override
    @NotNull
    public List<PaperComponent> getChildrens() {
        return this.parent.children().stream().map(PaperComponent::new).toList();
    }

    @Override
    public void setChildrens(@NotNull List<NightComponent> childrens) {
        this.parent = this.parent.children(childrens.stream().map(child -> ((PaperComponent)child).parent).toList());
    }

    @Override
    public void addChildren(@NotNull NightComponent other) {
        if (other instanceof PaperComponent paperComponent) {
            this.parent = this.parent.append(paperComponent.parent);
        }
    }

    @Override
    public void setText(@NotNull String text) {
        if (this.parent instanceof TextComponent textComponent) {
            this.parent = textComponent.content(text);
        }
    }

    @Override
    public void setColor(@NotNull Color color) {
        this.parent = this.parent.color(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
    }

    @Override
    public void setClickEvent(@NotNull ClickEventType type, @NotNull String value) {
        ClickEvent.Action action = switch (type) {
            case COPY_TO_CLIPBOARD -> ClickEvent.Action.COPY_TO_CLIPBOARD;
            case SUGGEST_COMMAND -> ClickEvent.Action.SUGGEST_COMMAND;
            case RUN_COMMAND -> ClickEvent.Action.RUN_COMMAND;
            case CHANGE_PAGE -> ClickEvent.Action.CHANGE_PAGE;
            case OPEN_FILE -> ClickEvent.Action.OPEN_FILE;
            case OPEN_URL -> ClickEvent.Action.OPEN_URL;
        };

        this.parent = this.parent.clickEvent(ClickEvent.clickEvent(action, value));
    }

    @Override
    public void setHoverEvent(@NotNull HoverEventType type, @NotNull String value) {
        HoverEvent<?> event = null;

        switch (type) {
            case SHOW_TEXT -> {
                PaperComponent component = (PaperComponent) NightMessage.parse(value);
                event = HoverEvent.showText(component.parent);
            }
            case SHOW_ITEM -> {
                ItemStack itemStack = ItemNbt.getHoverEventItem(value);
                event = itemStack.asHoverEvent();
            }
        }
        if (event == null) return;

        this.parent = this.parent.hoverEvent(event);
    }

    @Override
    public void setFont(@NotNull String font) {
        this.parent = this.parent.font(Key.key(font));
    }

    @Override
    public void setBold(boolean bold) {
        this.parent = this.parent.decoration(TextDecoration.BOLD, bold);
    }

    @Override
    public void setItalic(boolean italic) {
        this.parent = this.parent.decoration(TextDecoration.ITALIC, italic);
    }

    @Override
    public void setObfuscated(boolean obfuscated) {
        this.parent = this.parent.decoration(TextDecoration.OBFUSCATED, obfuscated);
    }

    @Override
    public void setUnderlined(boolean underlined) {
        this.parent = this.parent.decoration(TextDecoration.UNDERLINED, underlined);
    }

    @Override
    public void setStrikethrough(boolean strikethrough) {
        this.parent = this.parent.decoration(TextDecoration.STRIKETHROUGH, strikethrough);
    }

    @Override
    public String toString() {
        return "PaperComponent{" +
            "parent=" + parent +
            '}';
    }
}
