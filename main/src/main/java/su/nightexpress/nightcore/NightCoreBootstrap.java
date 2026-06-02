package su.nightexpress.nightcore;

import org.jspecify.annotations.NullMarked;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import su.nightexpress.nightcore.commands.ArgumentRegistry;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.configuration.codec.CodecRegistry;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class NightCoreBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        Arguments.init(new ArgumentRegistry());
        ConfigCodecs.init(new CodecRegistry());
    }
}
