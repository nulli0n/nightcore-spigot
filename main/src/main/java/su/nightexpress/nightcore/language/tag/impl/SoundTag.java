package su.nightexpress.nightcore.language.tag.impl;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;
import su.nightexpress.nightcore.util.bukkit.NightSound;

public class SoundTag extends MessageTag {

    public SoundTag() {
        super("sound");
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull Sound sound) {
        return this.wrap(sound);//this.enclose(NightSound.of(sound, 0.6F));
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull NightSound sound) {
        return this.wrap(sound);//this.enclose(sound.serialize());
    }

    @NotNull
    public String wrap(@NotNull Sound sound) {
        return this.enclose(NightSound.of(sound, 0.6F));
    }

    @NotNull
    public String wrap(@NotNull NightSound sound) {
        return this.wrap(sound.serialize());
    }

    @Override
    public void apply(@NotNull MessageOptions options, @Nullable String tagContent) {
        if (tagContent == null) return;

        options.setSound(NightSound.deserialize(tagContent));
    }
}
