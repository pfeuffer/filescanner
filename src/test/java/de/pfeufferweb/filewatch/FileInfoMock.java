package de.pfeufferweb.filewatch;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileInfoMock {
    public static FileInfo fileInfo(String mockName, Instant mockFileTime) {
        FileInfo mock = mock(FileInfo.class);
        when(mock.getName()).thenReturn(mockName);
        when(mock.getFileTime()).thenReturn(mockFileTime);
        return mock;
    }
}
