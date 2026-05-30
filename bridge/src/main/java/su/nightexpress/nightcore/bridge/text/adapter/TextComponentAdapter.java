package su.nightexpress.nightcore.bridge.text.adapter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.impl.NightKeybindComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightObjectComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTextComponent;
import su.nightexpress.nightcore.bridge.text.impl.NightTranslatableComponent;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public interface TextComponentAdapter<C> {

    void send(@NonNull CommandSender sender, @NonNull NightComponent component);

    void sendActionBar(@NonNull Player player, @NonNull NightComponent component);

    @NonNull
    String toJson(@NonNull NightComponent component);

    @NonNull
    String toLegacy(@NonNull NightComponent component);

    @NonNull
    C adaptComponent(@NonNull NightComponent component);

    @NonNull
    C adaptComponent(@NonNull NightTextComponent component);

    @NonNull
    C adaptComponent(@NonNull NightKeybindComponent component);

    @NonNull
    C adaptComponent(@NonNull NightTranslatableComponent component);

    @NonNull
    C adaptComponent(@NonNull NightObjectComponent component);
}
