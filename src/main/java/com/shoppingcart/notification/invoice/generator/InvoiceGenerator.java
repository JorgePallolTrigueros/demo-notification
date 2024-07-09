package com.shoppingcart.notification.invoice.generator;

import com.shoppingcart.notification.invoice.dto.GeneratedFile;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCart;

public interface InvoiceGenerator {

    GeneratedFile generate(InvoiceShoppingCart invoiceShoppingCart);

}
