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

    private FileConfig config;

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

    public void complete() {
        if (this.config != null) {
            this.config.saveChanges();
            this.config = null; // Clean up memory
        }
    }

    @NotNull
    public static List<LangElement> getElements(@NotNull Class<?> source) {
        return Reflex.getStaticFields(source, LangElement.class, false);
    }

    public static void loadEntries(@NotNull Class<?> source, @NotNull NightPlugin plugin, @NotNull FileConfig config) {
        loadEntries(getElements(source), plugin, config);
    }

    public static void loadEntries(@NotNull Collection<LangElement> elements, @NotNull NightPlugin plugin, @NotNull FileConfig config) {
        elements.forEach(element -> {
            element.load(plugin, config);
        });
    }

    public boolean hasElements() {
        return !this.elements.isEmpty();
    }

    public void register(@NotNull Class<? extends LangContainer> clazz) {
        this.elements.addAll(getElements(clazz));
    }

    /**
     * Saves and loads {@link LangElement} objects from the provided {@link LangContainer} object into the lang config file according to selected
     * language during the "enable" plugin's phase if the same can not be achieved through {@link NightPlugin#registerLang(Class)}
     * <br>
     * <b>Note:</b> This can not be used outside of the {@link NightPlugin#enable()} phase.
     * @param langContainer LangContainer object with some LangElement fields defined.
     * @see NightPlugin#registerLang(Class)
     */
    public void inject(@NotNull LangContainer langContainer) {
        this.inject(langContainer.getClass());
    }

    public void inject(@NotNull Class<? extends LangContainer> langClass) {
        if (this.config != null) {
            loadEntries(langClass, this.plugin, this.config);
        }
        else {
            this.plugin.warn("Lang Container " + langClass.getSimpleName() + " is not injected due to be out of the #enable() phase.");
        }
    }

    public void loadLocale() {
        if (this.elements.isEmpty()) return; // Do not load if nothing registered.

        this.plugin.extractResources(DIRECTORY);

        this.updateLegacy();

        String userLocale = this.plugin.getDetails().getLanguage();

        File file = new File(this.plugin.getDataFolder() + DIRECTORY, this.getFileName(userLocale));
        boolean isSupportedLocale = file.exists();
        String langCode = isSupportedLocale ? userLocale : DEFAULT_LANGUAGE;
        this.config = FileConfig.loadOrExtract(this.plugin, DIRECTORY, this.getFileName(langCode));

        if (!isSupportedLocale) {
            this.plugin.warn("Lang file for the '" + userLocale + "' locale does not exist. Will use the '" + langCode + "' one.");
        }

        loadEntries(this.elements, this.plugin, this.config);
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
    public String getFileName(@NotNull String langCode) {
        return FileConfig.withExtension("lang_" + langCode);
    }
}
