package com.shoppingcart.notification.util;

import com.shoppingcart.notification.campaign.dto.CampaignRequestDto;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;
import com.shoppingcart.notification.invoice.dto.ProductDto;
import com.shoppingcart.notification.user.dto.UserDto;

public class EmailUtil {


    public static String buildCampaignEmail(final CampaignRequestDto campaign) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>").append(campaign.getName()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }");
        html.append(".container { width: 80%; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }");
        html.append(".header { text-align: center; padding: 10px 0; border-bottom: 2px solid #FF4500; }");
        html.append(".header h1 { margin: 0; color: #FF4500; }");
        html.append(".header p { margin: 0; color: #6c757d; }");
        html.append(".content { text-align: center; padding: 20px; }");
        html.append(".content p { font-size: 18px; margin-bottom: 20px; color: #333; line-height: 1.6; }");
        html.append(".discount { font-size: 24px; color: #FF6347; font-weight: bold; margin: 20px 0; }");
        html.append(".days-left { font-size: 20px; color: #28a745; margin-bottom: 20px; }");
        html.append(".cta-button { padding: 15px 30px; background-color: #FF4500; color: #fff; font-size: 18px; text-decoration: none; border-radius: 10px; margin-top: 20px; display: inline-block; }");
        html.append(".cta-button:hover { background-color: #FF6347; }");
        html.append(".footer { text-align: center; padding: 10px 0; margin-top: 20px; border-top: 2px solid #FF4500; }");
        html.append(".footer p { margin: 0; color: #6c757d; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>").append(campaign.getName()).append("</h1>");
        html.append("<p>").append(campaign.getDescription()).append("</p>");
        html.append("</div>");
        html.append("<div class='content'>");
        html.append("<p><strong>¡Gran Oferta Especial!</strong></p>");
        html.append("<p><span class='discount'>¡Descuento de ").append(campaign.getDiscount()).append("%!</span></p>");
        html.append("<p class='days-left'>¡Solo por ").append(campaign.getDaysDuration()).append(" días!</p>");
        html.append("<p>¡No te pierdas esta oportunidad única! Haz tu compra ahora antes de que se acabe el tiempo.</p>");
        html.append("<a href='#' class='cta-button'>¡Compra Ahora!</a>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<p>Esta campaña es válida para todos los productos y usuarios registrados.</p>");
        html.append("<p>Si tienes alguna duda, no dudes en contactarnos.</p>");
        html.append("<p>¡Gracias por ser parte de nuestra comunidad!</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }


    public static String buildLowStockNotificationEmail(final com.shoppingcart.notification.product.dto.ProductDto product) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Low Stock Notification</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }");
        html.append(".container { width: 80%; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }");
        html.append(".header { text-align: center; padding: 10px 0; border-bottom: 2px solid #FF6347; }");
        html.append(".header h1 { margin: 0; color: #FF6347; }");
        html.append(".content { padding: 20px; text-align: center; }");
        html.append(".content p { margin-bottom: 20px; font-size: 16px; color: #333; }");
        html.append(".footer { text-align: center; padding: 10px 0; margin-top: 20px; border-top: 2px solid #FF6347; color: #6c757d; }");
        html.append(".product-details { text-align: left; margin: 20px 0; }");
        html.append(".product-details img { max-width: 100%; height: auto; border-radius: 10px; margin-bottom: 10px; }");
        html.append(".product-details p { font-size: 14px; color: #555; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>Low Stock Alert</h1>");
        html.append("</div>");
        html.append("<div class='content'>");
        html.append("<p>Dear Manager,</p>");
        html.append("<p>The following product has low stock in inventory:</p>");
        html.append("<div class='product-details'>");
        if (product.getGalleries() != null && !product.getGalleries().isEmpty()) {
            html.append("<img src='" + product.getGalleries().get(0).getUrl() + "' alt='Product Image'>");
        }
        html.append("<p><strong>Product Name:</strong> ").append(product.getName()).append("</p>");
        html.append("<p><strong>Description:</strong> ").append(product.getDescription()).append("</p>");
        html.append("<p><strong>Price:</strong> $").append(product.getPrice()).append("</p>");
        html.append("<p><strong>Category:</strong> ").append(product.getCategory()).append("</p>");
        html.append("<p><strong>Current Stock:</strong> ").append(product.getQuantity()).append("</p>");
        html.append("<p><strong>Date & Time:</strong> ").append(java.time.LocalDateTime.now()).append("</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<p>Please take action to restock the item as soon as possible.</p>");
        html.append("<p>If you have any questions, please contact the inventory team.</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }


    public static String buildPasswordChangeEmail(final UserDto user) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Password Changed Successfully</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }");
        html.append(".container { width: 80%; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }");
        html.append(".header { text-align: center; padding: 10px 0; border-bottom: 2px solid #007BFF; }");
        html.append(".header h1 { margin: 0; color: #007BFF; }");
        html.append(".header p { margin: 0; color: #6c757d; }");
        html.append(".content { padding: 20px; text-align: center; }");
        html.append(".content p { margin-bottom: 20px; font-size: 16px; color: #333; }");
        html.append(".button { display: inline-block; padding: 10px 20px; background-color: #007BFF; color: white; text-decoration: none; border-radius: 5px; }");
        html.append(".footer { text-align: center; padding: 10px 0; margin-top: 20px; border-top: 2px solid #007BFF; color: #6c757d; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>Password Changed Successfully</h1>");
        html.append("</div>");
        html.append("<div class='content'>");
        html.append("<p>Hi ").append(user.getName()).append(",</p>");
        html.append("<p>Your password has been successfully changed. You can now log in using your new credentials.</p>");
        html.append("<a href='/login' class='button'>Go to Login</a>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<p>Email sent to: ").append(user.getEmail()).append("</p>");
        html.append("<p>If you have any issues, please contact our support team.</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }


    public static String buildPasswordResetEmail(final UserDto user, final String url) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Password Reset</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }");
        html.append(".container { width: 80%; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }");
        html.append(".header { text-align: center; padding: 10px 0; border-bottom: 2px solid #007BFF; }");
        html.append(".header h1 { margin: 0; color: #007BFF; }");
        html.append(".header p { margin: 0; color: #6c757d; }");
        html.append(".content { padding: 20px; text-align: center; }");
        html.append(".content p { margin-bottom: 20px; font-size: 16px; color: #333; }");
        html.append(".button { display: inline-block; padding: 10px 20px; background-color: #007BFF; color: white; text-decoration: none; border-radius: 5px; }");
        html.append(".footer { text-align: center; padding: 10px 0; margin-top: 20px; border-top: 2px solid #007BFF; color: #6c757d; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>Password Reset Request</h1>");
        html.append("</div>");
        html.append("<div class='content'>");
        html.append("<p>Hi ").append(user.getName()).append(",</p>");
        html.append("<p>We received a request to reset your password. You can reset it by clicking the button below:</p>");
        html.append("<a href='").append(url).append("' class='button'>Reset Password</a>");
        html.append("<p>If you didn't request a password reset, please ignore this email or contact support if you have any concerns.</p>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<p>Email sent to: ").append(user.getEmail()).append("</p>");
        html.append("<p>Thank you for using our service!</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }



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
        for (ProductDto productDto : invoice.getProducts()) {
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
