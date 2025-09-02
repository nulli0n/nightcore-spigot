package su.nightexpress.nightcore;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.ImprovedCommands;
import su.nightexpress.nightcore.config.PluginDetails;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreManager;
import su.nightexpress.nightcore.core.CorePerms;
import su.nightexpress.nightcore.core.command.CoreCommands;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.dialog.DialogWatcher;
import su.nightexpress.nightcore.util.TimeUtil;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.blocktracker.PlayerBlockTracker;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;
import su.nightexpress.nightcore.core.tag.TagManager;

import java.util.Optional;

public class NightCore extends NightPlugin implements ImprovedCommands {

    private TagManager    tagManager;
    private CoreManager   coreManager;
    private DialogWatcher dialogWatcher;

    @Override
    @NotNull
    protected PluginDetails getDefaultDetails() {
        return PluginDetails.create("nightcore", new String[]{"nightcore", "ncore"})
            .setConfigClass(CoreConfig.class)
            //.setLangClass(CoreLang.class)
            .setPermissionsClass(CorePerms.class);
    }

    @Override
    protected void addRegistries() {
        this.registerLang(CoreLang.class);
    }

    @Override
    public void enable() {
        LangAssets.load(this);
        UIUtils.load(this);
        this.loadCommands();
        this.info("Time zone set as " + TimeUtil.getTimeZone().getID());

        this.tagManager = new TagManager(this);
        this.tagManager.setup();

        this.coreManager = new CoreManager(this);
        this.coreManager.setup();

        if (Version.isAtLeast(Version.MC_1_21_7)) {
            this.dialogWatcher = new DialogWatcher(this);
            this.dialogWatcher.setup();
        }
    }

    @Override
    public void disable() {
        if (this.dialogWatcher != null) this.dialogWatcher.shutdown();
        if (this.coreManager != null) this.coreManager.shutdown();
        if (this.tagManager != null) this.tagManager.shutdown();

        UIUtils.clear();
        LangAssets.shutdown();
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
        PlayerProfiles.clear();
        PlayerBlockTracker.shutdown();
        Engine.clear();
    }

    private void loadCommands() {
        CoreCommands.load(this);
    }

    @NotNull
    public Optional<DialogWatcher> getDialogWatcher() {
        return Optional.ofNullable(this.dialogWatcher);
    }

    @NotNull
    public TagManager getTagManager() {
        return this.tagManager;
    }
}
