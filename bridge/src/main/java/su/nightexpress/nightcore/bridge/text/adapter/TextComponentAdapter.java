package su.nightexpress.nightcore.bridge.text.adapter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.impl.NightKeybindComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightObjectComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTextComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTranslatableComponent;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface TextComponentAdapter<C> {

    void send(@NotNull CommandSender sender, @NotNull NightComponent component);

    void sendActionBar(@NotNull Player player, @NotNull NightComponent component);

    @NotNull String toJson(@NotNull NightComponent component);

    @NotNull String toLegacy(@NotNull NightComponent component);

    @NotNull C adaptComponent(@NotNull NightComponent component);

    @NotNull C adaptComponent(@NotNull NightTextComponent component);

    @NotNull C adaptComponent(@NotNull NightKeybindComponent component);

    @NotNull C adaptComponent(@NotNull NightTranslatableComponent component);

    @NotNull C adaptComponent(@NotNull NightObjectComponent component);
}
