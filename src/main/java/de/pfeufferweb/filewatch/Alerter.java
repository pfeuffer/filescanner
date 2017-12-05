package de.pfeufferweb.filewatch;

import java.util.List;

public interface Alerter {
    void alert(List<FileInfo> fileInfos);
}
