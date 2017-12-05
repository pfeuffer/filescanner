package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class FileScanner {

    private static Log LOG = LogFactory.getLog(FileScanner.class);

    private final String directory;

    public FileScanner(@Value("${directory}") String directory) {
        this.directory = directory;
    }

    @Scheduled(fixedDelayString = "${scanIntervalMs}")
    public void scan() {
        LOG.info("start scan of " + directory);
        Path directoryToWatch = Paths.get(directory);
        try {
            scan(directoryToWatch);
            LOG.info("scan finished");
        } catch (Exception e) {
            LOG.error("error scanning directory", e);
        }
    }

    private void scan(Path directoryToWatch) {
        listDirectory(directoryToWatch)
                .filter(Files::isRegularFile)
                .map(FileInfo::buildFrom)
                .forEach(System.out::println);
    }

    private Stream<Path> listDirectory(Path directoryToWatch) {
        try {
            return Files.list(directoryToWatch);
        } catch (IOException e) {
            LOG.fatal("could not list files in directory", e);
            return Stream.empty();
        }
    }
}
