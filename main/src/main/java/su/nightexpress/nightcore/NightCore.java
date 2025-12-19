package su.nightexpress.nightcore;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.chat.UniversalChatEventHandler;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.bridge.spigot.SpigotBridge;
import su.nightexpress.nightcore.chat.ChatManager;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreManager;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.command.CoreCommands;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.core.tag.TagManager;
import su.nightexpress.nightcore.integration.currency.CurrencyManager;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.dialog.DialogWatcher;
import su.nightexpress.nightcore.ui.inventory.MenuRegistry;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class NightCore extends NightPlugin {

    public static final Set<NightPlugin> CHILDRENS = new HashSet<>();

    private static NightCore core;

    private final ChatManager chatManager;

    private TagManager    tagManager;
    private CoreManager   coreManager;
    private MenuRegistry menuRegistry;
    private DialogWatcher dialogWatcher;
    private CurrencyManager currencyManager;

    @NotNull
    public static NightCore get() {
        if (core == null) throw new IllegalStateException("NightCore is not initialized!");

        return core;
    }

    public NightCore() {
        this.chatManager = new ChatManager(this);
    }

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("nightcore", new String[]{"nightcore", "ncore"})
            .setConfigClass(CoreConfig.class)
            .setPermissionsClass(CorePerms.class);
    }

    @Override
    protected void addRegistries() {
        this.registerLang(CoreLang.class);
    }

    @Override
    protected boolean disableCommandManager() {
        return true;
    }

    @Override
    protected boolean onInit() {
        core = this;

        Version version = Version.detect();
        if (!version.isDropped()) {
            Software.INSTANCE.load(Version.isPaper() ? new PaperBridge() : new SpigotBridge());
            this.info("Server version detected as " + version.getLocalized() + ". Using " + Software.get().getName() + ".");

            if (!testNbt()) {
                this.error("Could not initialize NBT Utils.");
                return false;
            }

            Plugins.detectPlugins();
        }

        return true;
    }

    @Override
    protected void onStartup() {
        super.onStartup();

        this.menuRegistry = new MenuRegistry(this);
        this.chatManager.setup();
    }

    @Override
    public void enable() {
        LangAssets.load(this);
        UIUtils.load(this);
        this.info("Time zone set as " + TimeUtil.getTimeZone().getID());

        this.tagManager = new TagManager(this);
        this.tagManager.setup();

        this.coreManager = new CoreManager(this);
        this.coreManager.setup();

        this.menuRegistry.setup();

        this.currencyManager = new CurrencyManager(this);
        this.currencyManager.setup();

        if (Version.isAtLeast(Version.MC_1_21_7)) {
            this.dialogWatcher = new DialogWatcher(this);
            this.dialogWatcher.setup();
        }

        this.loadCommands();
    }

    @Override
    public void disable() {
        if (this.dialogWatcher != null) this.dialogWatcher.shutdown();
        if (this.menuRegistry != null) this.menuRegistry.shutdown();
        if (this.coreManager != null) this.coreManager.shutdown();
        if (this.tagManager != null) this.tagManager.shutdown();
        if (this.currencyManager != null) this.currencyManager.shutdown();

        UIUtils.clear();
        LangAssets.shutdown();
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
        PlayerProfiles.clear();
        PlayerBlockTracker.shutdown();

        this.chatManager.shutdown();

        CHILDRENS.clear();
        core = null;
    }

    private void loadCommands() {
        this.rootCommand = NightCommand.forPlugin(this, builder -> CoreCommands.load(this, builder));
    }

    @NotNull
    public Optional<DialogWatcher> getDialogWatcher() {
        return Optional.ofNullable(this.dialogWatcher);
    }

    @NotNull
    public TagManager getTagManager() {
        return this.tagManager;
    }

    @NotNull
    public CurrencyManager getCurrencyManager() {
        return this.currencyManager;
    }

    private static boolean testNbt() {
        try {
            ItemStack testItem = new ItemStack(Material.DIAMOND_SWORD);
            ItemUtil.editMeta(testItem, meta -> {
                ItemUtil.setCustomName(meta, "Test Item");
                ItemUtil.setLore(meta, Lists.newList("Test Lore 1", "Test Lore 2", "Test Lore 3"));
                ItemUtil.setCustomModelData(meta, 100500);
                meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
            });

            ItemTag tag = ItemTag.of(testItem);
            ItemStack parsed = tag.getItemStack();
            return parsed != null && parsed.isSimilar(testItem);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public void addChatHandler(@NotNull EventPriority priority, @NotNull UniversalChatEventHandler handler) {
        this.chatManager.addHandler(priority, handler);
    }

    @Override
    public void removeChatHandler(@NotNull UniversalChatEventHandler handler) {
        this.chatManager.removeHandler(handler);
    }

    @NotNull
    public MenuRegistry getMenuRegistry() {
        return this.menuRegistry;
    }

    @NotNull
    public ChatManager getChatManager() {
        return this.chatManager;
    }
}
