package de.pfeufferweb.filewatch;

import java.util.stream.Stream;

public interface FileInfoHandler {

    void handle(Stream<FileInfo> files);
}
