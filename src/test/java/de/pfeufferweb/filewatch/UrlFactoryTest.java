package de.pfeufferweb.filewatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UrlFactoryTest {

    @Mock
    private Environment environment;
    @Mock
    private EmbeddedWebApplicationContext context;
    @Mock
    private EmbeddedServletContainer container;

    @InjectMocks
    private UrlFactory urlFactory;

    @Test
    public void shouldProduceCorrectImageUrl() {
        String imageUrl = urlFactory.createImageUrl("x");
        assertEquals("http://localhost:8080/motioncontrol/image/x", imageUrl);
    }

    @Before
    public void init() throws IOException {
        initMocks(this);
        Properties properties = new Properties();
        properties.load(UrlFactoryTest.class.getResourceAsStream("/application.properties"));
        when(environment.getProperty(anyString())).thenAnswer(x -> properties.getProperty(x.getArguments()[0].toString()));
        when(context.getEmbeddedServletContainer()).thenReturn(container);
        when(container.getPort()).thenReturn(8080);
    }
}