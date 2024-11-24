package com.shoppingcart.notification.product.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.notification.constant.AppConstant;
import com.shoppingcart.notification.mail.MailSenderService;
import com.shoppingcart.notification.product.dto.ProductDto;
import com.shoppingcart.notification.user.dto.UserDto;
import com.shoppingcart.notification.util.EmailUtil;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductListener {



    private final ObjectMapper objectMapper;

    private final MailSenderService mailSenderService;

    @JmsListener(destination = "active-mq.product-low-stock-queue")
    public void read(final Message jsonMessage) throws JMSException, JsonProcessingException {

        if (!jsonMessage.propertyExists(AppConstant.TYPE)) {
            log.warn("Property : {} is not informed in JMS Message: {}", AppConstant.TYPE, jsonMessage);
            return;
        }

        String type = jsonMessage.getStringProperty(AppConstant.TYPE);

        if(!type.contains(ProductDto.class.getSimpleName())){
            log.warn("Type request: {} is not possible convert to: {}",type, UserDto.class.getSimpleName());
            return;
        }

        String request = jsonMessage.getBody(String.class);

        ProductDto productDto = objectMapper.readValue(request, ProductDto.class);

        log.info("Class: {}", productDto);


        mailSenderService.sendHtmlMailAsync("jorgepallol1@gmail.com", "PRODUCTO: "+productDto.getName()+" CON POCO INVENTARIO", EmailUtil.buildLowStockNotificationEmail(productDto), Optional.empty());
    }



}
