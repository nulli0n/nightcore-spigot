package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Reflex {

    public static Class<?> getClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "." + name);
    }

    public static Class<?> getInnerClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "$" + name);
    }

    private static Class<?> getClass(@NotNull String path) {
        try {
            return Class.forName(path);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor<?> getConstructor(@NotNull Class<?> clazz, Class<?>... types) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor;
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeConstructor(@NotNull Constructor<?> constructor, Object... obj) {
        try {
            return constructor.newInstance(obj);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @NotNull
    public static <T> List<T> getFields(@NotNull Class<?> clazz, @NotNull Class<T> type) {
        List<T> list = new ArrayList<>();

        for (Field field : Reflex.getFields(clazz)) {
            if (!field.getDeclaringClass().equals(clazz)) continue;
            //if (!field.canAccess(null)) continue;
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!Modifier.isFinal(field.getModifiers())) continue;
            if (!type.isAssignableFrom(field.getType())) continue;
            if (!field.trySetAccessible()) continue;

            try {
                list.add(type.cast(field.get(null)));
            }
            catch (IllegalArgumentException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

        return list;
    }

    @NotNull
    public static List<Field> getFields(@NotNull Class<?> type) {
        List<Field> result = new ArrayList<>();

        Class<?> clazz = type;
        while (clazz != null && clazz != Object.class) {
            if (!result.isEmpty()) {
                result.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
            }
            else {
                Collections.addAll(result, clazz.getDeclaredFields());
            }
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    public static Field getField(@NotNull Class<?> clazz, @NotNull String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            return superClass == null ? null : getField(superClass, fieldName);
        }
    }

    public static Object getFieldValue(@NotNull Object from, @NotNull String fieldName) {
        try {
            Class<?> clazz = from instanceof Class<?> ? (Class<?>) from : from.getClass();
            Field field = getField(clazz, fieldName);
            if (field == null) return null;

            field.setAccessible(true);
            return field.get(from);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean setFieldValue(@NotNull Object of, @NotNull String fieldName, @Nullable Object value) {
        try {
            boolean isStatic = of instanceof Class;
            Class<?> clazz = isStatic ? (Class<?>) of : of.getClass();

            Field field = getField(clazz, fieldName);
            if (field == null) return false;

            field.setAccessible(true);
            field.set(isStatic ? null : of, value);
            return true;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Method getMethod(@NotNull Class<?> clazz, @NotNull String fieldName, @NotNull Class<?>... o) {
        try {
            return clazz.getDeclaredMethod(fieldName, o);
        }
        catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            return superClass == null ? null : getMethod(superClass, fieldName);
        }
    }

    public static Object invokeMethod(@NotNull Method method, @Nullable Object by, @Nullable Object... param) {
        method.setAccessible(true);
        try {
            return method.invoke(by, param);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
