package su.nightexpress.nightcore.user.data;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.user.UserInfo;
import su.nightexpress.nightcore.user.UserTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserDataAccessor<U extends UserTemplate> {

    void addSynchronization(@NonNull Consumer<U> consumer);

    @NonNull
    List<U> loadAll();

    @NonNull
    List<UserInfo> loadProfiles();

    @NonNull
    Optional<U> loadByName(@NonNull String name);

    @NonNull
    Optional<U> loadById(@NonNull UUID uuid);

    boolean isExists(@NonNull String name);

    boolean isExists(@NonNull UUID uuid);

    void update(@NonNull U user);

    void update(@NonNull Collection<U> users);

    void tinyUpdate(@NonNull U user);

    void tinyUpdate(@NonNull Collection<U> users);

    void insert(@NonNull U user);

    void insert(@NonNull Collection<U> users);

    void deleteByName(@NonNull String username);

    void deleteById(@NonNull UUID userId);
}
