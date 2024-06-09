package su.nightexpress.nightcore;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.database.AbstractUserDataHandler;
import su.nightexpress.nightcore.database.AbstractUserManager;
import su.nightexpress.nightcore.database.DataUser;

public abstract class NightDataPlugin<U extends DataUser> extends NightPlugin {

    @Override
    protected void loadManagers() {
        super.loadManagers();
        this.getUserManager().loadOnlineUsers();
    }

    @Override
    protected void unloadManagers() {
        super.unloadManagers();
        this.getUserManager().shutdown();
        this.getData().shutdown();
    }

    @NotNull
    public abstract AbstractUserDataHandler<? extends NightDataPlugin<U>, U> getData();

    @NotNull
    public abstract AbstractUserManager<? extends NightDataPlugin<U>, U> getUserManager();
}
