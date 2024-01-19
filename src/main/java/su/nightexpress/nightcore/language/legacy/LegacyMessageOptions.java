package su.nightexpress.nightcore.language.legacy;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.OutputType;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.regex.TimedMatcher;

@Deprecated
public class LegacyMessageOptions {

    private OutputType type;
    private boolean    hasPrefix;
    private boolean    papi;
    private Sound      sound;
    private int[]      titleTimes;

    public LegacyMessageOptions() {
        this.type = OutputType.CHAT;
        this.hasPrefix = true;
    }

    @NotNull
    public LegacyMessageOptions copy() {
        LegacyMessageOptions copy = new LegacyMessageOptions();
        copy.type = this.type;
        copy.hasPrefix = this.hasPrefix;
        copy.papi = this.papi;
        copy.sound = this.sound;
        copy.titleTimes = this.titleTimes;
        return copy;
    }

    public void read(@NotNull String matchOptions) {
        for (LegacyMessageOption option : LegacyMessageOption.values()) {
            TimedMatcher matcherParam = TimedMatcher.create(option.getPattern(), matchOptions);
            if (!matcherParam.find()) continue;

            String optionValue = matcherParam.getMatcher().group(1).stripLeading();
            switch (option) {
                case TYPE -> {
                    String[] split = optionValue.split(":");
                    this.type = StringUtil.getEnum(split[0], OutputType.class).orElse(OutputType.CHAT);
                    if (this.type == OutputType.TITLES) {
                        this.titleTimes = new int[3];
                        if (split.length >= 4) {
                            this.titleTimes[0] = NumberUtil.getInteger(split[1]);
                            this.titleTimes[1] = NumberUtil.getAnyInteger(split[2], -1);
                            this.titleTimes[2] = NumberUtil.getInteger(split[3]);
                        }

                        if (this.titleTimes[1] < 0) this.titleTimes[1] = Short.MAX_VALUE;
                    }
                }
                case PREFIX -> this.hasPrefix = Boolean.parseBoolean(optionValue);
                case PAPI -> this.papi = Boolean.parseBoolean(optionValue) && Plugins.hasPlaceholderAPI();
                case SOUND -> this.sound = StringUtil.getEnum(optionValue, Sound.class).orElse(null);
            }
        }
    }

    @NotNull
    public OutputType getType() {
        return type;
    }

    public boolean hasPrefix() {
        return this.hasPrefix;
    }

    public boolean hasPlaceholderAPI() {
        return this.papi;
    }

    @Nullable
    public Sound getSound() {
        return sound;
    }

    public int[] getTitleTimes() {
        return titleTimes;
    }
}
