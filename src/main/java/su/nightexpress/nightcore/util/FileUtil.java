package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        catch (IOException ex) {
            ex.printStackTrace();
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
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @NotNull
    public static List<File> getFiles(@NotNull String path, boolean deep) {
        List<File> files = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return files;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                files.add(file);
            }
            else if (file.isDirectory() && deep) {
                files.addAll(getFiles(file.getPath(), true));
            }
        }
        return files;
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
}
