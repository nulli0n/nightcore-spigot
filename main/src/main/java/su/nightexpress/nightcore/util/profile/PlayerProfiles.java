package su.nightexpress.nightcore.util.profile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.bukkit.OfflinePlayer;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.wrap.NightProfile;
import su.nightexpress.nightcore.util.bridge.Software;

public class PlayerProfiles {

    private PlayerProfiles() {
    }

    public static final String TEXTURES_HOST = "http://textures.minecraft.net/texture/";

    @Deprecated
    private static final Map<UUID, CachedProfile> CACHED_PROFILES = new ConcurrentHashMap<>();

    @NonNull
    @Deprecated
    private static CachedProfile cacheTemporary(@NonNull NightProfile profile) {
        return cacheProfile(profile, false, false);
    }

    /* @NonNull
    private static CachedProfile cachePermanent(@NonNull NightProfile profile) {
        return cacheProfile(profile, true, false);
    } */

    @NonNull
    @Deprecated
    public static CachedProfile cacheExact(@NonNull NightProfile profile) {
        return cacheProfile(profile, true, true);
    }

    @NonNull
    @Deprecated
    private static CachedProfile cacheProfile(@NonNull NightProfile profile, boolean permanent, boolean noUpdate) {
        if (profile.getId() == null) {
            return new CachedProfile(profile, true, true);
        }

        CachedProfile cachedProfile = new CachedProfile(profile, permanent, noUpdate);
        CACHED_PROFILES.put(profile.getId(), cachedProfile);
        return cachedProfile;
    }

    @NonNull
    @Deprecated
    private static CachedProfile queryOrCache(@NonNull UUID id, @NonNull Supplier<NightProfile> supplier) {
        CachedProfile cached = getCachedProfile(id);
        if (cached != null) {
            return cached;
        }

        return cacheTemporary(supplier.get());
    }

    @Nullable
    @Deprecated
    public static CachedProfile getCachedProfile(@NonNull UUID id) {
        return CACHED_PROFILES.get(id);
    }

    @NonNull
    @Deprecated
    public static Set<CachedProfile> getCachedProfiles() {
        return new HashSet<>(CACHED_PROFILES.values());
    }

    @Deprecated
    public static void clear() {
        CACHED_PROFILES.clear();
    }

    @Deprecated
    public static void purgeProfiles() {
        CACHED_PROFILES.values().removeIf(CachedProfile::isPurgeTime);
    }

    @NonNull
    @Deprecated
    public static CachedProfile getProfile(@NonNull OfflinePlayer player) {
        return queryOrCache(player.getUniqueId(), () -> Software.get().getProfile(player));
    }

    @NonNull
    @Deprecated
    public static CachedProfile createProfile(@NonNull UUID uuid) {
        return queryOrCache(uuid, () -> Software.get().createProfile(uuid));
    }

    @NonNull
    @Deprecated
    public static NightProfile createProfile(@NonNull String name) {
        return create(name);
    }

    @NonNull
    @Deprecated
    public static CachedProfile createProfile(@NonNull UUID uuid, @Nullable String name) {
        return queryOrCache(uuid, () -> Software.get().createProfile(uuid, name));
    }

    public static @NonNull NightProfile create(@NonNull UUID uuid) {
        return Software.get().createProfile(uuid);
    }


    public static @NonNull NightProfile create(@NonNull String name) {
        return Software.get().createProfile(name);
    }


    public static @NonNull NightProfile create(@NonNull UUID uuid, @Nullable String name) {
        return Software.get().createProfile(uuid, name);
    }

    @Nullable
    @Deprecated
    public static CachedProfile createProfileBySkinURL(@NonNull String urlData) {
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
            return cacheExact(profile);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }


    public static @NonNull NightProfile createStaticTexturedProfile(@NonNull String url) {
        try {
            if (!url.startsWith(TEXTURES_HOST)) {
                url = TEXTURES_HOST + url;
            }

            URL skinUrl = new URI(url).toURL();
            return createStaticTexturedProfile(skinUrl);
        }
        catch (URISyntaxException | MalformedURLException exception) {
            throw new IllegalArgumentException("Could not create profile", exception);
        }
    }

    public static @NonNull NightProfile createStaticTexturedProfile(@NonNull URL skinUrl) {
        NightProfile profile = Software.get().createProfile(null, null);
        PlayerTextures textures = profile.getTextures();

        textures.setSkin(skinUrl);
        profile.setTextures(textures);
        return profile;
    }

    @Nullable
    public static String getProfileSkinURL(@NonNull NightProfile profile) {
        URL skin = profile.getTextures().getSkin();
        if (skin == null) return null;

        String raw = skin.toString();
        return raw.substring(TEXTURES_HOST.length());
    }
}
