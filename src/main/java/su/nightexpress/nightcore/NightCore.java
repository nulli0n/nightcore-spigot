package su.nightexpress.nightcore;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CoreManager;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.command.CoreCommands;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;
import su.nightexpress.nightcore.util.text.tag.Tags;

import java.util.HashSet;
import java.util.Set;

public class NightCore extends NightPlugin implements ImprovedCommands {

    private final Set<NightPlugin> childrens;
    private final CoreManager      coreManager;

    public NightCore() {
        Plugins.load(this);
        Version.load(this);

        this.childrens = new HashSet<>();
        this.coreManager = new CoreManager(this);
    }

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("nightcore", new String[]{"nightcore", "ncore"})
            .setConfigClass(CoreConfig.class)
            .setLangClass(CoreLang.class)
            .setPermissionsClass(CorePerms.class);
    }

    @Override
    public void enable() {
        Tags.loadColorsFromFile(this);
        LangAssets.load(this);
        UIUtils.load(this);
        this.loadIntegrations();
        this.loadCommands();
        this.info("Time zone set as " + TimeUtil.getTimeZone().getID());

        this.coreManager.setup();
    }

    @Override
    public void disable() {
        this.coreManager.shutdown();

        if (Plugins.hasVault()) {
            VaultHook.shutdown();
        }
        UIUtils.clear();
        PlayerBlockTracker.shutdown();
        LangAssets.shutdown();
    }

    private void loadCommands() {
        CoreCommands.load(this);
    }

    private void loadIntegrations() {
        Plugins.detectPlugins();

        if (Plugins.hasVault()) {
            VaultHook.load(this);
        }
    }

    void addChildren(@NotNull NightPlugin child) {
        this.childrens.add(child);
        child.info("Powered by " + this.getName());
    }

    @NotNull
    public Set<NightPlugin> getChildrens() {
        return new HashSet<>(this.childrens);
    }
}
