package su.nightexpress.nightcore.bridge.dialog.wrap.body;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogBodyAdapter;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public record WrappedPlainMessageDialogBody(@NotNull NightComponent contents, int width) implements WrappedDialogBody {

    public WrappedPlainMessageDialogBody(@NotNull NightComponent contents) {
        this(contents, 200);
    }

    public WrappedPlainMessageDialogBody(@NotNull NightComponent contents, int width) {
        this.contents = contents;
        this.width = Math.clamp(width, 1, 1024); // TODO Const
    }

    @Override
    @NotNull
    public <D> D adapt(@NotNull DialogBodyAdapter<D> adapter) {
        return adapter.adaptBody(this);
    }
}
