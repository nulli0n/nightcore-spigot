package su.nightexpress.nightcore.language.tag.impl;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageTag;
import su.nightexpress.nightcore.util.BukkitThing;

public class SoundTag extends MessageTag {

    public SoundTag() {
        super("sound");
    }

    @NotNull
    public String enclose(@NotNull Sound sound) {
        return this.enclose(BukkitThing.toString(sound));
    }

    @Override
    public void apply(@NotNull MessageOptions options, @Nullable String tagContent) {
        if (tagContent == null) return;

        options.setSound(BukkitThing.getSound(tagContent));
    }
}
