package com.shoppingcart.notification.user.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.notification.constant.AppConstant;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;
import com.shoppingcart.notification.invoice.listener.InvoiceShoppingCartListener;
import com.shoppingcart.notification.mail.MailSenderService;
import com.shoppingcart.notification.user.dto.UserDto;
import com.shoppingcart.notification.util.EmailUtil;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserListener {

    private final ObjectMapper objectMapper;

    private final MailSenderService mailSenderService;

    @JmsListener(destination = "password-recovery-queue")
    public void read(final Message jsonMessage) throws JMSException, JsonProcessingException {

        if (!jsonMessage.propertyExists(AppConstant.TYPE)) {
            log.warn("Property : {} is not informed in JMS Message: {}", AppConstant.TYPE, jsonMessage);
            return;
        }

        String type = jsonMessage.getStringProperty(AppConstant.TYPE);

        if(!type.contains(UserDto.class.getSimpleName())){
            log.warn("Type request: {} is not possible convert to: {}",type, UserDto.class.getSimpleName());
            return;
        }

        String request = jsonMessage.getBody(String.class);

        UserDto user = objectMapper.readValue(request, UserDto.class);

        log.info("Class: {}", user);



        mailSenderService.sendSimpleMailAsync(user.getEmail(), "RECUPERACIÓN DE CONTRASEÑA", EmailUtil.buildPasswordResetEmail(user,"https://www.w3.org/TR/2024/WD-change-password-url-20240603/"));

    }


}
