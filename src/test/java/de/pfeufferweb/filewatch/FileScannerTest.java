package de.pfeufferweb.filewatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FileScannerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private FileInfoHandler fileInfoHandler;

    @Captor
    private ArgumentCaptor<Stream<FileInfo>> fileInfoSetCaptor;

    @Test
    public void shouldFindFiles() throws IOException {
        File tempDir = temporaryFolder.getRoot();
        Path newFile = new File(tempDir, "a").toPath();
        Files.createFile(newFile);

        new FileScanner(tempDir.getAbsolutePath(), fileInfoHandler).scan();

        verify(fileInfoHandler).handle(any());
        assertThat(fileInfoSetCaptor.getValue().collect(toList()).size(), is(1));
    }

    @Test
    public void shouldIgnoreDirectories() throws IOException {
        File tempDir = temporaryFolder.getRoot();
        Path newDirectory = new File(tempDir, "a").toPath();
        Files.createDirectory(newDirectory);

        doNothing().when(fileInfoHandler).handle(fileInfoSetCaptor.capture());

        new FileScanner(tempDir.getAbsolutePath(), fileInfoHandler).scan();

        verify(fileInfoHandler).handle(any());
        assertThat(fileInfoSetCaptor.getValue().collect(toList()).isEmpty(), is(true));
    }

    @Before
    public void init() {
        initMocks(this);
        doNothing().when(fileInfoHandler).handle(fileInfoSetCaptor.capture());
    }
}