package su.nightexpress.nightcore.commands;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;

@FunctionalInterface
@NullMarked
public interface CommandProvider {

    void provideCommands(HubNodeBuilder root);
}
