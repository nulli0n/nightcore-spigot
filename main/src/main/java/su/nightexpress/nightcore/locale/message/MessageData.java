package su.nightexpress.nightcore.locale.message;

import org.bukkit.Sound;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.sound.AbstractSound;
import su.nightexpress.nightcore.util.sound.VanillaSound;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MessageData(@NonNull MessageType type, boolean usePrefix, boolean replacePlaceholders,
                          @Nullable NightSound sound, int[] titleTimes) {

    public static final MessageData CHAT_DEFAULT = chat().build();
    public static final MessageData CHAT_NO_PREFIX = chat().usePrefix(false).build();
    public static final MessageData ACTION_BAR = actionBar().build();
    public static final MessageData TITLES = titles().build();

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

    @NonNull
    public String serialize() {
        Map<Option, String> values = new HashMap<>();
        if (this.type == MessageType.CHAT) {
            if (!this.usePrefix) values.put(Option.PREFIX, Boolean.toString(false));
            if (this.replacePlaceholders) values.put(Option.PLACEHOLDERS, Boolean.toString(true));
        }

        if (this.sound != null) {
            values.put(Option.SOUND, this.sound.serialize());
        }
        if (this.titleTimes != null && this.titleTimes.length == 3 && this.type == MessageType.TITLE) {
            values.put(Option.TITLE_TIMES, IntStream.of(this.titleTimes).mapToObj(String::valueOf).collect(Collectors
                .joining(":")));
        }
        if (this.type != MessageType.CHAT) {
            values.put(Option.TYPE, this.type.name().toLowerCase());
        }
        if (values.isEmpty()) return "";

        String serialized = values.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .map(entry -> entry.getKey().name().toLowerCase() + "=\"" + entry.getValue() + "\"")
            .collect(Collectors.joining(","));

        //System.out.println("serialized = " + serialized);

        return OPEN_BRACKET + serialized + CLOSE_BRACKET;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    @NonNull
    public static Builder chat() {
        return builder().type(MessageType.CHAT);
    }

    @NonNull
    public static Builder actionBar() {
        return builder().type(MessageType.ACTION_BAR);
    }

    @NonNull
    public static Builder titles() {
        return builder().type(MessageType.TITLE).titleTimes(20, 60, 20);
    }

    @NonNull
    public static String extractAndParse(@NonNull String string, @NonNull Builder builder) {
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

    public static boolean hasLegacyData(@NonNull String string) {
        return string.startsWith("<sound:") || string.startsWith("<papi>") || string.startsWith("<noprefix>") || string
            .startsWith("<output:");
    }

    @NonNull
    public static String extractAndParseOld(@NonNull String string, @NonNull Builder builder) {
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
            else if (key.equalsIgnoreCase("sound")) {
                value = value.replace(':', ';').replace("minecraft;", "minecraft:");
            }
            else if (key.equalsIgnoreCase("output")) {
                if (value.startsWith("titles:")) {
                    parseOption(Option.TITLE_TIMES, value.substring("titles:".length()), builder);
                    parseOption(Option.TYPE, MessageType.TITLE.name(), builder);
                    matcher.appendReplacement(remainderBuffer, "");
                    continue;
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

    private static void parseOption(@NonNull Option option, @NonNull String value, @NonNull Builder builder) {
        switch (option) {
            case TYPE -> builder.type(Enums.parse(value, MessageType.class).orElse(MessageType.CHAT));
            case TITLE_TIMES -> {
                String[] split = value.split(":");
                int length = split.length;
                int fadeIn = NumberUtil.getAnyInteger(split[0], 20);
                int stay = length >= 2 ? NumberUtil.getAnyInteger(split[1], 60) : 60;
                int fadeOut = length >= 3 ? NumberUtil.getAnyInteger(split[2], 20) : fadeIn;
                builder.titleTimes(fadeIn, stay, fadeOut);
            }
            case SOUND -> builder.sound(AbstractSound.deserialize(value));
            case PREFIX -> builder.usePrefix(Boolean.parseBoolean(value));
            case PLACEHOLDERS -> builder.replacePlaceholders(Boolean.parseBoolean(value));
        }
    }

    @Override
    @NonNull
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

        @NonNull
        public MessageData build() {
            return new MessageData(this.type, this.usePrefix, this.replacePlaceholders, this.sound, this.titleTimes);
        }

        @NonNull
        public Builder type(@NonNull MessageType type) {
            this.type = type;
            return this;
        }

        @NonNull
        public Builder usePrefix(boolean usePrefix) {
            this.usePrefix = usePrefix;
            return this;
        }

        @NonNull
        public Builder replacePlaceholders(boolean replacePlaceholders) {
            this.replacePlaceholders = replacePlaceholders;
            return this;
        }

        @NonNull
        public Builder sound(@Nullable Sound sound) {
            return this.sound(sound == null ? null : VanillaSound.of(sound));
        }

        @NonNull
        public Builder sound(@Nullable NightSound sound) {
            this.sound = sound;
            return this;
        }

        @NonNull
        public Builder titleTimes(int fadeIn, int stay, int fadeOut) {
            this.titleTimes = new int[]{fadeIn, stay, fadeOut};
            return this;
        }
    }
}
