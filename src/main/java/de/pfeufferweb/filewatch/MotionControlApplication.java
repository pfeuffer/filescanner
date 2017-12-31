package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableScheduling
public class MotionControlApplication {
    private static Log LOG = LogFactory.getLog(MotionControlApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MotionControlApplication.class, args);
        LOG.info("Context build");
        context.getBean(SmtpMailer.class).sendHtmlMail("Server started",
                new HtmlMessageBuilder()
                        .bold("Motion Control server started.")
                        .paragraph()
                        .text("Motion detection is ").bold("not").text(" enabled, yet!")
                        .toString());
    }
}

@EnableWebMvc
@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToInstantConverter());
        registry.addConverter(new InstantToStringConverter());
    }
}