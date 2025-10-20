package su.nightexpress.nightcore.bridge.spigot.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.bridge.spigot.click.SpigotClickEventAdapter;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.bridge.text.impl.NightKeybindComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightObjectComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTextComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTranslatableComponent;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;
import java.util.List;

public class SpigotTextComponentAdapter implements TextComponentAdapter<BaseComponent> {

    private final SpigotBridge bridge;

    public SpigotTextComponentAdapter(@NotNull SpigotBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void send(@NotNull CommandSender sender, @NotNull NightComponent component) {
        sender.spigot().sendMessage(this.adaptComponent(component));
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull NightComponent component) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, this.adaptComponent(component));
    }

    @Override
    @NotNull
    public String toJson(@NotNull NightComponent component) {
        return ComponentSerializer.toString(this.adaptComponent(component));
    }

    @Override
    @NotNull
    public String toLegacy(@NotNull NightComponent component) {
        return TextComponent.toLegacyText(this.adaptComponent(component));
    }

    @NotNull
    public ComponentStyle adaptStyle(@NotNull NightStyle nightStyle) {
        NightKey font = nightStyle.font();
        Color color = nightStyle.color();
        Color shadowColor = nightStyle.shadowColor();

        ComponentStyleBuilder builder = ComponentStyle.builder()
            .font(font == null ? null : font.toString())
            .color(color == null ? null : ChatColor.of(color))
            .bold(nightStyle.decoration(NightTextDecoration.BOLD).bool())
            .italic(nightStyle.decoration(NightTextDecoration.ITALIC).bool())
            .obfuscated(nightStyle.decoration(NightTextDecoration.OBFUSCATED).bool())
            .strikethrough(nightStyle.decoration(NightTextDecoration.STRIKETHROUGH).bool())
            .underlined(nightStyle.decoration(NightTextDecoration.UNDERLINED).bool());

        if (Version.isAtLeast(Version.MC_1_21_7)) {
            builder.shadowColor(shadowColor);
        }

        return builder.build();
    }

    @NotNull
    public ClickEvent adaptClickEvent(@NotNull NightClickEvent event) {
        return SpigotClickEventAdapter.adaptClickEvent(event, this.bridge.getDialogAdapter());
    }

    @Nullable
    public HoverEvent adaptHoverEvent(@NotNull NightHoverEvent<?> event) {
        Object value = event.value();

        if (value instanceof ItemStack itemStack) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) return null;

            String nbt = meta.getAsString();
            String key = BukkitThing.getAsString(itemStack.getType());

            Item item = new Item(key, itemStack.getAmount(), ItemTag.ofNbt(nbt));
            return new HoverEvent(HoverEvent.Action.SHOW_ITEM, item);
        }

        if (value instanceof NightComponent component) {
            Text text = new Text(this.adaptComponent(component));
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
        }

        return null;
    }

    private void adaptProperties(@NotNull BaseComponent spigot, @NotNull NightComponent component) {
        NightClickEvent clickEvent = component.clickEvent();
        NightHoverEvent<?> hoverEvent = component.hoverEvent();

        spigot.setStyle(this.adaptStyle(component.style()));
        spigot.setInsertion(component.insertion());
        spigot.setClickEvent(clickEvent == null ? null : this.adaptClickEvent(clickEvent));
        spigot.setHoverEvent(hoverEvent == null ? null : this.adaptHoverEvent(hoverEvent));
        component.children().forEach(child -> spigot.addExtra(this.adaptComponent(child)));
    }

    @NotNull
    public List<BaseComponent> adaptComponents(@NotNull List<NightComponent> components) {
        return Lists.modify(components, this::adaptComponent);
    }

    @Override
    @NotNull
    public BaseComponent adaptComponent(@NotNull NightComponent component) {
        return component.adapt(this);
    }

    @Override
    @NotNull
    public TextComponent adaptComponent(@NotNull NightTextComponent component) {
        TextComponent spigot = new TextComponent(component.content());
        this.adaptProperties(spigot, component);
        return spigot;
    }

    @Override
    @NotNull
    public KeybindComponent adaptComponent(@NotNull NightKeybindComponent component) {
        KeybindComponent spigot = new KeybindComponent(component.key());
        this.adaptProperties(spigot, component);
        return spigot;
    }

    @Override
    @NotNull
    public TranslatableComponent adaptComponent(@NotNull NightTranslatableComponent component) {
        TranslatableComponent spigot = new TranslatableComponent(component.key());
        spigot.setFallback(component.fallback());
        spigot.setWith(Lists.modify(component.arguments(), argument -> this.adaptComponent(argument.asComponent())));
        this.adaptProperties(spigot, component);
        return spigot;
    }

    @Override
    @NotNull
    public BaseComponent adaptComponent(@NotNull NightObjectComponent component) {
        ObjectComponent spigot = new ObjectComponent(SpigotObjectContentsAdapter.get().adaptContents(component.contents()));
        this.adaptProperties(spigot, component);
        return spigot;
    }
}
