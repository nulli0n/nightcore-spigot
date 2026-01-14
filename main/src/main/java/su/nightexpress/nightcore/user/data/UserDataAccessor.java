package su.nightexpress.nightcore.user.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.user.UserInfo;
import su.nightexpress.nightcore.user.UserTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserDataAccessor<U extends UserTemplate> {

    void addSynchronization(@NotNull Consumer<U> consumer);

    @NotNull List<U> loadAll();

    @NotNull List<UserInfo> loadProfiles();

    @NotNull Optional<U> loadByName(@NotNull String name);

    @NotNull Optional<U> loadById(@NotNull UUID uuid);

    boolean isExists(@NotNull String name);

    boolean isExists(@NotNull UUID uuid);

    void update(@NotNull U user);

    void update(@NotNull Collection<U> users);

    void tinyUpdate(@NotNull U user);

    void tinyUpdate(@NotNull Collection<U> users);

    void insert(@NotNull U user);

    void insert(@NotNull Collection<U> users);

    void deleteByName(@NotNull String username);

    void deleteById(@NotNull UUID userId);
}
