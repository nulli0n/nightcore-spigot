package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ServerUtils {

    @NotNull
    public static <T> Optional<T> serviceProvider(@NotNull Class<T> type) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(type);
        return provider == null ? Optional.empty() : Optional.of(provider.getProvider());
    }
}
