package su.nightexpress.nightcore.util;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ServerUtils {

    private ServerUtils() {
    }

    public static <T> Optional<T> serviceProvider(Class<T> type) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(type);
        return provider == null ? Optional.empty() : Optional.of(provider.getProvider());
    }

    public static <T> @Nullable T getProvider(Class<T> type) {
        return serviceProvider(type).orElse(null);
    }
}
