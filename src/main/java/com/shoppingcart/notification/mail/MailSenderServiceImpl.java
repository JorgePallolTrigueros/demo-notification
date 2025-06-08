package com.shoppingcart.notification.mail;

import com.shoppingcart.notification.invoice.dto.GeneratedFileDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendSimpleMailAsync(String destination, String title, String content) {

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(destination);
            simpleMailMessage.setFrom("noreply.demo.microservice@gmail.com");
            simpleMailMessage.setSubject(title);
            simpleMailMessage.setText(content);

            log.info("Sending email: {}", simpleMailMessage);

            mailSender.send(simpleMailMessage);

            log.info("Mail send  ok");
        }catch (Exception ex){
            log.error("Ocurrio un error al enviar email",ex);
        }
    }

    @Async
    @Override
    public void sendHtmlMailAsync(String destination, String title, String content, Optional<GeneratedFileDto> generatedFile) {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(destination);
            helper.setFrom("noreply.demo.microservice@gmail.com");
            helper.setSubject(title);
            helper.setText(content, true);

            if(generatedFile.isPresent()){
                ByteArrayDataSource bds = new ByteArrayDataSource(generatedFile.get().getFile().readAllBytes(), "application/pdf");
                helper.addAttachment(generatedFile.get().getName(),bds);
            }

            log.info("Sending email: {}", mimeMessage);

            mailSender.send(mimeMessage);

            log.info("Mail: {} sent successfully", title);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }

    @Override
    public void sendHtmlMailAsync(String destination, String title, String content, Optional<GeneratedFileDto> generatedFile, Optional<InputStream> optionalImageStream) {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(destination);
            helper.setFrom("noreply.demo.microservice@gmail.com");
            helper.setSubject(title);
            helper.setText(content, true);

            // Si existe el archivo PDF (u otro), lo adjunta
            if (generatedFile.isPresent()) {
                ByteArrayDataSource bds = new ByteArrayDataSource(
                        generatedFile.get().getFile().readAllBytes(), "application/pdf"
                );
                helper.addAttachment(generatedFile.get().getName(), bds);
            }

            // Si existe la imagen a incrustar, la añade como recurso embebido (inline)
            if (optionalImageStream.isPresent()) {
                ByteArrayDataSource imageDataSource = new ByteArrayDataSource(
                        optionalImageStream.get(), "image/jpeg"
                );
                helper.addInline("imagen1", imageDataSource);  // El mismo cid que se usa en el HTML
            }

            log.info("Sending email: {}", mimeMessage);
            mailSender.send(mimeMessage);
            log.info("Mail: {} sent successfully", title);
        } catch (Exception e) {
            log.error("Failed to send email", e);
        }
    }


}
