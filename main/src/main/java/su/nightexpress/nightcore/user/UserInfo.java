package su.nightexpress.nightcore.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UserInfo(@NotNull UUID id, @NotNull String name) {

    @NotNull
    public static UserInfo of(@NotNull Player player) {
        return new UserInfo(player.getUniqueId(), player.getName());
    }

    @NotNull
    public static UserInfo of(@NotNull UserTemplate user) {
        return new UserInfo(user.getId(), user.getName());
    }

    public boolean isUser(@NotNull Player player) {
        return player.getUniqueId().equals(this.id);
    }

    public boolean isUser(@NotNull CommandSender sender) {
        return sender instanceof Player player && this.isUser(player);
    }

    public boolean isUser(@NotNull String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
