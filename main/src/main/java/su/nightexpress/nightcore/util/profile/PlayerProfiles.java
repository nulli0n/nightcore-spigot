package su.nightexpress.nightcore.util.profile;

import org.bukkit.OfflinePlayer;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.util.bridge.Software;

import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class PlayerProfiles {

    public static final String TEXTURES_HOST = "http://textures.minecraft.net/texture/";

    private static final Map<UUID, CachedProfile> CACHED_PROFILES = new ConcurrentHashMap<>();

    @NotNull
    private static CachedProfile cacheTemporary(@NotNull NightProfile profile) {
        return cacheProfile(profile, false, false);
    }

    @NotNull
    private static CachedProfile cachePermanent(@NotNull NightProfile profile) {
        return cacheProfile(profile, true, false);
    }

    @NotNull
    public static CachedProfile cacheExact(@NotNull NightProfile profile) {
        return cacheProfile(profile, true, true);
    }

    @NotNull
    private static CachedProfile cacheProfile(@NotNull NightProfile profile, boolean permanent, boolean noUpdate) {
        if (profile.getId() == null) {
            return new CachedProfile(profile, true, true);
        }

        CachedProfile cachedProfile = new CachedProfile(profile, permanent, noUpdate);
        CACHED_PROFILES.put(profile.getId(), cachedProfile);
        return cachedProfile;
    }

    @NotNull
    private static CachedProfile queryOrCache(@NotNull UUID id, @NotNull Supplier<NightProfile> supplier) {
        CachedProfile cached = getCachedProfile(id);
        if (cached != null) {
            return cached;
        }

        return cacheTemporary(supplier.get());
    }

    @Nullable
    public static CachedProfile getCachedProfile(@NotNull UUID id) {
        return CACHED_PROFILES.get(id);
    }

    @NotNull
    public static Set<CachedProfile> getCachedProfiles() {
        return new HashSet<>(CACHED_PROFILES.values());
    }

    public static void clear() {
        CACHED_PROFILES.clear();
    }

    /*public static void inspectProfiles() {
        purgeProfiles();
        updateProfiles();
    }

    public static void updateProfiles() {
        getCachedProfiles().forEach(cachedProfile -> {
            if (cachedProfile.isUpdateTime()) {
                cachedProfile.update();
            }
        });
    }*/

    public static void purgeProfiles() {
        CACHED_PROFILES.values().removeIf(CachedProfile::isPurgeTime);
    }

    @NotNull
    public static CachedProfile getProfile(@NotNull OfflinePlayer player) {
        return queryOrCache(player.getUniqueId(), () -> Software.get().getProfile(player));
    }

    @NotNull
    public static CachedProfile createProfile(@NotNull UUID uuid) {
        return queryOrCache(uuid, () -> Software.get().createProfile(uuid));
    }

    @NotNull
    @Deprecated
    public static NightProfile createProfile(@NotNull String name) {
        return Software.get().createProfile(name);
    }

    @NotNull
    public static CachedProfile createProfile(@NotNull UUID uuid, @Nullable String name) {
        return queryOrCache(uuid, () -> Software.get().createProfile(uuid, name));
    }

    @Nullable
    public static CachedProfile createProfileBySkinURL(@NotNull String urlData) {
        if (urlData.isBlank()) return null;

        String name = urlData.substring(0, 16);

        if (!urlData.startsWith(TEXTURES_HOST)) {
            urlData = TEXTURES_HOST + urlData;
        }

        try {
            UUID uuid = UUID.nameUUIDFromBytes(urlData.getBytes());
            CachedProfile cached = getCachedProfile(uuid);
            if (cached != null) return cached;

            // If no name, then meta#getOwnerProfile will return 'null'.
            NightProfile profile = Software.get().createProfile(uuid, name);
            URL url = URI.create(urlData).toURL();
            PlayerTextures textures = profile.getTextures();

            textures.setSkin(url);
            profile.setTextures(textures);
            return CoreConfig.PROFILE_FETCH_CUSTOM.get() ? cachePermanent(profile) : cacheExact(profile);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getProfileSkinURL(@NotNull NightProfile profile) {
        URL skin = profile.getTextures().getSkin();
        if (skin == null) return null;

        String raw = skin.toString();
        return raw.substring(TEXTURES_HOST.length());
    }
}
