package su.nightexpress.nightcore.bridge.paper.text;

import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;
import su.nightexpress.nightcore.util.Lists;

import java.util.Objects;

public class PaperObjectContantsAdapter extends PaperAdapter implements ObjectContentsAdapter<ObjectContents> {

    private static PaperObjectContantsAdapter instance;

    @NonNull
    public static PaperObjectContantsAdapter get() {
        if (instance == null) {
            instance = new PaperObjectContantsAdapter();
        }
        return instance;
    }

    @Override
    @NonNull
    public ObjectContents adaptContents(@NonNull NightObjectContents contents) {
        return contents.adapt(this);
    }

    @Override
    @NonNull
    public SpriteObjectContents adaptContents(@NonNull NightSpriteObjectContents contents) {
        return ObjectContents.sprite(this.adaptKey(contents.atlas()), this.adaptKey(contents.sprite()));
    }

    @Override
    @NonNull
    public PlayerHeadObjectContents adaptContents(@NonNull NightPlayerHeadObjectContents contents) {
        return ObjectContents.playerHead()
            .name(contents.name())
            .id(contents.id())
            .hat(contents.hat())
            .profileProperties(Lists.modify(contents.profileProperties(), this::adaptProfilePropery))
            .texture(contents.texture() == null ? null : this.adaptKey(Objects.requireNonNull(contents.texture())))
            .build();
    }

    private PlayerHeadObjectContents.@NonNull ProfileProperty adaptProfilePropery(NightPlayerHeadObjectContents.@NonNull NightProfileProperty property) {
        return PlayerHeadObjectContents.property(property.name(), property.value(), property.signature());
    }
}
