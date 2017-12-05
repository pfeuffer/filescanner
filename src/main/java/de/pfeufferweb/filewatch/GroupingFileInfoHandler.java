package de.pfeufferweb.filewatch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GroupingFileInfoHandler implements FileInfoHandler {

    private final long groupTime;

    public GroupingFileInfoHandler(@Value("${groupTime}") long groupTime) {
        this.groupTime = groupTime;
    }

    @Override
    public void handle(Stream<FileInfo> files) {
        Map<Long, List<FileInfo>> groups = files.collect(Collectors.groupingBy(fileInfo -> fileInfo.getFileTime().toMillis() / groupTime * groupTime, Collectors.toList()));
        groups.entrySet().stream().forEach(e -> System.out.println(new Date(e.getKey()) + ": " + e.getValue().size()));
    }
}
