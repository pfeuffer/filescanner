package de.pfeufferweb.filewatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static de.pfeufferweb.filewatch.FileInfoMock.fileInfo;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.initMocks;

public class GroupingFileInfoHandlerTest {

    private static final Instant BASE_TIME = Instant.now().truncatedTo(SECONDS);
    private static final Instant GROUP_1_TIME_1 = BASE_TIME.plusMillis(1);
    private static final Instant GROUP_1_TIME_2 = BASE_TIME.plusMillis(2);
    private static final Instant GROUP_2_TIME_1 = BASE_TIME.plusMillis(100);
    private static final Instant GROUP_2_TIME_2 = BASE_TIME.plusMillis(101);

    @Mock
    private GroupHandler groupHandler;
    @Captor
    private ArgumentCaptor<Map<Instant, List<FileInfo>>> groupCaptor;
    private GroupingFileInfoHandler groupingFileInfoHandler;

    @Test
    public void shouldGroupCorrectly() {
        Map<Instant, List<FileInfo>> createdGroups = groupCaptor.getValue();
        assertThat(createdGroups.size(), is(2));
    }

    @Test
    public void shouldSetGroupKeyToRoundedTime() {
        Map<Instant, List<FileInfo>> createdGroups = groupCaptor.getValue();
        assertTrue(createdGroups.containsKey(BASE_TIME));
        assertTrue(createdGroups.containsKey(GROUP_2_TIME_1));
    }

    @Test
    public void shouldContainAllFileInfosForGroup() {
        Map<Instant, List<FileInfo>> createdGroups = groupCaptor.getValue();
        assertThat(createdGroups.get(BASE_TIME).size(), is(2));
        assertTrue(createdGroups.get(BASE_TIME).stream().anyMatch(i -> i.getName().equals("1-1")));
        assertTrue(createdGroups.get(BASE_TIME).stream().anyMatch(i -> i.getName().equals("1-2")));
    }

    @Before
    public void init() {
        initMocks(this);
        groupingFileInfoHandler = new GroupingFileInfoHandler(10, groupHandler);
        doNothing().when(groupHandler).handle(groupCaptor.capture());

        groupingFileInfoHandler.handle(
                Stream.of(
                        fileInfo("1-1", BASE_TIME),
                        fileInfo("1-2", GROUP_1_TIME_2),
                        fileInfo("2-1", GROUP_2_TIME_1),
                        fileInfo("2-2", GROUP_2_TIME_2)
                ));
    }
}