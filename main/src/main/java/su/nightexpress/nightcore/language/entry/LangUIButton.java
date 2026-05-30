package su.nightexpress.nightcore.language.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.ui.menu.click.ClickKey;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.*;

@Deprecated
public class LangUIButton implements LangElement {

    private final String  path;
    private final Details defaults;

    private Details details;

    protected LangUIButton(@NonNull String path, @NonNull Details defaults) {
        this.path = path;
        this.defaults = defaults;
        this.details = defaults;
    }

    @NonNull
    public static Builder builder(@NonNull String path, @NonNull String name) {
        return new Builder(path, name);
    }

    protected record Details(String name, List<String> description, boolean formatted, Map<String, String> currentInfo,
                             Map<ClickKey, String> clickActions) {
    }

    @Override
    public void write(@NonNull FileConfig config) {
        config.set(this.path + ".Name", this.defaults.name);
        config.set(this.path + ".Description", this.defaults.description);
        config.set(this.path + ".Formatted", this.defaults.formatted);

        config.remove(this.path + ".CurrentInfo");
        this.defaults.currentInfo.forEach((name, value) -> config.set(this.path + ".CurrentInfo." + name, value));

        config.remove(this.path + ".ClickActions");
        this.defaults.clickActions.forEach((key, action) -> config.set(this.path + ".ClickActions." + key.name(),
            action));
    }

    @Override
    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        if (!config.contains(this.path) || (!config.contains(this.path + ".ClickActions") && !this.defaults.clickActions
            .isEmpty())) {
            this.write(config);
        }

        String name = ConfigValue.create(this.path + ".Name", this.defaults.name).read(config);
        List<String> description = ConfigValue.create(this.path + ".Description", this.defaults.description).read(
            config);
        boolean formatted = ConfigValue.create(this.path + ".Formatted", this.defaults.formatted).read(config);

        Map<String, String> currentInfo = new LinkedHashMap<>();
        config.getSection(this.path + ".CurrentInfo").forEach(sId -> {
            String value = config.getString(this.path + ".CurrentInfo." + sId);
            if (value == null) return;

            currentInfo.put(sId, value);
        });

        Map<ClickKey, String> clickActions = new LinkedHashMap<>();
        config.getSection(this.path + ".ClickActions").forEach(sId -> {
            ClickKey key = StringUtil.getEnum(sId, ClickKey.class).orElse(null);
            if (key == null) return;

            String action = config.getString(this.path + ".ClickActions." + sId);
            if (action == null) return;

            clickActions.put(key, action);
        });

        this.details = new Details(name, description, formatted, currentInfo, clickActions);
    }

    @NonNull
    public String getName() {
        return this.details.name;
    }

    @NonNull
    public List<String> getDescription() {
        return this.details.description;
    }

    public boolean isFormatted() {
        return this.details.formatted;
    }

    @NonNull
    public Map<String, String> getCurrentInfo() {
        return this.details.currentInfo;
    }

    @NonNull
    public Map<ClickKey, String> getClickActions() {
        return this.details.clickActions;
    }

    public static final class Builder {

        private final String path;

        private final List<String>          description;
        private final Map<String, String>   currentInfo;
        private final Map<ClickKey, String> clickActions;

        private String  name;
        private boolean formatted;

        public Builder(@NonNull String path, @NonNull String name) {
            this.path = path;
            this.name = name;
            this.formatted = true;
            this.description = new ArrayList<>();
            this.currentInfo = new LinkedHashMap<>();
            this.clickActions = new LinkedHashMap<>();
        }

        @NonNull
        public LangUIButton build() {
            return new LangUIButton(this.path, new Details(this.name, new ArrayList<>(this.description), this.formatted, this.currentInfo, new HashMap<>(this.clickActions)));
        }

        @NonNull
        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        @NonNull
        public Builder description(@NonNull String... text) {
            this.description.addAll(Arrays.asList(text));
            return this;
        }

        @NonNull
        public Builder formatted(boolean formatted) {
            this.formatted = formatted;
            return this;
        }

        @NonNull
        public Builder current(@NonNull String value) {
            return this.current(CoreLang.EDITOR_BUTTON_CURRENT_DEFAULT_NAME.getString(), value);
        }

        @NonNull
        public Builder current(@NonNull String name, @NonNull String value) {
            this.currentInfo.put(name, value);
            return this;
        }

        @NonNull
        public Builder click(@NonNull String action) {
            return this.leftClick(action);
        }

        @NonNull
        public Builder leftClick(@NonNull String action) {
            return this.click(ClickKey.LEFT, action);
        }

        @NonNull
        public Builder rightClick(@NonNull String action) {
            return this.click(ClickKey.RIGHT, action);
        }

        @NonNull
        public Builder shiftLeft(@NonNull String action) {
            return this.click(ClickKey.SHIFT_LEFT, action);
        }

        @NonNull
        public Builder shiftRight(@NonNull String action) {
            return this.click(ClickKey.SHIFT_RIGHT, action);
        }

        @NonNull
        public Builder dropKey(@NonNull String action) {
            return this.click(ClickKey.DROP_KEY, action);
        }

        @NonNull
        public Builder swapKey(@NonNull String action) {
            return this.click(ClickKey.SWAP_KEY, action);
        }

        @NonNull
        public Builder dragAndDrop(@NonNull String action) {
            return this.click(ClickKey.DRAG_N_DROP, action);
        }

        @NonNull
        public Builder click(@NonNull ClickKey click, @NonNull String action) {
            this.clickActions.put(click, action);
            return this;
        }
    }
}

