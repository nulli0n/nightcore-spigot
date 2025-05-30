package su.nightexpress.nightcore;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.core.CoreManager;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.command.CoreCommands;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;
import su.nightexpress.nightcore.util.text.tag.Tags;

public class NightCore extends NightPlugin implements ImprovedCommands {

    private CoreManager coreManager;

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
        this.loadCommands();
        this.info("Time zone set as " + TimeUtil.getTimeZone().getID());

        this.coreManager = new CoreManager(this);
        this.coreManager.setup();
    }

    @Override
    public void disable() {
        if (this.coreManager != null) this.coreManager.shutdown();

        UIUtils.clear();
        LangAssets.shutdown();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        PlayerBlockTracker.shutdown();
        Engine.clear();
    }

    private void loadCommands() {
        CoreCommands.load(this);
    }
}
