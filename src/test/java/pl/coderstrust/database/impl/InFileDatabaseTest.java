package pl.coderstrust.database.impl;

import static org.junit.Assert.assertEquals;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH_TO_PURGE;

import org.junit.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;
import pl.coderstrust.model.InvoiceBuilder;
import pl.coderstrust.model.counterparty.CounterpartyBuilder;
import pl.coderstrust.model.invoiceItem.InvoiceItemBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class InFileDatabaseTest extends DatabaseTest {

  @Override
  protected Database provideDatabase() {
    return new InFileDatabase(DATABASE_TEST_PATH);
  }

  @Test
  public void shouldReturnNextInvoiceIdInFileDatabase() throws Exception {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
    InFileDatabase database = new InFileDatabase(DATABASE_TEST_PATH);
    InvoiceBook invoiceBook = new InvoiceBook(database);
    int numberOfInvoices = 10;
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", numberOfInvoices);
    invoices.forEach(invoiceBook::addInvoice);

    //then
    for (int i = 1; i < numberOfInvoices; i++) {
      int actualId = database.getNextInvoiceId();
      invoiceBook.addInvoice(InvoiceBuilder.builder()
          .withId(actualId)
          .withDate(LocalDate.now())
          .withCounterparty(CounterpartyBuilder.builder().withCompanyName("").build())
          .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
              .builder()
              .withDescription("something")
              .withAmount(BigDecimal.TEN)
              .withVatAmount(BigDecimal.ONE)
              .build()))
          .build());
      assertEquals(numberOfInvoices + i, actualId);
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

}