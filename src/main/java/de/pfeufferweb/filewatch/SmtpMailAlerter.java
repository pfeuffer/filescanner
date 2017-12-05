package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private final String mailRecipientAddress;
    private final String mailSenderAddress;
    private final String mailHost;

    public SmtpMailAlerter(
            @Value("${mail.recipient}") String mailRecipientAddress,
            @Value("${mail.sender}") String mailSenderAddress,
            @Value("${mail.host}") String mailHost) {
        this.mailRecipientAddress = mailRecipientAddress;
        this.mailSenderAddress = mailSenderAddress;
        this.mailHost = mailHost;
    }

    @Override
    public void alert(List<FileInfo> fileInfos) {
        // Setup mail server
        System.getProperties().setProperty("mail.smtp.host", mailHost);

        Session session = Session.getDefaultInstance(System.getProperties());
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailSenderAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailRecipientAddress));
            message.setSubject("A new motion has been detected!");
            message.setText("We have detected " + fileInfos.size() + " new motion images!");
            Transport.send(message);
            LOG.info("Sent message successfully....");
        } catch (MessagingException e) {
            LOG.error("error sending mail", e);
        }
    }
}
