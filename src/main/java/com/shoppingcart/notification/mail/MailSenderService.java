package com.shoppingcart.notification.mail;

import com.shoppingcart.notification.invoice.dto.GeneratedFileDto;

import java.util.Optional;

public interface MailSenderService {

    void sendSimpleMailAsync(String destination, String title, String content);

    void sendHtmlMailAsync(String destination, String title, String content, Optional<GeneratedFileDto> generatedFile);
}
