package su.nightexpress.nightcore.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.profile.CachedProfile;
import su.nightexpress.nightcore.util.profile.PlayerProfiles;

public class CoreListener extends AbstractListener<NightCore> {

    public CoreListener(@NotNull NightCore plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!CoreConfig.PROFILE_UPDATE_ON_JOIN.get()) return;

        Player player = event.getPlayer();
        CachedProfile profile = PlayerProfiles.getProfile(player);
        if (Version.isSpigot()) {
            profile.update(); // "If the player is online, the returned profile will be complete." yeah yeah sure
        }
        else {
            profile.update(Software.get().getProfile(player)); // Thanks paper for ACTUALLY complete profiles of online players.
        }
    }
}
