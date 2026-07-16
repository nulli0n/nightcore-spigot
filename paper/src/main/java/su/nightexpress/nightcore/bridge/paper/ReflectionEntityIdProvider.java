package su.nightexpress.nightcore.bridge.paper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ReflectionEntityIdProvider implements UnsafeEntityIdProvider {

    private final MethodHandle handle;
    private final boolean      takesWorld;

    @SuppressWarnings("deprecation")
    public ReflectionEntityIdProvider() {
        Object unsafe = Bukkit.getUnsafe();
        Method foundMethod;
        boolean isModern;

        // Try to find the modern method first
        try {
            foundMethod = unsafe.getClass().getMethod("nextEntityId", World.class);
            isModern = true;
        }
        catch (NoSuchMethodException e) {
            // Fallback to legacy
            try {
                foundMethod = unsafe.getClass().getMethod("nextEntityId");
                isModern = false;
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalStateException("Could not find nextEntityId in UnsafeValues", ex);
            }
        }

        try {
            foundMethod.setAccessible(true);
            this.handle = MethodHandles.lookup().unreflect(foundMethod).bindTo(unsafe);
            this.takesWorld = isModern;
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access nextEntityId", e);
        }
    }

    @Override
    public int getNextEntityId(World world) {
        try {
            return takesWorld ? (int) handle.invoke(world) : (int) handle.invoke();
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw new IllegalStateException("Error invoking nextEntityId", t);
        }
    }
}
