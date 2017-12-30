package pl.coderstrust.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests.generateOneInvoice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.coderstrust.emailprocessor.EmailSender;
import pl.coderstrust.generatorsfortests.InvoiceBodyGenerator;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class InvoiceBookServiceTest {

  @InjectMocks
  private InvoiceBookService service;

  @Mock
  InvoiceBook invoiceBook;

  @Mock
  EmailSender emailSender;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void shouldReturnInvoiceBook() {
    //when
    new InvoiceBookService(invoiceBook, emailSender);
    //then
    assertThat(invoiceBook).isNotNull();
  }

  @Test
  public void shouldReturnSingleInvoice() throws Exception {
    //given
    Invoice invoice = generateOneInvoice();
    int expectedId = invoice.getId();
    when(invoiceBook.getInvoiceById(expectedId)).thenReturn(invoice);

    //when
    Invoice actualInvoice = service.getSingleInvoice(expectedId);

    //then
    verify(invoiceBook).getInvoiceById(expectedId);
    assertEquals(expectedId, actualInvoice.getId());
  }

  @Test
  public void shouldReturnInvoicesFromGivenMonth() throws Exception {
    //given
    List<Invoice> invoices = InvoiceGeneratorForTests.generateListOfInvoicesFromApril2017();
    Month expectedMonth = Month.APRIL;
    Year expectedYear = Year.of(2017);
    when(service.getInvoicesFromGivenMonth(2017, "April")).thenReturn(invoices);
    when(invoiceBook.getInvoices(expectedYear, expectedMonth)).thenReturn(invoices);

    //when
    List<Invoice> actualInvoices = service.getInvoicesFromGivenMonth(2017, "April");

    //then
    verify(invoiceBook).getInvoices(expectedYear, expectedMonth);
  }

  @Test
  public void shouldReturnInvoices() throws Exception {
    //given
    List<Invoice> invoices = InvoiceGeneratorForTests.generateListOfInvoices(
        LocalDate.of(2015, 1, 1), 100);
    when(invoiceBook.getInvoices()).thenReturn(invoices);

    //when
    List<Invoice> actualInvoices = service.getInvoices();

    //then
    verify(invoiceBook).getInvoices();
    assertEquals(invoices, actualInvoices);
  }

  @Test
  public void shouldPostInvoice() throws Exception {
    //given
    int expectedInvoiceId = 1678;
    Invoice invoice = new Invoice();
    invoice.setId(expectedInvoiceId);
    InvoiceBody invoiceBody = new InvoiceBody();
    when(invoiceBook.createInvoice(invoiceBody)).thenReturn(invoice);
    doNothing().when(emailSender).sendEmail("adam.nowak.coder@gmail.com", invoice);

    //when
    int actualId = service.postInvoice(invoiceBody);

    //then
    assertEquals(expectedInvoiceId, actualId);
    verify(invoiceBook).createInvoice(invoiceBody);
    verify(invoiceBook).addInvoice(invoice);
    verify(emailSender).sendEmail("adam.nowak.coder@gmail.com", invoice);

  }

  @Test
  public void shouldPutInvoice() throws Exception {
    //given
    int expectedId = 1;
    InvoiceBody invoiceBody = invoiceBody = InvoiceBodyGenerator.generateOneInvoiceBody(
        LocalDate.now(), "LPD", "Petrol", BigDecimal.TEN, BigDecimal.ONE);

    Invoice invoice = generateOneInvoice(invoiceBody.getDate(),
        invoiceBody.getCounterparty().getCompanyName(),
        invoiceBody.getInvoiceItems().get(0).getDescription(),
        invoiceBody.getInvoiceItems().get(0).getAmount(),
        invoiceBody.getInvoiceItems().get(0).getVatAmount());

    List<Invoice> invoices = new ArrayList<>();
    invoices.add(invoice);
    when(invoiceBook.getInvoices()).thenReturn(invoices);
    when(invoiceBook.createInvoice(invoiceBody)).thenReturn(invoice);

    //when
    service.putInvoice(expectedId, invoiceBody);

    //then
    verify(invoiceBook).getInvoices();
    verify(invoiceBook).createInvoice(invoiceBody);
    verify(invoiceBook).addInvoice(invoice);
  }


  @Test
  public void shouldDeleteInvoice() throws Exception {
    //given
    int id = 1;
    Invoice invoice = generateOneInvoice();
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(invoice);
    when(invoiceBook.getInvoices()).thenReturn(invoices);

    //when
    Boolean actual = service.deleteInvoice(id);

    //then
    assertEquals(true, actual);
  }
}