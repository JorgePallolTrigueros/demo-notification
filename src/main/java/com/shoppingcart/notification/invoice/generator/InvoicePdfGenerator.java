package com.shoppingcart.notification.invoice.generator;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.shoppingcart.notification.invoice.dto.GeneratedFileDto;
import com.shoppingcart.notification.invoice.dto.InvoiceShoppingCartDto;
import com.shoppingcart.notification.invoice.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class InvoicePdfGenerator implements InvoiceGenerator {


    @Override
    public GeneratedFileDto generate(InvoiceShoppingCartDto invoice) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4);

            // Añadir encabezado de la factura
            document.add(new Paragraph("Factura")
                    .setBold()
                    .setFontSize(20)
                    .setMarginBottom(20));

            // Añadir información de la empresa y la fecha
            document.add(new Paragraph("Empresa: " + invoice.getBusinessName())
                    .setFontSize(12));
            document.add(new Paragraph("ID Empresa: " + invoice.getBusinessId())
                    .setFontSize(12));
            document.add(new Paragraph("Fecha: " + invoice.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Crear tabla de productos
            float[] columnWidths = {1, 5, 1, 2, 2, 2};
            Table table = new Table(columnWidths);
            table.addHeaderCell("ID");
            table.addHeaderCell("Nombre");
            table.addHeaderCell("Cantidad");
            table.addHeaderCell("Precio");
            table.addHeaderCell("Subtotal");
            table.addHeaderCell("Categoría");

            List<ProductDto> productDtos = invoice.getProductDtos();
            for (ProductDto productDto : productDtos) {
                table.addCell(productDto.getId().toString());
                table.addCell(productDto.getName());
                table.addCell(productDto.getQuantity().toString());
                table.addCell(productDto.getPrice().toString());
                table.addCell(productDto.getSubtotal().toString());
                table.addCell(productDto.getCategory());
            }

            // Añadir tabla al documento
            document.add(table);

            // Añadir totales
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Subtotal: " + invoice.getSubtotal().toString())
                    .setBold());
            document.add(new Paragraph("Impuesto (" + invoice.getTaxDescription() + "): " + invoice.getTotalTax().toString())
                    .setBold());
            document.add(new Paragraph("Total: " + invoice.getTotal().toString())
                    .setBold());

            // Cerrar el documento
            document.close();

            return GeneratedFileDto
                    .builder()
                    .name("FACTURA_"+invoice.getId()+new SimpleDateFormat("MM_dd_yyyy_HH_mm").format(new Date())+".pdf")
                    .type("PDF")
                    .createdAt(new Date())
                    .file(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
