package com.shoppingcart.notification.invoice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.notification.invoice.dto.GeneratedFile;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCart;
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

    public static final String TYPE = "_type";

    private final ObjectMapper objectMapper;

    private final MailSenderService mailSenderService;

    private final InvoiceGenerator invoicePdfGenerator;

    @JmsListener(destination = "demo.queue")
    public void read(final Message jsonMessage) throws JMSException, JsonProcessingException {

        if(!jsonMessage.propertyExists(TYPE)){
            log.warn("Property : {} is not informed in JMS Message: {}",TYPE,jsonMessage);
            return;
        }

        String type = jsonMessage.getStringProperty(TYPE);

        if(!type.contains(InvoiceShoppingCart.class.getSimpleName())){
            log.warn("Type request: {} is not possible convert to: {}",type,InvoiceShoppingCart.class.getSimpleName());
            return;
        }

        String request = jsonMessage.getBody(String.class);

        InvoiceShoppingCart invoiceShoppingCart = objectMapper.readValue(request, InvoiceShoppingCart.class);

        log.info("Class: {}",invoiceShoppingCart);

        final GeneratedFile pdfFile = invoicePdfGenerator.generate(invoiceShoppingCart);

        mailSenderService.sendHtmlMailAsync("noreply.demo.microservice@gmail.com","Demo de envio", EmailUtil.buildHtmlInvoice(invoiceShoppingCart), Optional.of(pdfFile));
    }


}
