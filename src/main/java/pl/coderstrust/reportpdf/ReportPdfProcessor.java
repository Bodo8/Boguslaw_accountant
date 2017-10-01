package pl.coderstrust.reportpdf;

import pl.coderstrust.configuration.FileConfigurationProvider;
import pl.coderstrust.database.impl.InvoiceProcessor;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReportPdfProcessor {

  private final InvoiceProcessor invoiceProcessor;

  public ReportPdfProcessor(InvoiceProcessor invoiceProcessor) {
    this.invoiceProcessor = invoiceProcessor;
  }

  /**
   * @param invoices the invoices to save in the pdf file.
   * @param year the year from which the invoice comes.
   * @param month the month from which the invoice comes.
   * @throws IOException when doesn't save to pdf file.
   */
  public void SaveDataToReportPdf(List<Invoice> invoices, Integer year, String month)
      throws IOException {
    String directoryName = FileConfigurationProvider.DATABASE_TEST_PDF_PATH;
    File filePath = FileProcessor.getInvoicePdfPath(directoryName, year, month);
    invoiceProcessor.saveToPdf(invoices, filePath);
  }
}
