package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;

public class Reflex {

    public static boolean classExists(@NonNull String path) {
        return findClass(path).isPresent();
    }

    @Nullable
    @Deprecated
    public static Class<?> getClass(@NonNull String path, @NonNull String name) {
        return getClass(path + "." + name);
    }

    @Nullable
    @Deprecated
    public static Class<?> getInnerClass(@NonNull String path, @NonNull String name) {
        return getClass(path + "$" + name);
    }

    @Nullable
    @Deprecated
    public static Class<?> getNMSClass(@NonNull String path, @NonNull String realName) {
        return getNMSClass(path, realName, null);
    }

    @Nullable
    @Deprecated
    public static Class<?> getNMSClass(@NonNull String path, @NonNull String realName, @Nullable String obfName) {
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
    private static Class<?> getClass(@NonNull String path) {
        return getClass(path, true);
    }

    @Deprecated
    private static Class<?> getClass(@NonNull String path, boolean printError) {
        try {
            return Class.forName(path);
        }
        catch (ClassNotFoundException exception) {
            if (printError) exception.printStackTrace();
            return null;
        }
    }

    @NonNull
    public static Class<?> safeClass(@NonNull String path, @NonNull String name, @NonNull String altName) {
        return findClass(path, name, altName).orElseThrow(() -> new IllegalStateException("Could not load classes: '" +
            name + "' and '" + altName + "' in '" + path + "'"));
    }

    @NonNull
    public static Class<?> safeClass(@NonNull String path, @NonNull String name) {
        return findClass(path, name).orElseThrow(() -> new IllegalStateException("Could not load class: '" + name +
            "' in '" + path + "'"));
    }

    @NonNull
    public static Class<?> safeInnerClass(@NonNull String path, @NonNull String name) {
        return findInnerClass(path, name).orElseThrow(() -> new IllegalStateException("Could not load inner class: '" +
            name + "' in '" + path + "'"));
    }

    @NonNull
    public static Class<?> safeClass(@NonNull String path) {
        return findClass(path).orElseThrow(() -> new IllegalStateException("Could not load class: '" + path + "'"));
    }

    @NonNull
    public static Optional<Class<?>> findClass(@NonNull String path, @NonNull String name, @NonNull String altName) {
        return findClass(path, name).or(() -> findClass(path, altName));
    }

    @NonNull
    public static Optional<Class<?>> findClass(@NonNull String path, @NonNull String name) {
        return findClass(path + "." + name);
    }

    @NonNull
    public static Optional<Class<?>> findInnerClass(@NonNull String path, @NonNull String name) {
        return findClass(path + "$" + name);
    }

    @NonNull
    public static Optional<Class<?>> findClass(@NonNull String path) {
        try {
            return Optional.of(Class.forName(path));
        }
        catch (ClassNotFoundException exception) {
            return Optional.empty();
        }
    }

    public static Constructor<?> getConstructor(@NonNull Class<?> source, Class<?>... types) {
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

    public static Object invokeConstructor(@NonNull Constructor<?> constructor, Object... obj) {
        try {
            return constructor.newInstance(obj);
        }
        catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static <T> List<T> getStaticFields(@NonNull Class<?> source, @NonNull Class<T> type, boolean includeParent) {
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

    @NonNull
    public static List<Field> getFields(@NonNull Class<?> source) {
        return getFields(source, true);
    }

    @NonNull
    public static List<Field> getFields(@NonNull Class<?> source, boolean includeParent) {
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

    public static Field getField(@NonNull Class<?> source, @NonNull String name) {
        try {
            return source.getDeclaredField(name);
        }
        catch (NoSuchFieldException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getField(superClass, name);
        }
    }

    public static Object getFieldValue(@NonNull Object source, @NonNull String realName, @NonNull String obfName) {
        Object byName = getFieldValue(source, realName);
        return byName == null ? getFieldValue(source, obfName) : byName;
    }

    public static Object getFieldValue(@NonNull Object source, @NonNull String name) {
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

    public static boolean setFieldValue(@NonNull Object source, @NonNull String name, @Nullable Object value) {
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
    public static Method getMethod(@NonNull Class<?> source, @NonNull String realName, @NonNull String obfName,
                                   @NonNull Class<?>... params) {
        Method byName = getMethod(source, realName, params);
        return byName == null ? getMethod(source, obfName, params) : byName;
    }

    @Deprecated
    public static Method getMethod(@NonNull Class<?> source, @NonNull String name, @NonNull Class<?>... params) {
        try {
            return source.getDeclaredMethod(name, params);
        }
        catch (NoSuchMethodException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getMethod(superClass, name);
        }
    }

    @NonNull
    public static Method safeMethod(@NonNull Class<?> source, @NonNull String name, @NonNull String altName,
                                    @NonNull Class<?>... params) {
        return findMethod(source, name, altName, params).orElseThrow(
            () -> new IllegalStateException("Could not find methods: '" + name + "' and '" + altName + "' in '" + source
                .getName() + "'"));
    }

    @NonNull
    public static Method safeMethod(@NonNull Class<?> source, @NonNull String name, @NonNull Class<?>... params) {
        return findMethod(source, name, params).orElseThrow(() -> new IllegalStateException("Could not find method: '" +
            name + "' in '" + source.getName() + "'"));
    }

    @NonNull
    public static Optional<Method> findMethod(@NonNull Class<?> source, @NonNull String name, @NonNull String altName,
                                              @NonNull Class<?>... params) {
        return findMethod(source, name, params).or(() -> findMethod(source, altName, params));
    }

    @NonNull
    public static Optional<Method> findMethod(@NonNull Class<?> source, @NonNull String name,
                                              @NonNull Class<?>... params) {
        try {
            return Optional.of(source.getDeclaredMethod(name, params));
        }
        catch (NoSuchMethodException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? Optional.empty() : findMethod(superClass, name);
        }
    }

    @NonNull
    public static Optional<Object> safeInvoke(@NonNull Method method, @Nullable Object by, @Nullable Object... param) {
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
    public static Object invokeMethod(@NonNull Method method, @Nullable Object by, @Nullable Object... param) {
        return safeInvoke(method, by, param).orElse(null);
    }
}
