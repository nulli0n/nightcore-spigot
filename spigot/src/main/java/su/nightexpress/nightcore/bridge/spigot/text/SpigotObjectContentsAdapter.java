package su.nightexpress.nightcore.bridge.spigot.text;

import net.md_5.bungee.api.chat.objects.ChatObject;
import net.md_5.bungee.api.chat.objects.PlayerObject;
import net.md_5.bungee.api.chat.objects.SpriteObject;
import net.md_5.bungee.api.chat.player.Profile;
import net.md_5.bungee.api.chat.player.Property;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;
import su.nightexpress.nightcore.util.Lists;

public class SpigotObjectContentsAdapter implements ObjectContentsAdapter<ChatObject> {

    private static SpigotObjectContentsAdapter instance;

    @NonNull
    public static SpigotObjectContentsAdapter get() {
        if (instance == null) {
            instance = new SpigotObjectContentsAdapter();
        }
        return instance;
    }

    @Override
    @NonNull
    public ChatObject adaptContents(@NonNull NightObjectContents contents) {
        return contents.adapt(this);
    }

    @Override
    @NonNull
    public ChatObject adaptContents(@NonNull NightSpriteObjectContents contents) {
        return new SpriteObject(contents.atlas().asString(), contents.sprite().value());
    }

    @Override
    @NonNull
    public ChatObject adaptContents(@NonNull NightPlayerHeadObjectContents contents) {
        Profile profile = new Profile(contents.name(), contents.id(), Lists.modify(contents.profileProperties(),
            this::adaptProfilePropery).toArray(new Property[0]));
        return new PlayerObject(profile, contents.hat());
    }

    @NonNull
    private Property adaptProfilePropery(NightPlayerHeadObjectContents.@NonNull NightProfileProperty property) {
        return new Property(property.name(), property.value(), property.signature());
    }
}
