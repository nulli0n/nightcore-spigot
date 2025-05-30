package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

import java.io.File;

public abstract class AbstractFileData<P extends NightCorePlugin> {

    protected final P      plugin;
    protected final File   file;
    private final   String id;

    public AbstractFileData(@NotNull P plugin, @NotNull String filePath) {
        this(plugin, new File(filePath));
    }

    public AbstractFileData(@NotNull P plugin, @NotNull File file) {
        this(plugin, file, FileConfig.getName(file));
    }

    public AbstractFileData(@NotNull P plugin, @NotNull String filePath, @NotNull String id) {
        this(plugin, new File(filePath), id);
    }

    public AbstractFileData(@NotNull P plugin, @NotNull File file, @NotNull String id) {
        this.plugin = plugin;
        this.file = file;
        this.id = StringUtil.lowerCaseUnderscore(id);
    }

    public final boolean load() {
        FileConfig config = this.getConfig();
        if (!this.onLoad(config)) return false;

        config.saveChanges();
        return true;
    }

    public final void save() {
        FileConfig config = this.getConfig();
        this.onSave(config);
        config.saveChanges();
    }

    protected abstract boolean onLoad(@NotNull FileConfig config);

    protected abstract void onSave(@NotNull FileConfig config);

    @NotNull
    public final File getFile() {
        return this.file;
    }

    @NotNull
    public final String getId() {
        return this.id;
    }

    @NotNull
    public final FileConfig getConfig() {
        return new FileConfig(this.getFile());
    }
}
