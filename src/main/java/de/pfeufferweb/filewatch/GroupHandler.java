package de.pfeufferweb.filewatch;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GroupHandler {
    void handle(Map<Instant, List<FileInfo>> groups);

    Collection<FileInfo> get(Instant group);

    Collection<Instant> getGroups();
}
