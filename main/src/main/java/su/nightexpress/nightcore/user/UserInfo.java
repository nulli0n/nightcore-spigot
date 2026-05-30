package su.nightexpress.nightcore.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record UserInfo(@NonNull UUID id, @NonNull String name) {

    @NonNull
    public static UserInfo of(@NonNull Player player) {
        return new UserInfo(player.getUniqueId(), player.getName());
    }

    @NonNull
    public static UserInfo of(@NonNull UserTemplate user) {
        return new UserInfo(user.getId(), user.getName());
    }

    public boolean isUser(@NonNull Player player) {
        return player.getUniqueId().equals(this.id);
    }

    public boolean isUser(@NonNull CommandSender sender) {
        return sender instanceof Player player && this.isUser(player);
    }

    public boolean isUser(@NonNull String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
