package su.nightexpress.nightcore.user.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.user.UserTemplate;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.TimeUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserRepository<U extends UserTemplate> {

    private final Map<UUID, CachedUser<U>>   loadedByIdMap;
    private final Map<String, CachedUser<U>> loadedByNameMap;
    private final Map<String, UUID> nameToIdMap;

    public UserRepository() {
        this.loadedByIdMap = new ConcurrentHashMap<>();
        this.loadedByNameMap = new ConcurrentHashMap<>();
        this.nameToIdMap = new ConcurrentHashMap<>();
    }

    public synchronized void clear() {
        this.loadedByIdMap.clear();
        this.loadedByNameMap.clear();
        this.nameToIdMap.clear();
    }

    public synchronized void cleanExpired() {
        this.loadedByIdMap.values().removeIf(CachedUser::isExpired);
        this.loadedByNameMap.values().removeIf(CachedUser::isExpired);
    }

    private synchronized void add(@NotNull U user, long expireDate) {
        CachedUser<U> cachedUser = new CachedUser<>(user, expireDate);
        this.loadedByNameMap.put(LowerCase.INTERNAL.apply(user.getName()), cachedUser);
        this.loadedByIdMap.put(user.getId(), cachedUser);
        this.addNameIdMapping(user.getName(), user.getId());
    }

    public synchronized void addTemporary(@NotNull U user, long duration) {
        if (duration == 0L) {
            return;
        }

        if (duration < 0L) {
            this.addPermanent(user);
            return;
        }

        this.add(user, TimeUtil.createFutureTimestamp(duration));
    }

    public synchronized void addNameIdMapping(@NotNull String name, @NotNull UUID uuid) {
        this.nameToIdMap.put(LowerCase.INTERNAL.apply(name), uuid);
    }

    public synchronized void addPermanent(@NotNull U user) {
        this.add(user, -1L);
    }

    public synchronized void remove(@NotNull String name) {
        Optional.ofNullable(this.loadedByNameMap.get(name)).ifPresent(this::remove);
    }

    public synchronized void remove(@NotNull UUID playerId) {
        Optional.ofNullable(this.loadedByIdMap.get(playerId)).ifPresent(this::remove);
    }

    private synchronized void remove(@NotNull CachedUser<U> cachedUser) {
        this.loadedByNameMap.remove(cachedUser.user().getName());
        this.loadedByIdMap.remove(cachedUser.user().getId());
    }

    public boolean contains(@NotNull UUID id) {
        return this.loadedByIdMap.containsKey(id);
    }

    public boolean contains(@NotNull String name) {
        return this.loadedByNameMap.containsKey(LowerCase.INTERNAL.apply(name));
    }

    @NotNull
    public Optional<UUID> associatedId(@NotNull String name) {
        return Optional.ofNullable(this.getAssociatedId(name));
    }

    @Nullable
    public UUID getAssociatedId(@NotNull String name) {
        return this.nameToIdMap.get(LowerCase.INTERNAL.apply(name));
    }

    @NotNull
    public Set<U> getAll() {
        return this.loadedByIdMap.values().stream().map(this::validate).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
    }

    @NotNull
    public Optional<U> getById(@NotNull UUID uuid) {
        return Optional.ofNullable(this.loadedByIdMap.get(uuid)).flatMap(this::validate);
    }

    @NotNull
    public Optional<U> getByName(@NotNull String name) {
        return Optional.ofNullable(this.loadedByNameMap.get(LowerCase.INTERNAL.apply(name))).flatMap(this::validate);
    }

    @NotNull
    private Optional<U> validate(@NotNull CachedUser<U> cachedUser) {
        if (cachedUser.isExpired()) {
            this.remove(cachedUser);
            return Optional.empty();
        }

        return Optional.of(cachedUser.user());
    }

    @NotNull
    public Map<UUID, CachedUser<U>> getLoadedByIdMap() {
        return this.loadedByIdMap;
    }

    @NotNull
    public Map<String, CachedUser<U>> getLoadedByNameMap() {
        return this.loadedByNameMap;
    }
}
