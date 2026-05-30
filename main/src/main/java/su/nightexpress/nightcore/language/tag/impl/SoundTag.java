package su.nightexpress.nightcore.language.tag.impl;

import org.bukkit.Sound;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;
import su.nightexpress.nightcore.util.sound.AbstractSound;
import su.nightexpress.nightcore.util.sound.VanillaSound;

@Deprecated
public class SoundTag extends MessageTag {

    public SoundTag() {
        super("sound");
    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull Sound sound) {
        return this.wrap(sound);
    }

    @NonNull
    @Deprecated
    public String enclose(su.nightexpress.nightcore.util.bukkit.@NonNull NightSound sound) {
        return this.wrap(sound);
    }

    @NonNull
    @Deprecated
    public String wrap(su.nightexpress.nightcore.util.bukkit.@NonNull NightSound sound) {
        return this.wrap(sound.serialize());
    }

    @NonNull
    public String wrap(@NonNull Sound sound) {
        return this.wrap(VanillaSound.of(sound));
    }

    @NonNull
    public String wrap(@NonNull NightSound sound) {
        return this.wrap(sound.serialize());
    }

    @Override
    public void apply(@NonNull MessageOptions options, @Nullable String tagContent) {
        if (tagContent == null) return;

        options.setSound(AbstractSound.deserialize(tagContent));
    }
}
