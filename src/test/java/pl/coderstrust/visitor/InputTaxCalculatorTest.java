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

public class InputTaxCalculatorTest {

  @Test
  public void shouldReturnTotalInputTaxFromInvoiceBook() throws Exception {
    //given
    InvoiceBook invoiceBook = new InvoiceBook(new InMemoryDatabase());
    int numberOfInvoices = 20;
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(OUTSIDE_COMPANY_NAME, "", numberOfInvoices);
    for (Invoice invoice : invoices) {
      invoiceBook.addInvoice(invoice);
    }
    InvoiceVisitor visitor = new InputTaxCalculator();
    BigDecimal expected = BigDecimal.valueOf(numberOfInvoices)
        .multiply(invoices.get(0).getInvoiceItems().get(0).getVatAmount());
    //when
    BigDecimal actual = invoiceBook.accept(visitor);
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnNullAsTotalInputTaxFromInvoiceBook() throws Exception {
    //given
    InvoiceBook invoiceBook = new InvoiceBook(new InMemoryDatabase());
    int numberOfInvoices = 20;
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(COMPANY_NAME, "", numberOfInvoices);
    for (Invoice invoice : invoices) {
      invoiceBook.addInvoice(invoice);
    }
    InvoiceVisitor visitor = new InputTaxCalculator();
    BigDecimal expected = BigDecimal.valueOf(0);

    //when
    BigDecimal actual = invoiceBook.accept(visitor);

    //then
    assertEquals(expected, actual);
  }
}