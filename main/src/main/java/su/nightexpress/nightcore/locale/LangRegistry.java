package su.nightexpress.nightcore.locale;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.manager.SimpleManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LangRegistry extends SimpleManager<NightPlugin> {

    public static final String DEFAULT_LANGUAGE = "en";
    public static final String DIRECTORY        = "/lang/";

    private final List<LangElement> elements;

    public LangRegistry(@NotNull NightPlugin plugin) {
        super(plugin);
        this.elements = new ArrayList<>();
    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onShutdown() {
        this.elements.clear();
    }

    @NotNull
    public static List<LangElement> getElements(@NotNull Class<?> source) {
        return Reflex.getStaticFields(source, LangElement.class, false);
    }

    public static void loadEntries(@NotNull Class<?> source, @NotNull NightPlugin plugin, @NotNull FileConfig config) {
        getElements(source).forEach(element -> {
            element.load(plugin, config, DEFAULT_LANGUAGE);
        });
    }

    public boolean hasElements() {
        return !this.elements.isEmpty();
    }

    public void register(@NotNull Class<? extends LangContainer> clazz) {
        this.elements.addAll(getElements(clazz));
    }

    public void loadLocale() {
        if (this.elements.isEmpty()) return; // Do not load if nothing registered.

        this.plugin.extractResources(DIRECTORY);

        this.updateLegacy();

        String userLocale = this.plugin.getDetails().getLanguage();
        Map<String, FileConfig> configMap = new HashMap<>();

        this.elements.forEach(langElement -> {
            // If user locale is not supported, fallback to the default (English) one.
            String preferredLocale = langElement.isSupportedLocale(userLocale) ? userLocale : DEFAULT_LANGUAGE;

            // Load each translation from/to it's own locale config file.
            langElement.getSupportedLocales().forEach(supportedLocale -> {
                FileConfig config = configMap.computeIfAbsent(supportedLocale, k -> this.getConfig(supportedLocale));
                if (!config.contains(langElement.getPath())) {
                    //langElement.write(config);
                    config.set(langElement.getPath(), langElement.getDefaultValue(supportedLocale));
                }

                // If supported locale matches user's or default one, read it from the config.
                if (supportedLocale.equalsIgnoreCase(preferredLocale)) {
                    langElement.load(this.plugin, config, preferredLocale);
                }
            });
        });

        configMap.values().forEach(FileConfig::saveChanges);
    }

    private void updateLegacy() {
        FileUtil.getConfigFiles(this.plugin.getDataFolder() + DIRECTORY).forEach(messagesFile -> {
            String messagesName = messagesFile.getName();

            if (!messagesName.startsWith("messages_")) return;

            String langCode = messagesName.substring("messages_".length(), messagesName.length() - 4);
            String dirPath = this.plugin.getDataFolder() + DIRECTORY;

            File langFile = new File(dirPath, this.getFileName(langCode));
            if (langFile.exists()) return;

            Path originPath = Paths.get(messagesFile.getPath());
            Path backupPath = Paths.get(messagesFile.getPath() + ".backup");
            Path renewPath = Paths.get(dirPath + this.getFileName(langCode));

            try {
                Files.copy(originPath, backupPath);

                FileConfig messagesConfig = new FileConfig(messagesFile);
                this.elements.forEach(element -> {
                    if (!(element instanceof MessageLocale messageLocale)) return;

                    List<String> text = new ArrayList<>(messagesConfig.getStringList(messageLocale.getPath()));
                    if (text.isEmpty()) {
                        text.add(messagesConfig.getString(messageLocale.getPath(), ""));
                    }

                    String inline = String.join(TagWrappers.BR, text);
                    if (!MessageData.hasLegacyData(inline)) return;

                    MessageData.Builder builder = MessageData.builder();
                    String message = MessageData.extractAndParseOld(inline, builder);
                    MessageData data = builder.build();

                    String[] textLines = ParserUtils.breakDownLineSplitters(data.serialize() + message);
                    if (textLines.length == 1) {
                        messagesConfig.set(messageLocale.getPath(), textLines[0]);
                    }
                    else {
                        messagesConfig.set(messageLocale.getPath(), Arrays.asList(textLines));
                    }
                });
                messagesConfig.saveChanges();

                Files.move(originPath, renewPath);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    @NotNull
    public FileConfig getConfig(@NotNull String langCode) {
        return FileConfig.loadOrExtract(this.plugin, DIRECTORY, this.getFileName(langCode));
    }

    @NotNull
    public String getFileName(@NotNull String langCode) {
        return FileConfig.withExtension("lang_" + langCode);
    }
}
