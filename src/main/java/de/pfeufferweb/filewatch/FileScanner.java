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
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
public class FileScanner {

    private static Log LOG = LogFactory.getLog(FileScanner.class);

    private final String directory;
    private final FileInfoHandler fileInfoHandler;
    private final Collection<String> acceptedTypes;

    public FileScanner(@Value("${directory}") String directory, @Value("${acceptedTypes}") String acceptedTypes, FileInfoHandler fileInfoHandler) {
        this.directory = directory;
        this.acceptedTypes = asList(acceptedTypes.toLowerCase().split(","));
        this.fileInfoHandler = fileInfoHandler;
    }

    @Scheduled(fixedDelayString = "${scanIntervalMs}")
    public void scan() {
        LOG.debug("start scan of " + directory);
        Path directoryToWatch = Paths.get(directory);
        try {
            scan(directoryToWatch);
        } catch (Exception e) {
            LOG.error("error scanning directory", e);
        }
        LOG.debug("scan finished");
    }

    private void scan(Path directoryToWatch) {
        listDirectory(directoryToWatch)
                .map(this::createFileInfos)
                .ifPresent(fileInfoHandler::handle);
    }

    private Stream<FileInfo> createFileInfos(Stream<Path> pathStream) {
        return pathStream
                .filter(Files::isRegularFile)
                .filter(f -> acceptedTypes.stream().anyMatch(type -> f.toString().toLowerCase().endsWith(type)))
                .map(FileInfo::buildFrom);
    }

    private Optional<Stream<Path>> listDirectory(Path directoryToWatch) {
        try {
            return of(Files.list(directoryToWatch));
        } catch (IOException e) {
            LOG.fatal("could not list files in directory", e);
            return empty();
        }
    }
}
