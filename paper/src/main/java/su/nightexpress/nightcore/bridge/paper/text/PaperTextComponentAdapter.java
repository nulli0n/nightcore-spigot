package su.nightexpress.nightcore.bridge.paper.text;

import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.bridge.text.adapter.TextComponentAdapter;
import su.nightexpress.nightcore.bridge.text.event.NightClickEvent;
import su.nightexpress.nightcore.bridge.text.event.NightHoverEvent;
import su.nightexpress.nightcore.bridge.text.event.WrappedPayload;
import su.nightexpress.nightcore.bridge.text.impl.*;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class PaperTextComponentAdapter implements TextComponentAdapter<Component> {

    private final PaperBridge bridge;

    public PaperTextComponentAdapter(@NotNull PaperBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void send(@NotNull CommandSender sender, @NotNull NightComponent component) {
        sender.sendMessage(this.adaptComponent(component));
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull NightComponent component) {
        player.sendActionBar(this.adaptComponent(component));
    }

    @Override
    @NotNull
    public String toJson(@NotNull NightComponent component) {
        return JSONComponentSerializer.json().serialize(this.adaptComponent(component));
    }

    @Override
    @NotNull
    public String toLegacy(@NotNull NightComponent component) {
        return LegacyComponentSerializer.legacy('§').serialize(this.adaptComponent(component));
    }

    @NotNull
    public ClickEvent adaptClickEvent(@NotNull NightClickEvent nightClickEvent) {
        WrappedPayload payload = nightClickEvent.payload();

        return switch (nightClickEvent.action()) {
            case CUSTOM -> {
                WrappedPayload.Custom custom = (WrappedPayload.Custom) payload;
                NightKey namespacedKey = custom.key();
                Key key = Key.key(namespacedKey.namespace(), namespacedKey.value());
                BinaryTagHolder nbt = BinaryTagHolder.binaryTagHolder(custom.nbt().asString());

                yield ClickEvent.custom(key, nbt);
            }
            case OPEN_URL -> ClickEvent.openUrl(((WrappedPayload.Text)payload).value());
            case OPEN_FILE -> ClickEvent.openFile(((WrappedPayload.Text)payload).value());
            case CHANGE_PAGE -> ClickEvent.changePage(((WrappedPayload.Int)payload).integer());
            case RUN_COMMAND -> ClickEvent.runCommand(((WrappedPayload.Text)payload).value());
            case SHOW_DIALOG -> ClickEvent.showDialog((DialogLike) this.bridge.getDialogAdapter().adaptDialog(((WrappedPayload.Dialog)payload).dialog()));
            case SUGGEST_COMMAND -> ClickEvent.suggestCommand(((WrappedPayload.Text)payload).value());
            case COPY_TO_CLIPBOARD -> ClickEvent.copyToClipboard(((WrappedPayload.Text)payload).value());
        };
    }

    @Nullable
    public <V> HoverEvent<?> adaptHoverEvent(@NotNull NightHoverEvent<V> event) {
        Object value = event.value();

        if (value instanceof ItemStack itemStack) {
            return itemStack.asHoverEvent();
        }

        if (value instanceof NightComponent component) {
            return HoverEvent.showText(this.adaptComponent(component));
        }

        return null;
    }

    @NotNull
    public TextDecoration adaptTextDecoration(@NotNull NightTextDecoration nightDecoration) {
        return switch (nightDecoration) {
            case BOLD -> TextDecoration.BOLD;
            case ITALIC -> TextDecoration.ITALIC;
            case OBFUSCATED -> TextDecoration.OBFUSCATED;
            case UNDERLINED -> TextDecoration.UNDERLINED;
            case STRIKETHROUGH -> TextDecoration.STRIKETHROUGH;
        };
    }

    @NotNull
    public TextDecoration.State adaptTextDecorationState(@NotNull NightTextDecoration.State nightState) {
        return switch (nightState) {
            case TRUE -> TextDecoration.State.TRUE;
            case FALSE -> TextDecoration.State.FALSE;
            case NOT_SET -> TextDecoration.State.NOT_SET;
        };
    }

    @NotNull
    public Style adaptStyle(@NotNull NightStyle nightStyle) {
        NightKey font = nightStyle.font();
        Color color = nightStyle.color();
        Color shadowColor = nightStyle.shadowColor();
        Map<NightTextDecoration, NightTextDecoration.State> decorations = nightStyle.decorations();
        NightClickEvent clickEvent = nightStyle.clickEvent();
        NightHoverEvent<?> hoverEvent = nightStyle.hoverEvent();
        String insertion = nightStyle.insertion();

        return Style.style(builder -> {
            decorations.forEach((nightDecoration, nightState) -> {
                builder.decoration(this.adaptTextDecoration(nightDecoration), this.adaptTextDecorationState(nightState));
            });

            if (Version.isAtLeast(Version.MC_1_21_6)) {
                Compat.setShadowColor(builder, shadowColor);
            }

            builder
                .font(font == null ? null : Key.key(font.namespace(), font.value()))
                .color(color == null ? null : TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))
                .clickEvent(clickEvent == null ? null : this.adaptClickEvent(clickEvent))
                .hoverEvent(hoverEvent == null ? null : this.adaptHoverEvent(hoverEvent))
                .insertion(insertion);
            }
        );
    }

    @NotNull
    public List<Component> adaptComponents(@NotNull List<NightComponent> components) {
        return Lists.modify(components, this::adaptComponent);
    }

    @Override
    @NotNull
    public Component adaptComponent(@NotNull NightComponent nightComponent) {
        return nightComponent.adapt(this);
    }

    @Override
    @NotNull
    public TextComponent adaptComponent(@NotNull NightTextComponent component) {
        String content = component.content();

        if (content.equals("\n")) return Component.newline();

        return Component.text(builder -> builder
            .style(this.adaptStyle(component.style()))
            .append(this.adaptComponents(component.children()))
            .content(content));
    }

    @NotNull
    public TranslationArgument adaptTranslatableArgument(@NotNull NightTranslationArgument nightArgument) {
        return TranslationArgument.component(this.adaptComponent(nightArgument.asComponent()));
    }

    @Override
    @NotNull
    public TranslatableComponent adaptComponent(@NotNull NightTranslatableComponent component) {
        String fallback = component.fallback();
        List<NightTranslationArgument> nightArguments = component.arguments();
        List<TranslationArgument> arguments = Lists.modify(nightArguments, this::adaptTranslatableArgument);

        return Component.translatable(builder -> builder
            .style(this.adaptStyle(component.style()))
            .append(this.adaptComponents(component.children()))
            .key(component.key())
            .fallback(fallback)
            .arguments(arguments));
    }

    @Override
    @NotNull
    public Component adaptComponent(@NotNull NightKeybindComponent component) {
        return Component.keybind(builder -> builder
            .style(this.adaptStyle(component.style()))
            .append(this.adaptComponents(component.children()))
            .keybind(component.key())
        );
    }

    @Override
    @NotNull
    public Component adaptComponent(@NotNull NightObjectComponent component) {
        return Component.object()
            .style(this.adaptStyle(component.style()))
            .append(this.adaptComponents(component.children()))
            .contents(PaperObjectContantsAdapter.get().adaptContents(component.contents()))
            .build();
    }

    // Quick fix for <1.21.4 loading.
    static class Compat {

        static void setShadowColor(@NotNull Style.Builder builder, @Nullable Color shadowColor) {
            builder.shadowColor(shadowColor == null ? null : ShadowColor.shadowColor(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), shadowColor.getAlpha()));
        }
    }
}
