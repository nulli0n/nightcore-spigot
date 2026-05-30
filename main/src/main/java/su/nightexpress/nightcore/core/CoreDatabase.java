package su.nightexpress.nightcore.core;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.db.AbstractDatabaseManager;

public class CoreDatabase extends AbstractDatabaseManager<NightCore> {

    public CoreDatabase(@NonNull NightCore plugin) {
        super(plugin);
    }

    @Override
    protected void onClose() {

    }

    @Override
    protected void onInitialize() {

    }

    @Override
    public void onPurge() {

    }

    @Override
    public void onSynchronize() {

    }
}
