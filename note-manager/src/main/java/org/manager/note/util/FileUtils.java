package org.manager.note.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String readFileContent(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    public static String readFileContent(File file) throws IOException {
        return Files.readString(file.toPath());
    }
}
