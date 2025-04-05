package su.nightexpress.nightcore.util.bridge.wrapper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public interface NightComponent {

    @NotNull NightComponent duplicate();

    @NotNull String toJson();

    @NotNull String toLegacy();

    void send(@NotNull CommandSender sender);

    void sendActionBar(@NotNull Player player);

    boolean isEmpty();

    boolean isText();

    @NotNull String getText();

    @NotNull List<? extends NightComponent> getChildrens();

    void setChildrens(@NotNull List<NightComponent> childrens);

    void addChildren(@NotNull NightComponent other);

    void setText(@NotNull String text);

    void setColor(@NotNull Color color);

    void setClickEvent(@NotNull ClickEventType type, @NotNull String value);

    void setHoverEvent(@NotNull HoverEventType type, @NotNull String value);

    void setFont(@NotNull String font);

    void setBold(boolean bold);

    void setItalic(boolean italic);

    void setObfuscated(boolean obfuscated);

    void setUnderlined(boolean underlined);

    void setStrikethrough(boolean strikethrough);
}
