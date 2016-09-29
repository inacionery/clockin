package org.clockin.service;

import java.util.List;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.CharEncoding;
import org.clockin.domain.SocialUserConnection;
import org.clockin.domain.User;
import org.clockin.repository.SocialUserConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private SocialUserConnectionRepository socialUserConnectionRepository;

    public boolean sendEmail(User user, String to, String[] bcc, String subject,
        String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        // Prepare message using a Spring helper
        javaMailSender.setUsername(user.getEmail());

        List<SocialUserConnection> list = socialUserConnectionRepository
            .findAllByUserIdOrderByProviderIdAscRankAsc(user.getLogin());

        javaMailSender.setPassword(list.get(0).getAccessToken());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setBcc(bcc);
            message.setFrom(user.getEmail());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
            return true;
        }
        catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}",
                to, e.getMessage());
            return false;
        }
    }
}
