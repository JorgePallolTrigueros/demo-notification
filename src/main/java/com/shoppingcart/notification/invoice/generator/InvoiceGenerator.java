package com.shoppingcart.notification.invoice.generator;

import com.shoppingcart.notification.invoice.dto.GeneratedFileDto;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;

public interface InvoiceGenerator {

    GeneratedFileDto generate(InvoiceShoppingCartDto invoiceShoppingCartDto);

}
