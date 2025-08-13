package su.nightexpress.nightcore.locale.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.sound.AbstractSound;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MessageData(@NotNull MessageType type, boolean usePrefix, boolean replacePlaceholders, @Nullable NightSound sound, int[] titleTimes) {

    private static final Pattern ENTRY_PATTERN = Pattern.compile("(\\w+)=\"([^\"]*)\"");
    private static final Pattern LEGACY_TAG_PATTERN = Pattern.compile("<(\\w+)(?:\\s*:\\s*\"([^\"]*)\")?>");

    private static final String OPEN_BRACKET = "[";
    private static final String CLOSE_BRACKET = "]";

    public enum Option {
        PREFIX,
        SOUND,
        PLACEHOLDERS,
        TITLE_TIMES,
        TYPE
    }

    @NotNull
    public String serialize() {
        Map<Option, String> values = new HashMap<>();
        values.put(Option.PREFIX, Boolean.toString(this.usePrefix));
        values.put(Option.PLACEHOLDERS, Boolean.toString(this.replacePlaceholders));

        if (this.sound != null) {
            values.put(Option.SOUND, this.sound.serialize());
        }
        if (this.titleTimes != null && this.titleTimes.length == 3) {
            values.put(Option.TITLE_TIMES, IntStream.of(this.titleTimes).mapToObj(String::valueOf).collect(Collectors.joining(":")));
        }
        if (this.type != MessageType.CHAT) {
            values.put(Option.TYPE, this.type.name().toLowerCase());
        }

        String serialized = values.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .map(entry -> entry.getKey().name().toLowerCase() + "=\"" + entry.getValue() + "\"")
            .collect(Collectors.joining(","));

        //System.out.println("serialized = " + serialized);

        return OPEN_BRACKET + serialized + CLOSE_BRACKET;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public static String extractAndParse(@NotNull String string, @NotNull Builder builder) {
        if (!string.startsWith(OPEN_BRACKET)) return string;

        int endIndex = string.indexOf(CLOSE_BRACKET);
        if (endIndex < 0) return string;

        String paramsText = string.substring(1, endIndex);
        String text = string.substring(endIndex + 1);

        Matcher matcher = ENTRY_PATTERN.matcher(paramsText);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            Option option = Enums.get(key, Option.class);
            if (option == null) continue;

            parseOption(option, value, builder);
        }

        return text.trim();
    }

    public static boolean hasLegacyData(@NotNull String string) {
        return string.startsWith("<sound:") || string.startsWith("<papi>") || string.startsWith("<noprefix>") || string.startsWith("<output:");
    }

    @NotNull
    public static String extractAndParseOld(@NotNull String string, @NotNull Builder builder) {
        Matcher matcher = LEGACY_TAG_PATTERN.matcher(string);
        StringBuilder remainderBuffer = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            if (key.equalsIgnoreCase("papi")) {
                key = Option.PLACEHOLDERS.name();
                value = "true";
            }
            else if (key.equalsIgnoreCase("noprefix")) {
                key = Option.PREFIX.name();
                value = "false";
            }
            else if (key.equalsIgnoreCase("output")) {
                if (value.startsWith("titles:")) {
                    parseOption(Option.TITLE_TIMES, value.substring("titles:".length()), builder);
                    parseOption(Option.TYPE, MessageType.TITLE.name(), builder);
                }
                else {
                    key = Option.TYPE.name();
                    value = value.replace("none", MessageType.SILENT.name());
                }
            }

            Option option = Enums.get(key, Option.class);
            if (option != null) {
                parseOption(option, value, builder);
                // Remove the tag completely from output
                matcher.appendReplacement(remainderBuffer, "");
            }
        }
        matcher.appendTail(remainderBuffer);

        // Trim leftover spaces if needed
        return remainderBuffer.toString().trim();
    }

    private static void parseOption(@NotNull Option option, @NotNull String value, @NotNull Builder builder) {
        switch (option) {
            case TYPE -> builder.type(Enums.parse(value, MessageType.class).orElse(MessageType.CHAT));
            case TITLE_TIMES -> {
                String[] split = value.split(":");
                int fadeIn = NumberUtil.getAnyInteger(split[1], 20);
                int stay = NumberUtil.getAnyInteger(split[2], 60);
                int fadeOut = NumberUtil.getAnyInteger(split[3], 20);
                builder.titleTimes(fadeIn, stay, fadeOut);
            }
            case SOUND -> builder.sound(AbstractSound.deserialize(value));
            case PREFIX -> builder.usePrefix(Boolean.parseBoolean(value));
            case PLACEHOLDERS -> builder.replacePlaceholders(Boolean.parseBoolean(value));
        }
    }

    @Override
    @NotNull
    public String toString() {
        return "MessageData{" +
            //"text='" + text + '\'' +
            ", type=" + type +
            ", usePrefix=" + usePrefix +
            ", replacePlaceholders=" + replacePlaceholders +
            ", sound=" + sound +
            ", titleTimes=" + Arrays.toString(titleTimes) +
            '}';
    }

    public static class Builder {

        private MessageType type;
        private boolean     usePrefix;
        private boolean     replacePlaceholders;
        private NightSound  sound;
        private int[]       titleTimes;

        public Builder() {
            this.type = MessageType.CHAT;
            this.usePrefix = true;
            this.replacePlaceholders = false;
        }

        @NotNull
        public MessageData build() {
            return new MessageData(this.type, this.usePrefix, this.replacePlaceholders, this.sound, this.titleTimes);
        }

        @NotNull
        public Builder type(@NotNull MessageType type) {
            this.type = type;
            return this;
        }

        @NotNull
        public Builder usePrefix(boolean usePrefix) {
            this.usePrefix = usePrefix;
            return this;
        }

        @NotNull
        public Builder replacePlaceholders(boolean replacePlaceholders) {
            this.replacePlaceholders = replacePlaceholders;
            return this;
        }

        @NotNull
        public Builder sound(@Nullable NightSound sound) {
            this.sound = sound;
            return this;
        }

        @NotNull
        public Builder titleTimes(int fadeIn, int stay, int fadeOut) {
            this.titleTimes = new int[]{fadeIn, stay, fadeOut};
            return this;
        }
    }
}
