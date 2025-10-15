package su.nightexpress.nightcore.bridge.paper.text;

import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;
import su.nightexpress.nightcore.util.Lists;

import java.util.Objects;

public class PaperObjectContantsAdapter extends PaperAdapter implements ObjectContentsAdapter<ObjectContents> {

    private static PaperObjectContantsAdapter instance;

    @NotNull
    public static PaperObjectContantsAdapter get() {
        if (instance == null) {
            instance = new PaperObjectContantsAdapter();
        }
        return instance;
    }

    @Override
    @NotNull
    public ObjectContents adaptContents(@NotNull NightObjectContents contents) {
        return contents.adapt(this);
    }

    @Override
    @NotNull
    public SpriteObjectContents adaptContents(@NotNull NightSpriteObjectContents contents) {
        return ObjectContents.sprite(this.adaptKey(contents.atlas()), this.adaptKey(contents.sprite()));
    }

    @Override
    @NotNull
    public PlayerHeadObjectContents adaptContents(@NotNull NightPlayerHeadObjectContents contents) {
        return ObjectContents.playerHead()
            .name(contents.name())
            .id(contents.id())
            .hat(contents.hat())
            .profileProperties(Lists.modify(contents.profileProperties(), this::adaptProfilePropery))
            .texture(contents.texture() == null ? null : this.adaptKey(Objects.requireNonNull(contents.texture())))
            .build();
    }

    @NotNull
    private PlayerHeadObjectContents.ProfileProperty adaptProfilePropery(@NotNull NightPlayerHeadObjectContents.NightProfileProperty property) {
        return PlayerHeadObjectContents.property(property.name(), property.value(), property.signature());
    }
}
