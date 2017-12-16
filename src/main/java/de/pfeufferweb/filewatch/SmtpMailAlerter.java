package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.List;

@Component
public class SmtpMailAlerter implements Alerter{

    private static Log LOG = LogFactory.getLog(SmtpMailAlerter.class);

    private final Environment environment;
    private final UrlFactory urlFactory;

    public SmtpMailAlerter(Environment environment, UrlFactory urlFactory) {
        this.environment = environment;
        this.urlFactory = urlFactory;
    }

    @Override
    public void alert(Instant instant, List<FileInfo> fileInfos) {
        // Setup mail server
        System.getProperties().setProperty("mail.smtp.host", environment.getProperty("mail.host"));

        Session session = Session.getDefaultInstance(System.getProperties());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(environment.getProperty("mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(environment.getProperty("mail.recipient")));
            message.setSubject("A new motion has been detected!");
            message.setContent(buildText(instant, fileInfos), "text/html; charset=utf-8");
            doSend(message);
            LOG.info("Sent message successfully....");
        } catch (MessagingException e) {
            LOG.error("error sending mail", e);
        }
    }

    void doSend(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    private String buildText(Instant instant, List<FileInfo> fileInfos) {
        StringBuilder b = new StringBuilder();
        b.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n");
        b.append("<html>\n<body>\n");
        b.append("<b>We have detected ").append(fileInfos.size()).append(" new motion images!</b>\n");
        b.append("<p><a href='").append(urlFactory.hostPart()).append(urlFactory.createGroupUrl(instant)).append("'>Overview</a>\n");
        b.append("<ul>");
        fileInfos.forEach(i -> b.append("<li><a href='").append(urlFactory.hostPart()).append(urlFactory.createImageUrl(i.getName())).append("'>").append(i.getFileTime()).append("</a></li>\n"));
        b.append("</ul>\n");
        b.append("</body>\n</html>\n");
        return b.toString();
    }
}
