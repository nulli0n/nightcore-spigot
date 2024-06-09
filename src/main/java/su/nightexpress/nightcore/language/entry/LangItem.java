package su.nightexpress.nightcore.language.entry;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Pair;
import su.nightexpress.nightcore.util.text.NightMessage;
import su.nightexpress.nightcore.util.text.TextRoot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class LangItem extends LangEntry<Pair<String, List<String>>> {

    private final List<String> defaultLore;

    private String localizedName;
    private List<String> localizedLore;

    private TextRoot             wrappedName;
    private List<TextRoot> wrappedLore;

    public LangItem(@NotNull String key, @NotNull String defaultName, @NotNull List<String> defaultLore) {
        super(key, defaultName);
        this.defaultLore = defaultLore;
    }

    @NotNull
    public static LangItem of(@NotNull String key, @NotNull String name, @NotNull String... lore) {
        return new LangItem(key, name, Arrays.asList(lore));
    }

    @Override
    public boolean write(@NotNull FileConfig config) {
        if (!config.contains(this.getPath())) {
            config.set(this.getPath() + ".Name", this.getDefaultName());
            config.set(this.getPath() + ".Lore", this.getDefaultLore());
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public Pair<String, List<String>> load(@NotNull NightCorePlugin plugin) {
        FileConfig config = plugin.getLang();

        this.write(config);

        this.setLocalizedName(config.getString(this.getPath() + ".Name", this.getDefaultName()));
        this.setLocalizedLore(config.getStringList(this.getPath() + ".Lore"));

        return Pair.of(this.getLocalizedName(), this.getLocalizedLore());
    }

    public void apply(@NotNull ItemStack item) {
        ItemUtil.editMeta(item, meta -> {
            meta.setDisplayName(this.getWrappedName().toLegacy());
            meta.setLore(this.getWrappedLore().stream().map(TextRoot::toLegacy).toList());
        });
    }

    @NotNull
    public String getDefaultName() {
        return this.getDefaultText();
    }

    @NotNull
    public List<String> getDefaultLore() {
        return defaultLore;
    }

    @NotNull
    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(@NotNull String localizedName) {
        this.localizedName = localizedName;
        this.wrappedName = NightMessage.create(localizedName);
    }

    @NotNull
    public List<String> getLocalizedLore() {
        return localizedLore;
    }

    public void setLocalizedLore(@NotNull List<String> localizedLore) {
        this.localizedLore = localizedLore;
        this.wrappedLore = new ArrayList<>();
        localizedLore.forEach(line -> wrappedLore.add(NightMessage.create(line)));
    }

    @NotNull
    public TextRoot getWrappedName() {
        return wrappedName;
    }

    @NotNull
    public List<TextRoot> getWrappedLore() {
        return wrappedLore;
    }

    public static final String CLICK     = "Click";
    public static final String LMB       = "Left-Click";
    public static final String RMB       = "Right-Click";
    public static final String DROP_KEY  = "[Q / Drop] Key";
    public static final String SWAP_KEY  = "[F / Swap] Key";
    public static final String SHIFT_LMB = "Shift-Left";
    public static final String SHIFT_RMB = "Shift-Right";
    public static final String DRAG_DROP = "Drag & Drop";

    @NotNull
    public static Builder builder(@NotNull String key) {
        return new Builder(key);
    }

    public static final class Builder {

        private final String key;
        private       String       name;
        private final List<String> lore;

        public Builder(@NotNull String key) {
            this.key = key;
            this.name = "";
            this.lore = new ArrayList<>();
        }

        @NotNull
        public LangItem build() {
            return new LangItem(this.key, this.name, this.lore);
        }

        @NotNull
        public Builder name(@NotNull String name) {
            this.name = LIGHT_YELLOW.enclose(BOLD.enclose(name));
            return this;
        }

        @NotNull
        public Builder text(@NotNull String... text) {
            for (String str : text) {
                this.addLore(LIGHT_GRAY.enclose(str));
            }
            return this;
        }

        @NotNull
        public Builder textRaw(@NotNull String... text) {
            return this.addLore(text);
        }

        @NotNull
        public Builder currentHeader() {
            return this.addLore(LIGHT_YELLOW.enclose(BOLD.enclose("Current:")));
        }

        @NotNull
        public Builder current(@NotNull String type, @NotNull String value) {
            return this.addLore(LIGHT_YELLOW.enclose("● " + LIGHT_GRAY.enclose(type + ": ") + value));
        }

        @NotNull
        public Builder current(@NotNull String value) {
            return this.addLore(LIGHT_YELLOW.enclose("● " + LIGHT_GRAY.enclose(value)));
        }

        @NotNull
        public Builder click(@NotNull String action) {
            return this.click(CLICK, action);
        }

        @NotNull
        public Builder leftClick(@NotNull String action) {
            return this.click(LMB, action);
        }

        @NotNull
        public Builder rightClick(@NotNull String action) {
            return this.click(RMB, action);
        }

        @NotNull
        public Builder shiftLeft(@NotNull String action) {
            return this.click(SHIFT_LMB, action);
        }

        @NotNull
        public Builder shiftRight(@NotNull String action) {
            return this.click(SHIFT_RMB, action);
        }

        @NotNull
        public Builder dropKey(@NotNull String action) {
            return this.click(DROP_KEY, action);
        }

        @NotNull
        public Builder swapKey(@NotNull String action) {
            return this.click(SWAP_KEY, action);
        }

        @NotNull
        public Builder dragAndDrop(@NotNull String action) {
            return this.click(DRAG_DROP, action);
        }

        @NotNull
        public Builder click(@NotNull String click, @NotNull String action) {
            return this.addLore(LIGHT_YELLOW.enclose("[▶]") + " " + LIGHT_GRAY.enclose(click + " to " + LIGHT_YELLOW.enclose(action) + "."));
        }

        @NotNull
        public Builder emptyLine() {
            return this.addLore("");
        }

        /*@NotNull
        private Builder addLore(@NotNull String prefix, @NotNull String... text) {
            for (String str : text) {
                this.lore.add(prefix + str);
            }
            return this;
        }*/

        @NotNull
        private Builder addLore(@NotNull String... text) {
            Collections.addAll(this.lore, text);
            return this;
        }
    }
}
