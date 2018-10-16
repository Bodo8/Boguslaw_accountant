package pl.coderstrust.model;


import static pl.coderstrust.dateprocessor.LocalDateProcessor.getFirstDayOfMonthInYear;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.getLastDayOfMonthInYear;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.isDateFromGivenPeriod;

import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.visitor.InvoiceBookCalculator;
import pl.coderstrust.visitor.InvoiceVisitor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * InvoiceBook.
 */

@Service
public class InvoiceBook implements InvoiceBookCalculator {


  private final Database database;


  public InvoiceBook(Database database) {
    this.database = database;
  }

  public void addInvoice(Invoice invoice) {
    database.saveInvoice(invoice);
  }

  public List<Invoice> getInvoices() throws IOException {
    return database.getInvoices();
  }

  /**
   * @param id - invoice id.
   * @return - invoice, which posses given id
   */
  public Invoice getInvoiceById(int id) {
    return database.getInvoices()
        .stream()
        .filter(invoice -> invoice.getId() == id)
        .findAny()
        .orElse(null);
  }

  /**
   * @param month - tells from which month invoices should be provided.
   * @return - method returns a sorted list of invoices from given month.
   */

  public List<Invoice> getInvoices(Year year, Month month) {
    LocalDate fromDate = getFirstDayOfMonthInYear(year, month);
    LocalDate toDate = getLastDayOfMonthInYear(year, month);
    return getInvoices(fromDate, toDate);
  }

  /**
   * @param fromDate - the date invoices should be provided from.
   * @param toDate - the date invoices should be provided to.
   * @return - method returns a sorted list of invoices from given period.
   */
  public List<Invoice> getInvoices(LocalDate fromDate, LocalDate toDate) {
    return database.getInvoices()
        .stream()
        .filter(invoice
            -> (isDateFromGivenPeriod(invoice.getDate(), fromDate, toDate)))
        .sorted()
        .collect(Collectors.toList());
  }

  public void removeInvoice(Integer id) {
    Invoice foundInvoice = getInvoiceById(id);
    database.removeInvoice(foundInvoice);
  }

  @Override
  public String toString() {
    return "InvoiceBook{"
        + "database=" + database
        + '}';
  }

  @Override
  public BigDecimal accept(InvoiceVisitor visitor) {
    return
        database.getInvoices()
            .stream()
            .map(visitor::visit)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
  }

  public Invoice createInvoice(InvoiceBody invoiceBody) {
    return database.createInvoice(invoiceBody);
  }

}
