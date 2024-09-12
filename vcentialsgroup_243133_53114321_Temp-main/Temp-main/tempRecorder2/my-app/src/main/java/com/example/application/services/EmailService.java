package com.example.application.services;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmail(String recipient, String subject, String body, List<Attachment> attachments) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(body, true);  // true to indicate HTML content

        for (Attachment attachment : attachments) {
            helper.addAttachment(attachment.getFilename(), new ByteArrayResource(attachment.getData()));
        }

        mailSender.send(message);
    }

    public static class Attachment {
        private final byte[] data;
        private final String filename;

        public Attachment(byte[] data, String filename) {
            this.data = data;
            this.filename = filename;
        }

        public byte[] getData() {
            return data;
        }

        public String getFilename() {
            return filename;
        }
    }
}
