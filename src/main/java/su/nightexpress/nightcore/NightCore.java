package su.nightexpress.nightcore;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.base.ReloadSubCommand;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CoreManager;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.command.CheckPermCommand;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;

import java.util.HashSet;
import java.util.Set;

public class NightCore extends NightPlugin {

    private final Set<NightCorePlugin> childrens;
    private final CoreManager          coreManager;

    public NightCore() {
        this.childrens = new HashSet<>();
        this.coreManager = new CoreManager(this);
    }

    @Override
    public void enable() {
        LangAssets.load();

        if (Plugins.hasVault()) {
            VaultHook.setup();
            this.getBaseCommand().addChildren(new CheckPermCommand(this));
        }
        this.getBaseCommand().addChildren(new ReloadSubCommand(this, CorePerms.COMMAND_RELOAD));

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
            this.warn("Server Version: UNSUPPORTED (!)");
        }
        else this.info("Server Version: " + Version.getCurrent().getLocalized() + " (OK)");

        if (EntityUtil.ENTITY_COUNTER == null) {
            this.warn("Entity Id Counter: NULL!");
        }
        else this.info("Entity Id Counter: OK.");

        if (ItemUtil.compress(new ItemStack(Material.DIAMOND_SWORD)) == null) {
            this.warn("Item NBT Compress: FAILED!");
        }
        else this.info("Item NBT Compress: OK.");
    }
}
