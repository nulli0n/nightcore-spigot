package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;

public class Reflex {

    public static boolean classExists(@NotNull String path) {
        return findClass(path).isPresent();
    }

    @Nullable
    @Deprecated
    public static Class<?> getClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "." + name);
    }

    @Nullable
    @Deprecated
    public static Class<?> getInnerClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "$" + name);
    }

    @Nullable
    @Deprecated
    public static Class<?> getNMSClass(@NotNull String path, @NotNull String realName) {
        return getNMSClass(path, realName, null);
    }

    @Nullable
    @Deprecated
    public static Class<?> getNMSClass(@NotNull String path, @NotNull String realName, @Nullable String obfName) {
        Class<?> byRealName = getClass(path + "." + realName, false);
        if (byRealName != null) {
            return byRealName;
        }

        if (obfName != null) {
            return getClass(path + "." + obfName, false);
        }

        return null;
    }

    @Deprecated
    private static Class<?> getClass(@NotNull String path) {
        return getClass(path, true);
    }

    @Deprecated
    private static Class<?> getClass(@NotNull String path, boolean printError) {
        try {
            return Class.forName(path);
        }
        catch (ClassNotFoundException exception) {
            if (printError) exception.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static Class<?> safeClass(@NotNull String path, @NotNull String name, @NotNull String altName) {
        return findClass(path, name, altName).orElseThrow(() -> new IllegalStateException("Could not load classes: '" + name + "' and '" + altName + "' in '" + path + "'"));
    }

    @NotNull
    public static Class<?> safeClass(@NotNull String path, @NotNull String name) {
        return findClass(path, name).orElseThrow(() -> new IllegalStateException("Could not load class: '" + name + "' in '" + path + "'"));
    }

    @NotNull
    public static Class<?> safeInnerClass(@NotNull String path, @NotNull String name) {
        return findInnerClass(path, name).orElseThrow(() -> new IllegalStateException("Could not load inner class: '" + name + "' in '" + path + "'"));
    }

    @NotNull
    public static Class<?> safeClass(@NotNull String path) {
        return findClass(path).orElseThrow(() -> new IllegalStateException("Could not load class: '" + path + "'"));
    }

    @NotNull
    public static Optional<Class<?>> findClass(@NotNull String path, @NotNull String name, @NotNull String altName) {
        return findClass(path, name).or(() -> findClass(path, altName));
    }

    @NotNull
    public static Optional<Class<?>> findClass(@NotNull String path, @NotNull String name) {
        return findClass(path + "." + name);
    }

    @NotNull
    public static Optional<Class<?>> findInnerClass(@NotNull String path, @NotNull String name) {
        return findClass(path + "$" + name);
    }

    @NotNull
    public static Optional<Class<?>> findClass(@NotNull String path) {
        try {
            return Optional.of(Class.forName(path));
        }
        catch (ClassNotFoundException exception) {
            return Optional.empty();
        }
    }

    public static Constructor<?> getConstructor(@NotNull Class<?> source, Class<?>... types) {
        try {
            Constructor<?> constructor = source.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor;
        }
        catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Object invokeConstructor(@NotNull Constructor<?> constructor, Object... obj) {
        try {
            return constructor.newInstance(obj);
        }
        catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static <T> List<T> getStaticFields(@NotNull Class<?> source, @NotNull Class<T> type, boolean includeParent) {
        List<T> list = new ArrayList<>();

        for (Field field : Reflex.getFields(source, includeParent)) {
            //if (!field.getDeclaringClass().equals(source)) continue;
            //if (!field.canAccess(null)) continue;
            if (!Modifier.isStatic(field.getModifiers())) continue;
            //if (!Modifier.isFinal(field.getModifiers())) continue;
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
    public static List<Field> getFields(@NotNull Class<?> source) {
        return getFields(source, true);
    }

    @NotNull
    public static List<Field> getFields(@NotNull Class<?> source, boolean includeParent) {
        List<Field> result = new ArrayList<>();

        Class<?> lookupClass = source;
        while (lookupClass != null && lookupClass != Object.class) {
            if (!result.isEmpty()) {
                result.addAll(0, Arrays.asList(lookupClass.getDeclaredFields()));
            }
            else {
                Collections.addAll(result, lookupClass.getDeclaredFields());
            }
            if (!includeParent) {
                break;
            }
            lookupClass = lookupClass.getSuperclass();
        }

        return result;
    }

    public static Field getField(@NotNull Class<?> source, @NotNull String name) {
        try {
            return source.getDeclaredField(name);
        }
        catch (NoSuchFieldException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getField(superClass, name);
        }
    }

    public static Object getFieldValue(@NotNull Object source, @NotNull String realName, @NotNull String obfName) {
        Object byName = getFieldValue(source, realName);
        return byName == null ? getFieldValue(source, obfName) : byName;
    }

    public static Object getFieldValue(@NotNull Object source, @NotNull String name) {
        try {
            Class<?> clazz = source instanceof Class<?> ? (Class<?>) source : source.getClass();
            Field field = getField(clazz, name);
            if (field == null) return null;

            field.setAccessible(true);
            return field.get(source);
        }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static boolean setFieldValue(@NotNull Object source, @NotNull String name, @Nullable Object value) {
        try {
            boolean isStatic = source instanceof Class;
            Class<?> clazz = isStatic ? (Class<?>) source : source.getClass();

            Field field = getField(clazz, name);
            if (field == null) return false;

            field.setAccessible(true);
            field.set(isStatic ? null : source, value);
            return true;
        }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Deprecated
    public static Method getMethod(@NotNull Class<?> source, @NotNull String realName, @NotNull String obfName, @NotNull Class<?>... params) {
        Method byName = getMethod(source, realName, params);
        return byName == null ? getMethod(source, obfName, params) : byName;
    }

    @Deprecated
    public static Method getMethod(@NotNull Class<?> source, @NotNull String name, @NotNull Class<?>... params) {
        try {
            return source.getDeclaredMethod(name, params);
        }
        catch (NoSuchMethodException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getMethod(superClass, name);
        }
    }

    @NotNull
    public static Method safeMethod(@NotNull Class<?> source, @NotNull String name, @NotNull String altName, @NotNull Class<?>... params) {
        return findMethod(source, name, altName, params).orElseThrow(() -> new IllegalStateException("Could not find methods: '" + name + "' and '" + altName + "' in '" + source.getName() + "'"));
    }

    @NotNull
    public static Method safeMethod(@NotNull Class<?> source, @NotNull String name, @NotNull Class<?>... params) {
        return findMethod(source, name, params).orElseThrow(() -> new IllegalStateException("Could not find method: '" + name + "' in '" + source.getName() + "'"));
    }

    @NotNull
    public static Optional<Method> findMethod(@NotNull Class<?> source, @NotNull String name, @NotNull String altName, @NotNull Class<?>... params) {
        return findMethod(source, name, params).or(() -> findMethod(source, altName, params));
    }

    @NotNull
    public static Optional<Method> findMethod(@NotNull Class<?> source, @NotNull String name, @NotNull Class<?>... params) {
        try {
            return Optional.of(source.getDeclaredMethod(name, params));
        }
        catch (NoSuchMethodException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? Optional.empty() : findMethod(superClass, name);
        }
    }

    @NotNull
    public static Optional<Object> safeInvoke(@NotNull Method method, @Nullable Object by, @Nullable Object... param) {
        try {
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(by, param));
        }
        catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    @Nullable
    public static Object invokeMethod(@NotNull Method method, @Nullable Object by, @Nullable Object... param) {
        return safeInvoke(method, by, param).orElse(null);
    }
}
