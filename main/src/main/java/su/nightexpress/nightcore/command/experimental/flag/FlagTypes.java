package su.nightexpress.nightcore.command.experimental.flag;

import org.bukkit.World;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.builder.ContentFlagBuilder;
import su.nightexpress.nightcore.command.experimental.builder.SimpleFlagBuilder;

@Deprecated
public class FlagTypes {

    @NonNull
    public static SimpleFlagBuilder simple(@NonNull String name) {
        return SimpleFlag.builder(name);
    }

    @NonNull
    public static ContentFlagBuilder<World> world(@NonNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.WORLD).sample("world");
    }

    @NonNull
    public static ContentFlagBuilder<String> string(@NonNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.STRING).sample("foo");
    }

    @NonNull
    public static ContentFlagBuilder<Integer> integer(@NonNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.INTEGER).sample("0");
    }

    @NonNull
    public static ContentFlagBuilder<Double> decimal(@NonNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.DOUBLE).sample("0.0");
    }

    @NonNull
    public static ContentFlagBuilder<Boolean> bool(@NonNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.BOOLEAN).sample(Boolean.TRUE.toString());
    }
}
