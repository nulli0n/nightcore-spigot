package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class FileUtil {

    public static void copy(@NotNull InputStream inputStream, @NotNull File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] array = new byte[1024];
            int read;
            while ((read = inputStream.read(array)) > 0) {
                outputStream.write(array, 0, read);
            }
            outputStream.close();
            inputStream.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean createFileIfNotExists(@NotNull Path path) {
        if (Files.exists(path)) return false;

        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return true;
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean create(@NotNull File file) {
        if (file.exists()) return false;

        File parent = file.getParentFile();
        if (parent == null) return false;

        parent.mkdirs();
        try {
            return file.createNewFile();
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @NotNull
    public static String getNameWithoutExtension(@NotNull Path path) {
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index == -1) return fileName;

        return fileName.substring(0, index);
    }

    @NotNull
    public static List<File> getConfigFiles(@NotNull String path) {
        return getConfigFiles(path, false);
    }

    @NotNull
    public static List<File> getConfigFiles(@NotNull String path, boolean deep) {
        return getFiles(path, FileConfig.EXTENSION, deep);
    }

    @NotNull
    public static List<File> getFiles(@NotNull String path) {
        return getFiles(path, false);
    }

    @NotNull
    public static List<File> getFiles(@NotNull String path, boolean deep) {
        return getFiles(path, null, deep);
    }

    @NotNull
    public static List<File> getFiles(@NotNull String path, @Nullable String extension, boolean deep) {
        List<File> files = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return files;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (extension == null || file.getName().endsWith(extension)) {
                    files.add(file);
                }
            }
            else if (file.isDirectory() && deep) {
                files.addAll(getFiles(file.getPath(), true));
            }
        }
        return files;
    }

    @NotNull
    public static List<Path> findFiles(@NotNull String directoryPath) {
        return findFiles(directoryPath, Files::isRegularFile);
    }

    @NotNull
    public static List<Path> findYamlFiles(@NotNull String directoryPath) {
        return findFiles(directoryPath, path -> Files.isRegularFile(path) && path.toString().endsWith(FileConfig.EXTENSION));
    }

    @NotNull
    public static List<Path> findFiles(@NotNull String directoryPath, @NotNull Predicate<Path> predicate) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) return Collections.emptyList();

        try (Stream<Path> stream = Files.list(path)) {
            return stream.filter(predicate).toList();
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }

    @NotNull
    public static List<File> getFolders(@NotNull String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return Collections.emptyList();

        return Stream.of(listOfFiles).filter(File::isDirectory).toList();
    }

    public static boolean deleteRecursive(@NotNull String path) {
        return deleteRecursive(new File(path));
    }

    public static boolean deleteRecursive(@NotNull File dir) {
        if (!dir.exists()) return false;

        File[] inside = dir.listFiles();
        if (inside != null) {
            for (File file : inside) {
                deleteRecursive(file);
            }
        }
        return dir.delete();
    }

    public static void extractResources(@NotNull File pluginFile, @NotNull String fromPath, @NotNull File destination) {
        if (!destination.exists()) {
            if (!destination.mkdirs()) {
                return;
            }
        }

        try {
            JarFile jar = new JarFile(pluginFile);
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String path = entry.getName();
                if (entry.isDirectory() || !path.startsWith(fromPath)) continue;

                File file = new File(destination, path.replaceFirst(fromPath, ""));
                if (file.exists()) continue;

                FileUtil.create(file);
                InputStream inputStream = jar.getInputStream(entry);
                FileOutputStream outputStream = new FileOutputStream(file);

                while (inputStream.available() > 0) {
                    outputStream.write(inputStream.read());
                }
                outputStream.close();
                inputStream.close();
            }

            jar.close();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
