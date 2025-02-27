package su.nightexpress.nightcore;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

@SuppressWarnings("UnstableApiUsage")
public class NightCoreLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("maven", "default", "https://repo1.maven.org/maven2/").build());

        resolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:6.0.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("it.unimi.dsi:fastutil-core:8.5.13"), null));

        classpathBuilder.addLibrary(resolver);
    }
}
