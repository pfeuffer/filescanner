package de.pfeufferweb.filewatch;

import java.util.Set;

public interface FileInfoHandler {

    void handle(Set<FileInfo> files);
}
