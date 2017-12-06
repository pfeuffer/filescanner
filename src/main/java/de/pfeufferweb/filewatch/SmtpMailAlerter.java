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
    public void alert(List<FileInfo> fileInfos) {
        // Setup mail server
        System.getProperties().setProperty("mail.smtp.host", environment.getProperty("mail.host"));

        Session session = Session.getDefaultInstance(System.getProperties());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(environment.getProperty("mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(environment.getProperty("mail.recipient")));
            message.setSubject("A new motion has been detected!");
            message.setText(buildText(fileInfos));
            doSend(message);
            LOG.info("Sent message successfully....");
        } catch (MessagingException e) {
            LOG.error("error sending mail", e);
        }
    }

    void doSend(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    private String buildText(List<FileInfo> fileInfos) {
        StringBuilder b = new StringBuilder();
        b.append("We have detected ").append(fileInfos.size()).append(" new motion images!\n");
        fileInfos.forEach(i -> b.append("- ").append(urlFactory.createImageUrl(i.getName())).append("\t").append(i.getFileTime()).append("\n"));
        return b.toString();
    }
}
