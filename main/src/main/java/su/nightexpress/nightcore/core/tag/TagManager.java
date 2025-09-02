package su.nightexpress.nightcore.core.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.tag.TagHandlerRegistry;
import su.nightexpress.nightcore.util.text.night.tag.TagShortNames;
import su.nightexpress.nightcore.util.text.night.tag.handler.*;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.io.File;
import java.util.*;

public class TagManager extends AbstractManager<NightCore> {

    public static final String COLORS_FILE = FileConfig.withExtension("color_schemes");

    private final Map<String, ColorScheme> colorSchemeByIdMap;

    private ColorScheme colorScheme;

    public TagManager(@NotNull NightCore plugin) {
        super(plugin);
        this.colorSchemeByIdMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.loadColorSchemes();
        this.loadColorTags();
        this.loadTags();
    }

    @Override
    protected void onShutdown() {
        if (this.colorScheme != null) {
            this.colorScheme.getColors().forEach(code -> TagHandlerRegistry.unregister(code.name()));
            this.colorScheme = null;
        }

        TagHandlerRegistry.clear();
    }

    public void loadTags() {
        TagHandlerRegistry.register(ColorTagHandler::new, TagShortNames.COLOR, "color", "colour");
        TagHandlerRegistry.register(GradientTagHandler::new, TagShortNames.GRADIENT);
        TagHandlerRegistry.register(ShadowTagHandler::new, TagShortNames.SHADOW_COLOR);
        TagHandlerRegistry.register(FontTagHandler::new, TagShortNames.FONT);
        TagHandlerRegistry.register(LangTagHandler::new, TagShortNames.LANG, TagShortNames.LANG_OR, "tr", "translate", "tr_or", "translate_or");
        TagHandlerRegistry.register(HoverTagHandler::new, TagShortNames.HOVER);
        TagHandlerRegistry.register(ClickTagHandler::new, TagShortNames.CLICK);
        TagHandlerRegistry.register(ResetTagHandler::new, TagShortNames.RESET, "reset");
        TagHandlerRegistry.register(KeybindTagHandler::new, TagShortNames.KEYBIND);
        TagHandlerRegistry.register(InsertionTagHandler::new, TagShortNames.INSERTION);

        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.BOLD, true), TagShortNames.BOLD, "bold");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.ITALIC, true), TagShortNames.ITALIC, "italic", "em");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.OBFUSCATED, true), TagShortNames.OBFUSCATED, "obfuscated");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.STRIKETHROUGH, true), TagShortNames.STRIKETHROUGH, "strikethrough");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.UNDERLINED, true), TagShortNames.UNDERLINED, "underlined");

        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.BOLD, false), TagShortNames.UNBOLD, "!bold");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.ITALIC, false), TagShortNames.UNITALIC, "!italic", "!em");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.OBFUSCATED, false), TagShortNames.UNOBFUSCATED, "!obfuscated");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.STRIKETHROUGH, false), TagShortNames.UNSTRIKETHROUGH, "!strikethrough");
        TagHandlerRegistry.register(() -> DecorationTagHandler.normal(NightTextDecoration.UNDERLINED, false), TagShortNames.UNUNDERLINED, "!underlined");

        TagHandlerRegistry.register(() -> new PlaceholderTagHandler("\n"), TagShortNames.BR, "newline");
    }

    public void loadColorTags() {
        if (this.colorScheme == null) return;

        this.colorScheme.getColors().forEach(code -> {
            TagHandlerRegistry.register(code::createHandler, code.name());
        });

        Tags.registerFromScheme(this.colorScheme);
    }

    public void loadColorSchemes() {
        FileConfig config = FileConfig.loadOrExtract(this.plugin, COLORS_FILE);

        String schemeId = ConfigValue.create("Selected", ColorScheme.CUSTOM).read(config);
        String path = "ColorSchemes";

        if (config.getSection(path).isEmpty()) {
            ColorScheme.getDefaultSchemes().forEach(scheme -> scheme.write(config, path + "." + scheme.getId()));
        }

        this.migrateOldSettings(config);

        config.getSection(path).forEach(sId -> {
            ColorScheme scheme = ColorScheme.read(config, path + "." + sId, sId);
            this.colorSchemeByIdMap.put(scheme.getId(), scheme);
        });

        config.saveChanges();

        this.plugin.info("Loaded " + this.colorSchemeByIdMap.size() + " color schemes.");

        this.setColorScheme(schemeId);
    }

    public void setColorScheme(@NotNull String name) {
        ColorScheme scheme = this.getColorScheme(name);
        if (scheme == null) {
            if (!name.equalsIgnoreCase(ColorScheme.DEFAULT)) {
                this.plugin.error("Color scheme '" + name + "' not found. Try fallback to the '" + ColorScheme.DEFAULT + "' one...");
                this.setColorScheme(ColorScheme.DEFAULT);
            }
            else {
                this.plugin.error("No default color scheme was found. Color tags will be unavailable.");
            }
            return;
        }

        this.colorScheme = scheme;
        this.plugin.info("Using '" + this.colorScheme.getId() + "' color scheme.");
    }

    private void migrateOldSettings(@NotNull FileConfig target) {
        File fromFile = new File(this.plugin.getDataFolder(), "colors.yml");
        if (!fromFile.exists()) return;

        FileConfig from = new FileConfig(fromFile);
        if (!from.contains("Colors")) return;

        ColorScheme scheme = ColorScheme.read(from, "", ColorScheme.CUSTOM);

        scheme.getColors().forEach(code -> {
            target.set("ColorSchemes." + scheme.getId() + ".Colors." + code.name(), ParserUtils.colorToHexString(code.color()));
        });

        //scheme.write(target, "ColorSchemes." + scheme.getId());
        fromFile.renameTo(new File(this.plugin.getDataFolder(), "colors.old"));
    }

    @NotNull
    public Map<String, ColorScheme> getColorSchemeByIdMap() {
        return this.colorSchemeByIdMap;
    }

    @Nullable
    public ColorScheme getColorScheme(@NotNull String id) {
        return this.colorSchemeByIdMap.get(id.toLowerCase());
    }

    @NotNull
    public Set<ColorScheme> getColorSchemes() {
        return new HashSet<>(this.colorSchemeByIdMap.values());
    }

    @NotNull
    public Optional<ColorScheme> getColorScheme() {
        return Optional.ofNullable(this.colorScheme);
    }
}
