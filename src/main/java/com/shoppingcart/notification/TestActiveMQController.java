package com.shoppingcart.notification;

import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;
import com.shoppingcart.notification.invoice.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@RestController
public class TestActiveMQController {

    private final JmsTemplate jmsTemplate;
    private final String destination;

    public TestActiveMQController(JmsTemplate jmsTemplate,@Value("${active-mq.queue}") String destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    @GetMapping("/")
    public void test(){
        InvoiceShoppingCartDto data = InvoiceShoppingCartDto
                .builder()
                .id("0-000000-1")
                .businessName("empresa s.a.")
                .businessId("b77665544")
                .tax(BigDecimal.valueOf(0.21))
                .taxDescription("IVA")
                .totalTax(BigDecimal.valueOf(2.1))
                .subtotal(BigDecimal.valueOf(10))
                .total(BigDecimal.valueOf(12.1))
                .productDtos(List.of(
                        ProductDto.builder().id(1L).name("Producto1").price(BigDecimal.TEN).category("Categoria1").quantity(BigDecimal.ONE).description("Producto de calidad 1").galleries(List.of("p1","p2")).build(),
                        ProductDto.builder().id(1L).name("Producto2").price(BigDecimal.TEN).category("Categoria2").quantity(BigDecimal.ONE).description("Producto de calidad 2").galleries(List.of("p1","p2")).build()
                ))
                .build();
        jmsTemplate.convertAndSend(destination, data);
        log.info("Mensaje enviado a la cola");
    }

}
