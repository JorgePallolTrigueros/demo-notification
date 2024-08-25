package com.shoppingcart.notification.util;

import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;
import com.shoppingcart.notification.invoice.dto.ProductDto;

public class EmailUtil {
    public static String buildHtmlInvoice(final InvoiceShoppingCartDto invoice) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Invoice</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }");
        html.append(".container { width: 80%; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }");
        html.append(".header { text-align: center; padding: 10px 0; border-bottom: 2px solid #007BFF; }");
        html.append(".header h1 { margin: 0; color: #007BFF; }");
        html.append(".header p { margin: 0; color: #6c757d; }");
        html.append(".invoice-details, .products { width: 100%; margin-top: 20px; border-collapse: collapse; }");
        html.append(".invoice-details td, .products th, .products td { padding: 15px; border: 1px solid #ddd; }");
        html.append(".invoice-details td { font-weight: bold; }");
        html.append(".products th { background-color: #007BFF; color: #fff; text-align: left; }");
        html.append(".products td { background-color: #f9f9f9; }");
        html.append(".footer { text-align: right; padding: 10px 0; margin-top: 20px; border-top: 2px solid #007BFF; }");
        html.append(".footer p { margin: 0; color: #6c757d; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>Invoice</h1>");
        html.append("<p>Invoice ID: ").append(invoice.getId()).append("</p>");
        html.append("</div>");
        html.append("<table class='invoice-details'>");
        html.append("<tr><td>Business Name:</td><td>").append(invoice.getBusinessName()).append("</td></tr>");
        html.append("<tr><td>Date:</td><td>").append(invoice.getDatetime()).append("</td></tr>");
        html.append("<tr><td>Subtotal:</td><td>").append(invoice.getSubtotal()).append("</td></tr>");
        html.append("<tr><td>Total Tax:</td><td>").append(invoice.getTotalTax()).append(" (").append(invoice.getTaxDescription()).append(")</td></tr>");
        html.append("<tr><td>Total:</td><td>").append(invoice.getTotal()).append("</td></tr>");
        html.append("</table>");
        html.append("<h2 style='color: #007BFF; margin-top: 20px;'>Products</h2>");
        html.append("<table class='products'>");
        html.append("<tr><th>ID</th><th>Name</th><th>Category</th><th>Description</th><th>Quantity</th><th>Price</th><th>Subtotal</th></tr>");
        for (ProductDto productDto : invoice.getProductDtos()) {
            html.append("<tr>");
            html.append("<td>").append(productDto.getId()).append("</td>");
            html.append("<td>").append(productDto.getName()).append("</td>");
            html.append("<td>").append(productDto.getCategory()).append("</td>");
            html.append("<td>").append(productDto.getDescription()).append("</td>");
            html.append("<td>").append(productDto.getQuantity()).append("</td>");
            html.append("<td>").append(productDto.getPrice()).append("</td>");
            html.append("<td>").append(productDto.getSubtotal()).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("<div class='footer'>");
        html.append("<p>Thank you for your business!</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }


}
