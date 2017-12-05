package de.pfeufferweb.filewatch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GroupingFileInfoHandler implements FileInfoHandler {

    private final long groupTime;
    private final GroupHandler groupHandler;

    public GroupingFileInfoHandler(@Value("${groupTime}") long groupTime, GroupHandler groupHandler) {
        this.groupTime = groupTime;
        this.groupHandler = groupHandler;
    }

    @Override
    public void handle(Stream<FileInfo> files) {
        Map<Instant, List<FileInfo>> groups = files.collect(Collectors.groupingBy(fileInfo -> instantFromFileInfo(fileInfo), Collectors.toList()));
        groupHandler.handle(groups);
    }

    private Instant instantFromFileInfo(FileInfo fileInfo) {
        return Instant.ofEpochMilli(fileInfo.getFileTime().toMillis() / groupTime * groupTime);
    }
}
