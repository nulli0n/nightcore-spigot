package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ServerUtils {

    @NonNull
    public static <T> Optional<T> serviceProvider(@NonNull Class<T> type) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(type);
        return provider == null ? Optional.empty() : Optional.of(provider.getProvider());
    }
}
