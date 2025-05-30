package su.nightexpress.nightcore.language.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.bukkit.NightSound;

public class MessageOptions {

    private OutputType outputType;
    private boolean    hasPrefix;
    private boolean    usePlaceholderAPI;
    private NightSound sound;
    private int[]      titleTimes;

    public MessageOptions() {
        this.setOutputType(OutputType.CHAT);
        this.setHasPrefix(true);
    }

    @NotNull
    public MessageOptions copy() {
        MessageOptions copy = new MessageOptions();
        copy.outputType = this.outputType;
        copy.hasPrefix = this.hasPrefix;
        copy.usePlaceholderAPI = this.usePlaceholderAPI;
        copy.sound = this.sound;
        copy.titleTimes = this.titleTimes;
        return copy;
    }

    @NotNull
    public OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public boolean hasPrefix() {
        return hasPrefix;
    }

    public void setHasPrefix(boolean hasPrefix) {
        this.hasPrefix = hasPrefix;
    }

    public boolean usePlaceholderAPI() {
        return this.usePlaceholderAPI;
    }

    public void setUsePlaceholderAPI(boolean usePlaceholderAPI) {
        this.usePlaceholderAPI = usePlaceholderAPI;
    }

    @Nullable
    public NightSound getSound() {
        return this.sound;
    }

    public void setSound(@Nullable NightSound sound) {
        this.sound = sound;
    }

    public int[] getTitleTimes() {
        return titleTimes;
    }

    public void setTitleTimes(int[] titleTimes) {
        this.titleTimes = titleTimes;
    }
}
