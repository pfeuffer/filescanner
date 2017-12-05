package de.pfeufferweb.filewatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

public class FileInfo {
    private final String name;
    private final FileTime fileTime;

    private FileInfo(String name, FileTime fileTime) {
        this.name = name;
        this.fileTime = fileTime;
    }

    public static FileInfo buildFrom(Path file) {
        return new FileInfo(file.getFileName().toString(), lastModifiedTime(file));
    }

    private static FileTime lastModifiedTime(Path file) {
        try {
            return Files.getLastModifiedTime(file);
        } catch (IOException e) {
            throw new RuntimeException("could not read las modified time for " + file, e);
        }
    }

    @Override
    public String toString() {
        return name + " / " + fileTime.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(name, fileInfo.name) &&
                Objects.equals(fileTime, fileInfo.fileTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, fileTime);
    }
}
