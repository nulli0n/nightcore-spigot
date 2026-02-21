package su.nightexpress.nightcore.user.cache;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.user.UserTemplate;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.TimeUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserRepository<U extends UserTemplate> {

    private final Map<UUID, CachedUser<U>>   loadedByIdMap;
    private final Map<String, CachedUser<U>> loadedByNameMap;
    private final Map<String, UUID>          nameToIdMap;

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

    private synchronized void add(@NonNull U user, long expireDate) {
        CachedUser<U> cachedUser = new CachedUser<>(user, expireDate);
        this.loadedByNameMap.put(LowerCase.INTERNAL.apply(user.getName()), cachedUser);
        this.loadedByIdMap.put(user.getId(), cachedUser);
        this.addNameIdMapping(user.getName(), user.getId());
    }

    public synchronized void addTemporary(@NonNull U user, long duration) {
        if (duration == 0L) {
            return;
        }

        if (duration < 0L) {
            this.addPermanent(user);
            return;
        }

        this.add(user, TimeUtil.createFutureTimestamp(duration));
    }

    public synchronized void addNameIdMapping(@NonNull String name, @NonNull UUID uuid) {
        this.nameToIdMap.put(LowerCase.INTERNAL.apply(name), uuid);
    }

    public synchronized void addPermanent(@NonNull U user) {
        this.add(user, -1L);
    }

    public synchronized void remove(@NonNull String name) {
        Optional.ofNullable(this.loadedByNameMap.get(name)).ifPresent(this::remove);
    }

    public synchronized void remove(@NonNull UUID playerId) {
        Optional.ofNullable(this.loadedByIdMap.get(playerId)).ifPresent(this::remove);
    }

    private synchronized void remove(@NonNull CachedUser<U> cachedUser) {
        this.loadedByNameMap.remove(cachedUser.user().getName());
        this.loadedByIdMap.remove(cachedUser.user().getId());
    }

    public boolean contains(@NonNull UUID id) {
        return this.loadedByIdMap.containsKey(id);
    }

    public boolean contains(@NonNull String name) {
        return this.loadedByNameMap.containsKey(LowerCase.INTERNAL.apply(name));
    }

    @NonNull
    public Optional<UUID> associatedId(@NonNull String name) {
        return Optional.ofNullable(this.getAssociatedId(name));
    }

    @Nullable
    public UUID getAssociatedId(@NonNull String name) {
        return this.nameToIdMap.get(LowerCase.INTERNAL.apply(name));
    }

    public void forEach(@NonNull Consumer<U> consumer) {
        this.stream().forEach(consumer);
    }
    
    @NonNull
    public Stream<U> stream() {
        return this.stream(null);
    }

    @NonNull
    public Stream<U> stream(@Nullable Predicate<U> predicate) {
        return this.loadedByIdMap.values().stream()
            .map(this::validate)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(user -> predicate == null || predicate.test(user));
    }
    
    @NonNull
    public Set<U> getAll() {
        return this.stream().collect(Collectors.toSet());
    }

    @NonNull
    public Optional<U> getById(@NonNull UUID uuid) {
        return Optional.ofNullable(this.loadedByIdMap.get(uuid)).flatMap(this::validate);
    }

    @NonNull
    public Optional<U> getByName(@NonNull String name) {
        return Optional.ofNullable(this.loadedByNameMap.get(LowerCase.INTERNAL.apply(name))).flatMap(this::validate);
    }

    @NonNull
    private Optional<U> validate(@NonNull CachedUser<U> cachedUser) {
        if (cachedUser.isExpired()) {
            this.remove(cachedUser);
            return Optional.empty();
        }

        return Optional.of(cachedUser.user());
    }

    @NonNull
    public Map<UUID, CachedUser<U>> getLoadedByIdMap() {
        return this.loadedByIdMap;
    }

    @NonNull
    public Map<String, CachedUser<U>> getLoadedByNameMap() {
        return this.loadedByNameMap;
    }
}
