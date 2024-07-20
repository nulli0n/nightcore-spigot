package su.nightexpress.nightcore;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.command.experimental.impl.ReloadCommand;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CoreManager;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.command.CheckPermCommand;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;

import java.util.HashSet;
import java.util.Set;

public class NightCore extends NightPlugin implements ImprovedCommands {

    private final Set<NightCorePlugin> childrens;
    private final CoreManager          coreManager;

    public NightCore() {
        this.childrens = new HashSet<>();
        this.coreManager = new CoreManager(this);
    }

    @Override
    public void enable() {
        LangAssets.load();

        ChainedNode rootNode = this.getRootNode();

        if (Plugins.hasVault()) {
            VaultHook.setup();
            CheckPermCommand.inject(this, rootNode);
        }
        ReloadCommand.inject(this, rootNode, CorePerms.COMMAND_RELOAD);

        this.testMethods();
        this.coreManager.setup();
    }

    @Override
    public void disable() {
        this.coreManager.shutdown();

        Dialog.shutdown();
        if (Plugins.hasVault()) {
            VaultHook.shutdown();
        }
        PlayerBlockTracker.shutdown();
    }

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("nightcore", new String[]{"nightcore", "ncore"})
            .setConfigClass(CoreConfig.class)
            .setLangClass(CoreLang.class)
            .setPermissionsClass(CorePerms.class);
    }

    void addChildren(@NotNull NightCorePlugin child) {
        this.childrens.add(child);
        child.info("Powered by " + this.getName());
    }

    @NotNull
    public Set<NightCorePlugin> getChildrens() {
        return new HashSet<>(this.childrens);
    }

    private void testMethods() {
        if (Version.getCurrent() == Version.UNKNOWN) {
            this.warn("Server Version: UNSUPPORTED ✘");
        }
        else this.info("Server Version: " + Version.getCurrent().getLocalized() + " ✔");

        if (EntityUtil.setupEntityCounter(this)) {
            this.info("Entity Id Counter: OK ✔");
        }
        else this.error("Entity Id Counter: FAIL ✘");

        if (this.testItemNbt()) {
            this.info("Item NBT Compress: OK ✔");
        }
        else this.error("Item NBT Compress: FAIL ✘");

        if (Version.isAtLeast(Version.MC_1_20_6) && CoreConfig.DATA_FIXER_ENABLED.get()) {
            this.warn("=".repeat(10) + " WARNING " + "=".repeat(10));
            this.warn("Enabled Mojang's DataFixer for ItemStacks to update/fix NBT structure from <= 1.20.4 to 1.20.6+");
            this.warn("This may significally affect server's performance.");
            this.warn("Please, re-save all configuration(s) using in-game GUI editor(s) (if there is any).");
            this.warn("Then disable this 'feature' in the config.yml of nightcore.");
        }
    }

    private boolean testItemNbt() {
        if (!ItemNbt.setup(this)) return false;

        ItemStack testItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemUtil.editMeta(testItem, meta -> {
            meta.setDisplayName("Test Item");
            meta.setLore(Lists.newList("Test Lore 1", "Test Lore 2", "Test Lore 3"));
            meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
        });

        String nbt = ItemNbt.compress(testItem);
        if (nbt == null) return false;

        ItemStack decompressed = ItemNbt.decompress(nbt);
        return decompressed != null && decompressed.isSimilar(testItem);
    }
}
