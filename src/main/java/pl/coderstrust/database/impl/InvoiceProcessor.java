package pl.coderstrust.database.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InvoiceProcessor {

  private static Logger logger = LoggerFactory.getLogger(InvoiceProcessor.class);

  InvoiceProcessorHelper processorHelper = new InvoiceProcessorHelper();
  ObjectMapper mapper = processorHelper.objectMapper();

  void saveToFile(List<Invoice> invoices, File filename) throws IOException {
    mapper.writeValue(filename, invoices);
  }

  public void saveToPdf(List<Invoice> bookInvoices, File filename) throws IOException {
    PDDocument doc = new PDDocument();
    PDPage page = new PDPage();
    float fontSize = 10f;

    try {
      doc.addPage(page);
      PDPageContentStream content = new PDPageContentStream(doc, page);
      content.beginText();
      content.setFont(PDType1Font.TIMES_ROMAN, fontSize);
      content.setLeading(fontSize);
      content.newLineAtOffset(25, 725);

      for (Invoice invoices : bookInvoices) {
        content.showText(invoices.toString());
        content.newLine();
      }
      content.endText();
      content.close();
      doc.save(filename);
      doc.close();
    } catch (Exception e) {
      logger.warn("Not able to save invoice to pdf file", e);
    }
  }

  List<Invoice> readFileAndConvertContentIntoListOfInvoices(String invoicePath)
      throws IOException {
    return mapper.readValue(new File(invoicePath),
        mapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));
  }
}