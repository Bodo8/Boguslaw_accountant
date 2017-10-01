package pl.coderstrust.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH_TO_PURGE;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.getFirstDayOfMonthInYear;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.getLastDayOfMonthInYear;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.isDateFromGivenPeriod;
import static pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests.generateOneInvoice;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.impl.InFileDatabase;
import pl.coderstrust.database.impl.InMemoryDatabase;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.visitor.IncomeCalculator;
import pl.coderstrust.visitor.InvoiceBookCalculator;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnitParamsRunner.class)
public class InvoiceBookTest {

  @Test
  public void shouldReturnEmptyListOfInvoicesIfNothingWasAdded() throws Exception {
    //given
    final Database database = mock(Database.class);
    //when
    InvoiceBook invoiceBook = new InvoiceBook(database);
    //then
    assertEquals(0, invoiceBook.getInvoices().size());
  }

  @Test
  public void shouldReturnOneInvoiceWhichWasAddedToInvoiceBook() throws Exception {
    //given
    Database database = mock(Database.class);
    final InvoiceBook invoiceBook = new InvoiceBook(database);
    ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
    Invoice invoice = generateOneInvoice();
    //when
    invoiceBook.addInvoice(invoice);
    doThrow(new RuntimeException()).when(database).saveInvoice(any(Invoice.class));
    //then
    verify(database).saveInvoice(captor.capture());
    verify(database, timeout(1)).saveInvoice(any(Invoice.class));
    assertEquals(invoice, captor.getValue());
  }

  @Test
  public void shouldCheckIfInvoicesAreSavedInInvoiceBook() throws Exception {
    //given
    Database database = mock(Database.class);
    final InvoiceBook invoiceBook = new InvoiceBook(database);
    ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);

    List<Invoice> expectedList = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 100);
    //when
    for (Invoice invoice : expectedList) {
      invoiceBook.addInvoice(invoice);
    }
    Collections.sort(expectedList);
    verify(database, times(expectedList.size())).saveInvoice(captor.capture());
    List<Invoice> actualList = captor.getAllValues();
    Collections.sort(actualList);
    //then
    assertNotNull(database);
    verify(database, times(expectedList.size())).saveInvoice(any(Invoice.class));
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  @Test
  public void shouldReturnInvoicesFromInvoiceBook() throws Exception {
    //given
    Database database = mock(Database.class);
    final InvoiceBook invoiceBook = new InvoiceBook(database);
    List<Invoice> expectedList = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 10);
    //when
    for (Invoice invoice : expectedList) {
      invoiceBook.addInvoice(invoice);
    }
    Collections.sort(expectedList);
    //when
    when(invoiceBook.getInvoices()).thenReturn(expectedList);
    List<Invoice> actualList = invoiceBook.getInvoices();
    //then
    assertNotNull(actualList);
    assertEquals(expectedList.size(), actualList.size());
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  @Test
  public void shouldReturnInvoicesWhichWereAddedToInvoiceBook() throws Exception {
    //given
    Database database = mock(Database.class);
    final InvoiceBook invoiceBook = new InvoiceBook(database);
    List<Invoice> expectedList = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", 100);
    //when
    for (Invoice invoice : expectedList) {
      invoiceBook.addInvoice(invoice);
    }
    Collections.sort(expectedList);
    when(database.getInvoices()).thenReturn(expectedList);
    //then
    List<Invoice> actualList = invoiceBook.getInvoices();
    verify(database, times(1)).getInvoices();
    assertEquals(expectedList.size(), actualList.size());
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  @Test
  public void shouldReturnInvoiceBookAsString() {
    //given
    Database database = mock(Database.class);
    ArgumentCaptor captor = ArgumentCaptor.forClass(String.class);
    InvoiceBook invoiceBook = new InvoiceBook(database);
    String expectedStringFromDatabase = "Date from pl.coderstrust.database.";
    String expected = "InvoiceBook{database=" + expectedStringFromDatabase + "}";
    //when
    when(database.toString()).thenReturn(expectedStringFromDatabase);
    String actual = invoiceBook.toString();
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldCheckIfVisitorIsAccepted() {
    //given
    Database database = mock(Database.class);
    InvoiceBook invoiceBook = new InvoiceBook(database);
    InvoiceBookCalculator invoiceBookCalculator = invoiceBook;
    IncomeCalculator incomeCalculator = new IncomeCalculator();
    try {
      invoiceBookCalculator.accept(incomeCalculator);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void shouldReturnInvoiceById() throws IOException {
    //given
    Database database = mock(Database.class);
    InvoiceBook invoiceBook = new InvoiceBook(database);
    ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
    int numberOfInvoices = 20;
    List<Invoice> expectedList = InvoiceGeneratorForTests
        .generateListOfInvoices("", "", numberOfInvoices);
    Collections.sort(expectedList);
    //when
    for (Invoice invoice : expectedList) {
      invoiceBook.addInvoice(invoice);
    }
    verify(database, times(expectedList.size())).saveInvoice(captor.capture());
    when(database.getInvoices()).thenReturn(captor.getAllValues());
    List<Invoice> actualList = new ArrayList<>();
    for (int i = 0; i < expectedList.size(); i++) {
      actualList.add(invoiceBook.getInvoiceById(i + 1));
    }
    Collections.sort(actualList);
    //then
    assertEquals(expectedList.size(), actualList.size());
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  @Test
  public void shouldReturnListOfInvoicesFromGivenYearAndMonth() {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    InvoiceBook invoiceBook = new InvoiceBook(new InFileDatabase(DATABASE_TEST_PATH));
    Month month = Month.APRIL;
    Year year = Year.of(2017);
    LocalDate dateForInitialising = LocalDate.of(year.getValue(), 1, 1);
    List<Invoice> generatedListOfInvoices = InvoiceGeneratorForTests
        .generateListOfInvoices(dateForInitialising, 10);
    List<Invoice> expectedListOfInvoices = generatedListOfInvoices
        .stream()
        .filter(invoice -> isDateFromGivenPeriod(invoice.getDate(),
            getFirstDayOfMonthInYear(year, month),
            getLastDayOfMonthInYear(year, month)))
        .collect(Collectors.toList());

    for (Invoice invoice : generatedListOfInvoices) {
      invoiceBook.addInvoice(invoice);
    }
    //when
    final List<Invoice> actualListOfInvoices =
        invoiceBook.getInvoices(year, month).stream()
            .sorted()
            .collect(
                Collectors.toList());
    //then
    assertNotNull("List of invoices should not be null.", actualListOfInvoices);
    assertEquals(expectedListOfInvoices.size(), actualListOfInvoices.size());
    for (int i = 0; i < expectedListOfInvoices.size(); i++) {
      assertEquals(expectedListOfInvoices.get(i), actualListOfInvoices.get(i));
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  @Test
  public void shouldReturnListOfInvoicesFromGivenPeriod() {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));

    InvoiceBook invoiceBook = new InvoiceBook(new InFileDatabase(DATABASE_TEST_PATH));
    LocalDate fromDate = LocalDate.of(2016, 2, 1);
    LocalDate toDate = LocalDate.of(2017, 2, 17);
    LocalDate dateForInitialising = LocalDate.of(2017, 1, 1);

    List<Invoice> generatedListOfInvoices = InvoiceGeneratorForTests
        .generateListOfInvoices(dateForInitialising, 10);
    List<Invoice> expectedListOfInvoices = generatedListOfInvoices
        .stream()
        .filter(invoice
            -> isDateFromGivenPeriod(invoice.getDate(), fromDate, toDate))
        .sorted()
        .collect(Collectors.toList());

    for (Invoice invoice : generatedListOfInvoices) {
      invoiceBook.addInvoice(invoice);
    }
    //when
    final List<Invoice> actualListOfInvoices =
        invoiceBook.getInvoices(fromDate, toDate);
    //then
    assertNotNull("List of invoices should not be null.", actualListOfInvoices);
    assertEquals(expectedListOfInvoices.size(), actualListOfInvoices.size());
    for (int i = 0; i < expectedListOfInvoices.size(); i++) {
      assertEquals(expectedListOfInvoices.get(i), actualListOfInvoices.get(i));
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  @Test
  public void shouldDeleteInvoiceFromDatabase() throws IOException {
    //given
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
    Database database = new InMemoryDatabase();
    final InvoiceBook invoiceBook = new InvoiceBook(database);
    Invoice invoice = generateOneInvoice();
    database.saveInvoice(invoice);

    //when
    invoiceBook.removeInvoice(invoice.getId());

    //then
    assertEquals(0, invoiceBook.getInvoices().size());
  }
}