package su.nightexpress.nightcore.language.entry;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@Deprecated
public class LangItem extends LangEntry {

    private final List<String> defaultLore;

    private String       localizedName;
    private List<String> localizedLore;

    private TextRoot       wrappedName;
    private List<TextRoot> wrappedLore;

    public LangItem(@NonNull String key, @NonNull String defaultName, @NonNull List<String> defaultLore) {
        super(key, defaultName);
        this.defaultLore = defaultLore;

        this.setLocalizedName(defaultName);
        this.setLocalizedLore(defaultLore);
    }

    @NonNull
    public static LangItem of(@NonNull String key, @NonNull String name, @NonNull String... lore) {
        return new LangItem(key, name, Arrays.asList(lore));
    }

    @Override
    public void write(@NonNull FileConfig config) {
        config.set(this.path + ".Name", this.defaultText);
        config.set(this.path + ".Lore", this.defaultLore);
    }

    @Override
    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        if (!config.contains(this.path)) {
            this.write(config);
        }

        this.setLocalizedName(config.getString(this.path + ".Name", this.defaultText));
        this.setLocalizedLore(config.getStringList(this.path + ".Lore"));
    }

    public void apply(@NonNull ItemStack item) {
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(this.wrappedName.toLegacy());
            meta.setLore(this.wrappedLore.stream().map(TextRoot::toLegacy).toList());
        });
    }

    @Deprecated
    public void apply(@NonNull NightItem item) {
        item.setDisplayName(this.localizedName);
        item.setLore(this.localizedLore);
    }

    @NonNull
    public String getDefaultName() {
        return this.defaultText;
    }

    @NonNull
    public List<String> getDefaultLore() {
        return this.defaultLore;
    }

    @NonNull
    public String getLocalizedName() {
        return this.localizedName;
    }

    public void setLocalizedName(@NonNull String localizedName) {
        this.localizedName = localizedName;
        this.wrappedName = NightMessage.create(localizedName);
    }

    @NonNull
    public List<String> getLocalizedLore() {
        return this.localizedLore;
    }

    public void setLocalizedLore(@NonNull List<String> localizedLore) {
        this.localizedLore = localizedLore;
        this.wrappedLore = new ArrayList<>();
        localizedLore.forEach(line -> wrappedLore.add(NightMessage.create(line)));
    }

    @NonNull
    public TextRoot getWrappedName() {
        return this.wrappedName;
    }

    @NonNull
    public List<TextRoot> getWrappedLore() {
        return this.wrappedLore;
    }

    public static final String CLICK     = "Click";
    public static final String LMB       = "Left-Click";
    public static final String RMB       = "Right-Click";
    public static final String DROP_KEY  = "[Q / Drop] Key";
    public static final String SWAP_KEY  = "[F / Swap] Key";
    public static final String SHIFT_LMB = "Shift-Left";
    public static final String SHIFT_RMB = "Shift-Right";
    public static final String DRAG_DROP = "Drag & Drop";

    @NonNull
    public static Builder builder(@NonNull String key) {
        return new Builder(key);
    }

    public static final class Builder {

        private final String       key;
        private String             name;
        private final List<String> lore;

        public Builder(@NonNull String key) {
            this.key = key;
            this.name = "";
            this.lore = new ArrayList<>();
        }

        @NonNull
        public LangItem build() {
            return new LangItem(this.key, this.name, this.lore);
        }

        @NonNull
        public Builder name(@NonNull String name) {
            this.name = LIGHT_YELLOW.enclose(BOLD.enclose(name));
            return this;
        }

        @NonNull
        public Builder text(@NonNull String... text) {
            for (String str : text) {
                this.addLore(LIGHT_GRAY.enclose(str));
            }
            return this;
        }

        @NonNull
        public Builder textRaw(@NonNull String... text) {
            return this.addLore(text);
        }

        @NonNull
        public Builder currentHeader() {
            return this.addLore(LIGHT_YELLOW.enclose(BOLD.enclose("Current:")));
        }

        @NonNull
        public Builder current(@NonNull String type, @NonNull String value) {
            return this.addLore(LIGHT_YELLOW.enclose("● " + LIGHT_GRAY.enclose(type + ": ") + value));
        }

        @NonNull
        public Builder current(@NonNull String value) {
            return this.addLore(LIGHT_YELLOW.enclose("● " + LIGHT_GRAY.enclose(value)));
        }

        @NonNull
        public Builder click(@NonNull String action) {
            return this.click(CLICK, action);
        }

        @NonNull
        public Builder leftClick(@NonNull String action) {
            return this.click(LMB, action);
        }

        @NonNull
        public Builder rightClick(@NonNull String action) {
            return this.click(RMB, action);
        }

        @NonNull
        public Builder shiftLeft(@NonNull String action) {
            return this.click(SHIFT_LMB, action);
        }

        @NonNull
        public Builder shiftRight(@NonNull String action) {
            return this.click(SHIFT_RMB, action);
        }

        @NonNull
        public Builder dropKey(@NonNull String action) {
            return this.click(DROP_KEY, action);
        }

        @NonNull
        public Builder swapKey(@NonNull String action) {
            return this.click(SWAP_KEY, action);
        }

        @NonNull
        public Builder dragAndDrop(@NonNull String action) {
            return this.click(DRAG_DROP, action);
        }

        @NonNull
        public Builder click(@NonNull String click, @NonNull String action) {
            return this.addLore(LIGHT_YELLOW.enclose("[▶]") + " " + LIGHT_GRAY.enclose(click + " to " + LIGHT_YELLOW
                .enclose(action) + "."));
        }

        @NonNull
        public Builder emptyLine() {
            return this.addLore("");
        }

        /*@NonNull
        private Builder addLore(@NonNull String prefix, @NonNull String... text) {
            for (String str : text) {
                this.lore.add(prefix + str);
            }
            return this;
        }*/

        @NonNull
        private Builder addLore(@NonNull String... text) {
            Collections.addAll(this.lore, text);
            return this;
        }
    }
}
