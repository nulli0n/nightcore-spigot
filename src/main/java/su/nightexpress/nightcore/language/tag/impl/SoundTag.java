package su.nightexpress.nightcore.language.tag.impl;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.tag.MessageDecorator;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.tag.api.DynamicTag;

public class SoundTag extends DynamicTag implements MessageDecorator {

    public SoundTag() {
        super("sound");
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @NotNull
    public String enclose(@NotNull Sound sound) {
        return this.leading(null, sound.name().toLowerCase());
    }

    @Override
    public void apply(@NotNull MessageOptions options, @NotNull String value) {
        Sound sound = StringUtil.getEnum(value, Sound.class).orElse(null);
        options.setSound(sound);
    }
}
