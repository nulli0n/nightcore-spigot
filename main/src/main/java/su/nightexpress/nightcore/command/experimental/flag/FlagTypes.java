package su.nightexpress.nightcore.command.experimental.flag;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.builder.ContentFlagBuilder;
import su.nightexpress.nightcore.command.experimental.builder.SimpleFlagBuilder;

public class FlagTypes {

    @NotNull
    public static SimpleFlagBuilder simple(@NotNull String name) {
        return SimpleFlag.builder(name);
    }

    @NotNull
    public static ContentFlagBuilder<World> world(@NotNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.WORLD).sample("world");
    }

    @NotNull
    public static ContentFlagBuilder<String> string(@NotNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.STRING).sample("foo");
    }

    @NotNull
    public static ContentFlagBuilder<Integer> integer(@NotNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.INTEGER).sample("0");
    }

    @NotNull
    public static ContentFlagBuilder<Double> decimal(@NotNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.DOUBLE).sample("0.0");
    }

    @NotNull
    public static ContentFlagBuilder<Boolean> bool(@NotNull String name) {
        return ContentFlag.builder(name, ArgumentTypes.BOOLEAN).sample(Boolean.TRUE.toString());
    }
}
