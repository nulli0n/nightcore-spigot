package su.nightexpress.nightcore.manager;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

import java.io.File;

@Deprecated
public abstract class AbstractFileData<P extends NightCorePlugin> {

    protected final P    plugin;
    protected final File file;
    private final String id;

    public AbstractFileData(@NonNull P plugin, @NonNull String filePath) {
        this(plugin, new File(filePath));
    }

    public AbstractFileData(@NonNull P plugin, @NonNull File file) {
        this(plugin, file, FileConfig.getName(file));
    }

    public AbstractFileData(@NonNull P plugin, @NonNull String filePath, @NonNull String id) {
        this(plugin, new File(filePath), id);
    }

    public AbstractFileData(@NonNull P plugin, @NonNull File file, @NonNull String id) {
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

    protected abstract boolean onLoad(@NonNull FileConfig config);

    protected abstract void onSave(@NonNull FileConfig config);

    @NonNull
    public final File getFile() {
        return this.file;
    }

    @NonNull
    public final String getId() {
        return this.id;
    }

    @NonNull
    public final FileConfig getConfig() {
        return new FileConfig(this.getFile());
    }
}
