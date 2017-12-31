package de.pfeufferweb.filewatch;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class SmtpMailAlerter implements Alerter {

    private final SmtpMailer mailer;
    private final UrlFactory urlFactory;

    public SmtpMailAlerter(SmtpMailer mailer, UrlFactory urlFactory) {
        this.mailer = mailer;
        this.urlFactory = urlFactory;
    }

    @Override
    public void alert(Instant instant, List<FileInfo> fileInfos) {
        mailer.sendHtmlMail("A new motion has been detected!", buildText(instant, fileInfos));
    }

    private String buildText(Instant instant, List<FileInfo> fileInfos) {
        HtmlMessageBuilder b = new HtmlMessageBuilder();
        b.bold("We have detected " + fileInfos.size() + " new motion images!");
        b.paragraph();
        b.link(urlFactory.hostPart() + urlFactory.createGroupUrl(instant), "Overview");
        HtmlMessageBuilder.ListBuilder list = b.list();
        fileInfos.forEach(i -> {
            list.startItem();
            list.link(urlFactory.hostPart() + urlFactory.createImageUrl(i.getName()), i.getFileTime().toString());
            list.endItem();
        });
        list.endList();
        return b.toString();
    }
}
