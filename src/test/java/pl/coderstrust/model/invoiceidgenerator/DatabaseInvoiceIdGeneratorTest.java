package pl.coderstrust.model.invoiceidgenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.impl.InMemoryDatabase;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseInvoiceIdGeneratorTest {


  @Test
  public void shouldReturnNextInvoiceId() throws Exception {
    //given
    Database database = new InMemoryDatabase();
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 100);
    //when
    List<Integer> actualIdList = invoices
        .stream()
        .map(invoice -> invoice.getId())
        .sorted()
        .collect(Collectors.toList());
    //then
    for (Integer i = 1; i <= 10; i++) {
      assertEquals(i, actualIdList.get(i - 1));
    }
  }
}