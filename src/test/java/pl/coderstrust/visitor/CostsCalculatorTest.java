package pl.coderstrust.visitor;

import static org.junit.Assert.assertEquals;
import static pl.coderstrust.configuration.CompanyConfigurationProvider.COMPANY_NAME;
import static pl.coderstrust.configuration.CompanyConfigurationProvider.OUTSIDE_COMPANY_NAME;

import org.junit.Test;
import pl.coderstrust.database.impl.InMemoryDatabase;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;

import java.math.BigDecimal;
import java.util.List;

public class CostsCalculatorTest {

  @Test
  public void shouldReturnTotalCostsFromInvoiceBook() throws Exception {
    //given
    InvoiceBook invoiceBook = new InvoiceBook(new InMemoryDatabase());
    int numberOfInvoices = 20;
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(OUTSIDE_COMPANY_NAME, "", numberOfInvoices);
    for (Invoice invoice : invoices) {
      invoiceBook.addInvoice(invoice);
    }
    InvoiceVisitor visitor = new CostsCalculator();
    BigDecimal expected = BigDecimal.valueOf(
        numberOfInvoices).multiply(invoices.get(0).getInvoiceItems().get(0).getAmount());
    //when
    BigDecimal actual = invoiceBook.accept(visitor);
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnNullAsSumOfCostsFromInvoiceBook() throws Exception {
    //given
    InvoiceBook invoiceBook = new InvoiceBook(new InMemoryDatabase());
    int numberOfInvoices = 20;
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(COMPANY_NAME, "", numberOfInvoices);
    for (Invoice invoice : invoices) {
      invoiceBook.addInvoice(invoice);
    }
    InvoiceVisitor visitor = new CostsCalculator();
    BigDecimal expected = BigDecimal.valueOf(0);
    System.out.println(expected);
    //when
    BigDecimal actual = invoiceBook.accept(visitor);
    System.out.println(actual);

    //then
    assertEquals(expected, actual);
  }
}