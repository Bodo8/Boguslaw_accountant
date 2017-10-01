package pl.coderstrust.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH_TO_PURGE;

import org.junit.Test;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DatabaseTest {

  protected abstract Database provideDatabase();

  @Test
  public void shouldSaveOneInvoice() throws IOException {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    Database database = provideDatabase();
    Invoice invoice = InvoiceGeneratorForTests
        .generateOneInvoice();
    //when
    database.saveInvoice(invoice);
    //then
    List<Invoice> actual = database.getInvoices();
    assertNotNull(actual);
    assertEquals(invoice, actual.get(0));

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  @Test
  public void shouldSaveInvoicesIntoDatabase() throws IOException {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    Database database = provideDatabase();

    List<Invoice> expectedListOfInvoices = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 100)
        .stream()
        .sorted()
        .collect(Collectors.toList());
    //when
    for (Invoice invoice : expectedListOfInvoices) {
      database.saveInvoice(invoice);
    }
    //then
    assertNotNull("Database should not be empty ", database.getInvoices());
    assertEquals(expectedListOfInvoices.size(), database.getInvoices().size());
    for (int i = 0; i < expectedListOfInvoices.size(); i++) {
      assertEquals(expectedListOfInvoices.get(i), database.getInvoices().get(i));
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  @Test
  public void shouldRemoveInvoiceFromDatabase() throws IOException {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    Database database = provideDatabase();

    List<Invoice> expectedInvoices = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 10)
        .stream()
        .sorted()
        .collect(Collectors.toList());
    expectedInvoices.stream().forEach(database::saveInvoice);

    // when
    Invoice invoice;
    int sizeOfList = expectedInvoices.size();
    for (int i = 0; i < sizeOfList; i++) {
      invoice = database.getInvoices().get(i);
      database.removeInvoice(invoice);
      sizeOfList--;
      assertEquals(database.getInvoices().size(), sizeOfList);
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  @Test
  public void shouldReadInvoiceWhichWasSavedInDatabase() throws IOException {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    Database database = provideDatabase();
    Invoice invoice = InvoiceGeneratorForTests
        .generateOneInvoice();
    //when
    database.saveInvoice(invoice);
    //then
    final List<Invoice> listOfInvoices = database.getInvoices();
    assertNotNull("List of invoices should not be null.", listOfInvoices);
    assertEquals(1, listOfInvoices.size());
    assertEquals(invoice, listOfInvoices.get(0));

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  @Test
  public void shouldReturnListOfInvoicesFromDatabase() throws IOException {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    Database database = provideDatabase();

    List<Invoice> expectedListOfInvoices = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 10);
    for (Invoice invoice : expectedListOfInvoices) {
      database.saveInvoice(invoice);
    }
    //when
    final List<Invoice> actualListOfInvoices = database.getInvoices();
    //then
    assertNotNull("List of invoices should not be null.", actualListOfInvoices);
    assertEquals(expectedListOfInvoices.size(), actualListOfInvoices.size());
    for (int i = 0; i < expectedListOfInvoices.size(); i++) {
      assertEquals(expectedListOfInvoices.get(i), actualListOfInvoices.get(i));
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }
}