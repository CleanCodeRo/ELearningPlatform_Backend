package cleancode.eLearningPlatform.auth.service;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailService {
    private final String username;
    private final String password;
    private final Properties props;

    public EmailService(
            @Value("${MAIL_SMTP_AUTH}") String smtpAuth,
            @Value("${MAIL_SMTP_STARTTLS_ENABLE}") String starttlsEnable,
            @Value("${MAIL_SMTP_HOST}") String smtpHost,
            @Value("${MAIL_SMTP_PORT}") String smtpPort,
            @Value("${MAIL_SMTP_SSL_TRUST}") String sslTrust,
            @Value("${EMAIL_SENDER_ADDRESS}") String emailSenderAddress,
            @Value("${EMAIL_PASSWORD}") String emailPassword
    ) {
        this.username = emailSenderAddress;
        this.password = emailPassword;

        props = new Properties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", sslTrust);
    }

    public void sendEmail(String to, String subject, String body) {
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}


