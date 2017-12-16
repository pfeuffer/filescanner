package de.pfeufferweb.filewatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.time.Instant;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UrlFactoryTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private UrlFactory urlFactory;

    private final Properties properties = new Properties();

    @Test
    public void shouldProduceCorrectImagePath() {
        String imageUrl = urlFactory.createImageUrl("x");
        assertEquals("/motioncontrol/image/x", imageUrl);
    }

    @Test
    public void shouldProduceCorrectGroupPath() {
        properties.setProperty("server.port", "8080");
        Instant group = Instant.parse("2017-12-30T13:15:00.000Z");
        String imageUrl = urlFactory.createGroupUrl(group);
        assertEquals("/motioncontrol/view/2017-12-30T13:15:00Z", imageUrl);
    }

    @Test
    public void shouldProduceCorrectHostUrlWithPort() {
        properties.setProperty("server.port", "8080");
        String imageUrl = urlFactory.hostPart();
        assertEquals("http://localhost:8080", imageUrl);
    }

    @Test
    public void shouldProduceCorrectHostUrlWithoutPort() {
        String imageUrl = urlFactory.hostPart();
        assertEquals("http://localhost", imageUrl);
    }

    @Before
    public void init() throws IOException {
        initMocks(this);
        properties.load(UrlFactoryTest.class.getResourceAsStream("/application.properties"));
        when(environment.getProperty(anyString())).thenAnswer(x -> properties.getProperty(x.getArguments()[0].toString()));
    }
}