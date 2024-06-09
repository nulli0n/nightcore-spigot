package su.nightexpress.nightcore.util.text;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public interface ComponentBuildable {

    @NotNull BaseComponent toComponent();
}
