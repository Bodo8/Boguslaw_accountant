package pl.coderstrust.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.impl.InvoiceProcessor;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;
import pl.coderstrust.reportpdf.ReportPdfProcessor;

import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
public class PdfService {

  @Resource
  private final InvoiceBook invoiceBook;
  ReportPdfProcessor pdfProcessor = new ReportPdfProcessor(new InvoiceProcessor());

  public PdfService(InvoiceBook invoiceBook) {
    this.invoiceBook = invoiceBook;
  }


  /**
   * generates list invoices to pdf report.
   *
   * @param year the year from which the invoice comes.
   * @param month the month from which the invoice comes.
   * @throws IOException when doesn't save the file.
   */
  public void generateListInvoicesToPdfReport(Integer year, String month)
      throws IOException {
    Month enumMonth = Month.valueOf(month.toUpperCase());
    List<Invoice> bookInvoices = invoiceBook.getInvoices(Year.of(year), enumMonth);
    pdfProcessor.SaveDataToReportPdf(bookInvoices, year, month);
  }

}
