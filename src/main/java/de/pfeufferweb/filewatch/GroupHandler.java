package de.pfeufferweb.filewatch;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface GroupHandler {
    void handle(Map<Instant, List<FileInfo>> groups);
}
