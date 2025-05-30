package su.nightexpress.nightcore.language.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.language.message.MessageOptions;
import su.nightexpress.nightcore.language.message.OutputType;
import su.nightexpress.nightcore.language.tag.MessageTag;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;

public class OutputTag extends MessageTag {

    public OutputTag() {
        super("output");
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull OutputType type) {
        //String prefix = type.name().toLowerCase();

        return this.wrap(type);//this.enclose(prefix);
    }

    @NotNull
    @Deprecated
    public String enclose(int fade, int stay) {
//        String prefix = OutputType.TITLES.name().toLowerCase();
//        String content = prefix + ":" + fade + ":" + stay + ":" + fade;

        return this.wrap(fade, stay);//this.enclose(content);
    }

    @NotNull
    public String wrap(@NotNull OutputType type) {
        String prefix = type.name().toLowerCase();

        return this.wrap(prefix);
    }

    @NotNull
    public String wrap(int fade, int stay) {
        String prefix = OutputType.TITLES.name().toLowerCase();
        String content = prefix + ":" + fade + ":" + stay + ":" + fade;

        return this.wrap(content);
    }

    @Override
    public void apply(@NotNull MessageOptions options, @Nullable String tagContent) {
        if (tagContent == null) return;

        String[] split = tagContent.split(":");
        OutputType outputType = StringUtil.getEnum(split[0], OutputType.class).orElse(OutputType.CHAT);

        options.setOutputType(outputType);
        if (outputType == OutputType.TITLES) {
            int[] titleTimes = new int[3];
            if (split.length >= 4) {
                titleTimes[0] = NumberUtil.getIntegerAbs(split[1]);
                titleTimes[1] = NumberUtil.getAnyInteger(split[2], -1);
                titleTimes[2] = NumberUtil.getIntegerAbs(split[3]);
            }

            if (titleTimes[1] < 0) titleTimes[1] = Short.MAX_VALUE;

            options.setTitleTimes(titleTimes);
        }
    }
}
