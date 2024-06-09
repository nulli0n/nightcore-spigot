package su.nightexpress.nightcore.language;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class LangAssets {

    private static FileConfig config;

    public static void load() {
        NightCore core = Plugins.CORE;
        String langCode = core.getLanguage();

        String assetsCode = downloadAssets(core, langCode);
        config = FileConfig.loadOrExtract(core, LangManager.DIR_LANG, getFileName(assetsCode));
    }

    @NotNull
    private static String downloadAssets(@NotNull NightCore plugin, @NotNull String langCode) {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + LangManager.DIR_LANG, getFileName(langCode));
        if (file.exists()) return langCode;

        FileUtil.create(file);

        String url = Placeholders.GITHUB_URL + "/raw/master/assets/" + langCode + ".yml";
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            plugin.info("Downloading '" + langCode + "' assets from github...");
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return langCode;
        }
        catch (IOException exception) {
            //exception.printStackTrace();
            plugin.error("Could not download language assets for '" + langCode + "' (no such assets?).");
            return LangManager.isDefault(langCode) ? langCode : downloadAssets(plugin, LangManager.DEFAULT_LANGUAGE);
        }
    }

    @NotNull
    private static String getFileName(@NotNull String langCode) {
        return "assets_" + langCode + ".yml";
    }

    @NotNull
    public static FileConfig getConfig() {
        return config;
    }

    @NotNull
    public static String get(@NotNull PotionEffectType type) {
        return getAsset("PotionEffectType", type);
    }

    @NotNull
    public static String get(@NotNull EntityType type) {
        return getAsset("EntityType", type);
    }

    @NotNull
    public static String get(@NotNull Material type) {
        return getAsset("Material", type);
    }

    @NotNull
    public static String get(@NotNull World world) {
        return getOrCreate("World", world);
    }

    @NotNull
    public static String get(@NotNull Enchantment enchantment) {
        return getOrCreate("Enchantment", enchantment);
    }

    @NotNull
    public static String getAsset(@NotNull String path, @NotNull Keyed keyed) {
        return getAsset(path, BukkitThing.toString(keyed));
    }

    @NotNull
    public static String getAsset(@NotNull String path, @NotNull String nameRaw) {
        return getAsset(path + "." + nameRaw).orElse(nameRaw);
    }

    @NotNull
    public static Optional<String> getAsset(@NotNull String path) {
        return Optional.ofNullable(config.getString(path));//.map(NightMessage::asLegacy);
    }

    @NotNull
    public static String getOrCreate(@NotNull String path, @NotNull Keyed keyed) {
        return getOrCreate(path, BukkitThing.toString(keyed));
    }

    @NotNull
    public static String getOrCreate(@NotNull String path, @NotNull String nameRaw) {
        config.addMissing(path + "." + nameRaw, StringUtil.capitalizeUnderscored(nameRaw));
        config.saveChanges();

        return getAsset(path, nameRaw);
    }
}
