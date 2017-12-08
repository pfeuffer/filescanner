package de.pfeufferweb.filewatch;

import java.time.Instant;
import java.util.List;

public interface Alerter {
    void alert(Instant instant, List<FileInfo> fileInfos);
}
