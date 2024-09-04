package com.shoppingcart.notification.invoice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.notification.constant.AppConstant;
import com.shoppingcart.notification.invoice.dto.GeneratedFileDto;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;
import com.shoppingcart.notification.invoice.generator.InvoiceGenerator;
import com.shoppingcart.notification.mail.MailSenderService;
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
public class InvoiceShoppingCartListener {

    private final ObjectMapper objectMapper;

    private final MailSenderService mailSenderService;

    private final InvoiceGenerator invoicePdfGenerator;

    @JmsListener(destination = "demo.queue")
    public void read(final Message jsonMessage) throws JMSException, JsonProcessingException {

        if(!jsonMessage.propertyExists(AppConstant.TYPE)){
            log.warn("Property : {} is not informed in JMS Message: {}",AppConstant.TYPE,jsonMessage);
            return;
        }

        String type = jsonMessage.getStringProperty(AppConstant.TYPE);

        if(!type.contains(InvoiceShoppingCartDto.class.getSimpleName())){
            log.warn("Type request: {} is not possible convert to: {}",type, InvoiceShoppingCartDto.class.getSimpleName());
            return;
        }

        String request = jsonMessage.getBody(String.class);

        InvoiceShoppingCartDto invoiceShoppingCartDto = objectMapper.readValue(request, InvoiceShoppingCartDto.class);

        log.info("Class: {}", invoiceShoppingCartDto);

        final GeneratedFileDto pdfFile = invoicePdfGenerator.generate(invoiceShoppingCartDto);

        mailSenderService.sendHtmlMailAsync("noreply.demo.microservice@gmail.com","Demo de envio", EmailUtil.buildHtmlInvoice(invoiceShoppingCartDto), Optional.of(pdfFile));
    }


}
