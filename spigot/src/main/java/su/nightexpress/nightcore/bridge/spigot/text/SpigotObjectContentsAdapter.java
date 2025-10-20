package su.nightexpress.nightcore.bridge.spigot.text;

import net.md_5.bungee.api.chat.objects.ChatObject;
import net.md_5.bungee.api.chat.objects.PlayerObject;
import net.md_5.bungee.api.chat.objects.SpriteObject;
import net.md_5.bungee.api.chat.player.Profile;
import net.md_5.bungee.api.chat.player.Property;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;
import su.nightexpress.nightcore.util.Lists;

public class SpigotObjectContentsAdapter implements ObjectContentsAdapter<ChatObject> {

    private static SpigotObjectContentsAdapter instance;

    @NotNull
    public static SpigotObjectContentsAdapter get() {
        if (instance == null) {
            instance = new SpigotObjectContentsAdapter();
        }
        return instance;
    }

    @Override
    @NotNull
    public ChatObject adaptContents(@NotNull NightObjectContents contents) {
        return contents.adapt(this);
    }

    @Override
    @NotNull
    public ChatObject adaptContents(@NotNull NightSpriteObjectContents contents) {
        return new SpriteObject(contents.atlas().asString(), contents.sprite().value());
    }

    @Override
    @NotNull
    public ChatObject adaptContents(@NotNull NightPlayerHeadObjectContents contents) {
        Profile profile = new Profile(contents.name(), contents.id(), Lists.modify(contents.profileProperties(), this::adaptProfilePropery).toArray(new Property[0]));
        return new PlayerObject(profile, contents.hat());
    }

    @NotNull
    private Property adaptProfilePropery(@NotNull NightPlayerHeadObjectContents.NightProfileProperty property) {
        return new Property(property.name(), property.value(), property.signature());
    }
}
