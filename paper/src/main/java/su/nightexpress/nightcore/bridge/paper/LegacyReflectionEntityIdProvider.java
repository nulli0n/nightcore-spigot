package su.nightexpress.nightcore.bridge.paper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class LegacyReflectionEntityIdProvider implements UnsafeEntityIdProvider {

    // Left for future refactor

    private final MethodHandle nextEntityIdHandle;

    @SuppressWarnings("deprecation")
    public LegacyReflectionEntityIdProvider() {
        Object unsafeValues = Bukkit.getUnsafe();

        try {
            Class<?> targetClass = unsafeValues.getClass();
            Method method = targetClass.getMethod("nextEntityId");
            method.setAccessible(true);

            this.nextEntityIdHandle = MethodHandles.lookup().unreflect(method).bindTo(unsafeValues);
        }
        catch (NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to initialize legacy reflection for nextEntityId().", e);
        }
    }

    @Override
    public int getNextEntityId(@Nullable World world) {
        try {
            return (int) this.nextEntityIdHandle.invoke();
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to invoke legacy nextEntityId via reflection.", e);
        }
    }
}
