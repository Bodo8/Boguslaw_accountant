package pl.coderstrust.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.impl.InvoiceProcessor;
import pl.coderstrust.emailprocessor.EmailSender;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;
import pl.coderstrust.reportpdf.ReportPdfProcessor;

import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.util.Iterator;
import java.util.List;

@Service
public class InvoiceBookService {

  private final EmailSender emailSender;
  @Resource
  private final InvoiceBook invoiceBook;
  ReportPdfProcessor pdfProcessor = new ReportPdfProcessor(new InvoiceProcessor());

  public InvoiceBookService(InvoiceBook invoiceBook, EmailSender emailSender) {
    this.invoiceBook = invoiceBook;
    this.emailSender = emailSender;
  }

  public Invoice getSingleInvoice(int id) {
    return invoiceBook.getInvoiceById(id);
  }


  public List<Invoice> getInvoicesFromGivenMonth(int year, String month) {
    Month enumMonth = Month.valueOf(month.toUpperCase());
    return invoiceBook.getInvoices(Year.of(year), enumMonth);
  }

  public List<Invoice> getInvoices() throws IOException {
    return invoiceBook.getInvoices();
  }

  /**
   * @param invoiceBody - body of invoice.
   * @return - id of invoice.
   */

  public int postInvoice(InvoiceBody invoiceBody) {
    Invoice invoice = invoiceBook.createInvoice(invoiceBody);
    invoiceBook.addInvoice(invoice);

    emailSender.sendEmail("adam.nowak.coder@gmail.com", invoice);
    return invoice.getId();
  }

  /**
   * @param id - id of invoice.
   * @param invoiceBody - body of invoice.
   * @throws IOException - when no success in reading and saving invoice.
   */

  public void putInvoice(Integer id, InvoiceBody invoiceBody)
      throws IOException {
    Iterator<Invoice> iterator = invoiceBook.getInvoices().iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getId() == id) {
        iterator.remove();
        Invoice invoice = invoiceBook.createInvoice(invoiceBody);
        invoiceBook.addInvoice(invoice);
      }
    }
  }

  /**
   * @param id - id of invoice.
   * @return - the number and description of exception
   * @throws IOException - when problems deleting invoice.
   */

  public boolean deleteInvoice(Integer id) throws IOException {
    Iterator<Invoice> iterator = invoiceBook.getInvoices().iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getId() == id) {
        iterator.remove();
        return true;
      }
    }
    return false;
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
