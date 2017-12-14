package de.pfeufferweb.filewatch;

import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class AlertingGroupHandlerTest {

    private final Alerter alerter = spy(Alerter.class);

    private final AlertingGroupHandler handler = new AlertingGroupHandler(alerter);

    @Test
    public void shouldListNothingOnStartup() {
        assertTrue(handler.getGroups().isEmpty());
    }

    @Test
    public void shouldNotAlertOnInitialList() {
        handler.handle(singletonMap(Instant.now(), singletonList(mock(FileInfo.class))));
        verify(alerter, never()).alert(any(), anyList());
    }

    @Test
    public void shouldAlertOnSubsequentEvents() {
        handler.handle(singletonMap(Instant.now().minus(5, MINUTES), singletonList(mock(FileInfo.class))));

        List<FileInfo> newFileList = singletonList(mock(FileInfo.class));
        Instant newInstant = Instant.now();
        handler.handle(singletonMap(newInstant, newFileList));

        verify(alerter).alert(newInstant, newFileList);
    }

    @Test
    public void shouldListEvents() {
        List<FileInfo> files1 = singletonList(mock(FileInfo.class));
        Instant instant1 = Instant.now().minus(5, MINUTES);
        handler.handle(singletonMap(instant1, files1));

        assertThat(handler.get(instant1), is(equalTo(files1)));
    }
}