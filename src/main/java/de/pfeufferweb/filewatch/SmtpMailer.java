package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class SmtpMailer {
    private static Log LOG = LogFactory.getLog(SmtpMailer.class);

    private final Environment environment;

    public SmtpMailer(Environment environment) {
        this.environment = environment;
    }

    public void sendHtmlMail(String subject, String content) {
        // Setup mail server
        System.getProperties().setProperty("mail.smtp.host", environment.getProperty("mail.host"));

        Session session = Session.getDefaultInstance(System.getProperties());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(environment.getProperty("mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(environment.getProperty("mail.recipient")));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            doSend(message);
            LOG.info("Sent message successfully....");
        } catch (MessagingException e) {
            LOG.error("error sending mail", e);
        }
    }

    void doSend(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    @PreDestroy
    public void exit() {
        this.sendHtmlMail("Shutdown", HtmlMessageBuilder.simpleText("Motion Control is shutting down."));
    }
}
