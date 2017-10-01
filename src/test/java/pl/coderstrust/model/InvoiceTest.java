package pl.coderstrust.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.counterparty.AddressBuilder;
import pl.coderstrust.model.counterparty.Counterparty;
import pl.coderstrust.model.counterparty.CounterpartyBuilder;
import pl.coderstrust.model.invoiceItem.InvoiceItemBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class InvoiceTest {

  @Test
  public void shouldReturnNullInvoice() throws Exception {
    //when
    Invoice invoice = InvoiceBuilder
        .builder()
        .build();
    //then
    assertNotNull(invoice);
  }

  @Test
  public void shouldReturnInvoice() throws Exception {
    LocalDate date = LocalDate.now();
    Counterparty counterparty = CounterpartyBuilder.builder()
        .withCompanyName("LPT")
        .withAddress(AddressBuilder.builder()
            .withZipCode("97-420")
            .withTownName("Lodz")
            .withStreetName("Piotrkowska")
            .withHouseNumber("34")
            .build())
        .withPhoneNumber("345678912")
        .withNIP("")
        .withBankName("PKO")
        .withBankNumber("2345")
        .build();
    //when
    Invoice invoice = InvoiceBuilder.builder()
        .withId(1)
        .withDate(date)
        .withCounterparty(counterparty)
        .withInvoiceItems(Arrays.asList(
            InvoiceItemBuilder.builder()
                .withDescription("Item1")
                .withAmount(BigDecimal.TEN)
                .withVatAmount(BigDecimal.ONE)
                .build()))
        .build();
    //then
    assertNotNull(invoice);
    int actualId = invoice.getId();
    assertEquals(1, actualId);
    assertEquals(date, invoice.getDate());
    assertEquals(counterparty, invoice.getCounterparty());
    assertEquals("Item1", invoice.getInvoiceItems().get(0).getDescription());
    assertEquals(BigDecimal.TEN, invoice.getInvoiceItems().get(0).getAmount());
    assertEquals(BigDecimal.ONE, invoice.getInvoiceItems().get(0).getVatAmount());
  }

  @Test
  public void shouldGetId() throws Exception {
    //given
    Invoice invoice = InvoiceGeneratorForTests
        .generateOneInvoice();
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(LocalDate.now(), 10);
    //when
    int actualId = invoice.getId();
    List<Integer> actualIds = invoices
        .stream().map(invoice1 -> invoice1.getId())
        .sorted()
        .collect(Collectors.toList());
    //then
    assertEquals(1, actualId);
    for (int i = 1; i <= invoices.size(); i++) {
      actualId = actualIds.get(i - 1);
      assertEquals(i, actualId);
    }
  }

  @Test
  public void shouldGetDate() throws Exception {
    //given
    LocalDate expectedDate = LocalDate.now();
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(
        expectedDate, "", "", BigDecimal.TEN, BigDecimal.ONE);
    //when
    LocalDate actualDate = invoice.getDate();
    //then
    assertEquals(expectedDate, actualDate);
  }

  @Test
  public void shouldGetCounterparty() throws Exception {
    //given
    Counterparty expectedCounterparty = CounterpartyBuilder.builder()
        .withCompanyName("LPT")
        .withAddress(AddressBuilder.builder()
            .withZipCode("97-420")
            .withTownName("Lodz")
            .withStreetName("Piotrkowska")
            .withHouseNumber("34")
            .build())
        .withPhoneNumber("345678912")
        .withNIP("")
        .withBankName("PKO")
        .withBankNumber("2345")
        .build();
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(
        LocalDate.now(), expectedCounterparty, "", BigDecimal.TEN, BigDecimal.ONE);
    //when
    Counterparty actualCounterparty = invoice.getCounterparty();
    //then
    assertEquals(expectedCounterparty, actualCounterparty);
    assertEquals(expectedCounterparty.getCompanyName(), actualCounterparty.getCompanyName());
    assertEquals(expectedCounterparty.getNip(), actualCounterparty.getNip());
    assertEquals(expectedCounterparty.getAddress(), actualCounterparty.getAddress());
    assertEquals(expectedCounterparty.getBankName(), actualCounterparty.getBankName());
    assertEquals(expectedCounterparty.getBankNumber(), actualCounterparty.getBankNumber());
    assertEquals(expectedCounterparty.getPhoneNumber(), actualCounterparty.getPhoneNumber());
  }

  @Test
  public void shouldGetDescription() throws Exception {
    //given
    String expectedDescription = "Invoice nr 1";
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(
        LocalDate.now(), "LPT", expectedDescription, BigDecimal.TEN, BigDecimal.ONE);
    //when
    String actualDescription = invoice.getInvoiceItems().get(0).getDescription();
    //then
    assertEquals(expectedDescription, actualDescription);
  }

  @Test
  public void shouldGetAmount() throws Exception {
    //given
    BigDecimal expectedAmount = BigDecimal.TEN;
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(
        LocalDate.now(), "LPT", "", expectedAmount, BigDecimal.ONE);
    //when
    BigDecimal actualAmount = invoice.getInvoiceItems().get(0).getAmount();
    //then
    assertEquals(expectedAmount, actualAmount);
  }

  @Test
  public void shouldGetVatAmount() throws Exception {
    //given
    BigDecimal expectedVat = BigDecimal.ONE;
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(
        LocalDate.now(), "LPT", "", BigDecimal.TEN, expectedVat);
    //when
    BigDecimal actualVat = invoice.getInvoiceItems().get(0).getVatAmount();
    //then
    assertEquals(expectedVat, actualVat);
  }

  @Test
  public void shouldCheckIfInvoicesAreEqual() throws Exception {
    //given
    Invoice invoice1 = InvoiceGeneratorForTests
        .generateOneInvoice();
    Invoice invoice2 = InvoiceGeneratorForTests.generateOneInvoice();
    Boolean expected = true;
    //when
    Boolean actual = invoice1.equals(invoice2);
    //then
    assertEquals(expected, actual);
    assertEquals(invoice1.equals(invoice2), invoice2.equals(invoice1));
    assertEquals(false, invoice1.equals(null));
  }

  @Test
  public void shouldCheckIfInvoicesAreNotEqual() throws Exception {
    //given
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(LocalDate.now(), 2);
    Boolean expected = true;
    //when
    Boolean actual = invoices.get(0).equals(invoices.get(1));
    //then
    assertNotEquals(expected, invoices.get(0).equals(null));
    assertNotEquals(expected, invoices.get(1).equals(null));
    assertNotEquals(expected, actual);
  }

  @Test
  public void shouldReturnHashCode() throws Exception {
    //given
    Invoice invoice = InvoiceGeneratorForTests
        .generateOneInvoice();
    Integer id = invoice.getId();
    int expectedHashCode = 31 * (31 * id.hashCode() + invoice.getDate().hashCode())
        + invoice.getCounterparty().hashCode();
    //when
    int actualHashCode = invoice.hashCode();
    //then
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
  public void shouldCompareInvoices() throws Exception {
    //given
    LocalDate date = LocalDate.of(2017, 3, 12);
    List<Invoice> invoices = InvoiceGeneratorForTests
        .generateListOfInvoices(date, 2);
    Invoice invoice3 = null;
    int expected = -1;
    //when
    int actual = invoices.get(0).compareTo(invoices.get(1));

    //then
    assertEquals(expected, actual);
    assertEquals(-1, invoices.get(0).compareTo(null));
    assertEquals(-1, invoices.get(1).compareTo(null));
    assertEquals(Math.abs(invoices.get(0).compareTo(invoices.get(1))),
        Math.abs(invoices.get(1).compareTo(invoices.get(0))));
  }

  @Test
  public void shouldPrintOutInvoiceAsString() throws Exception {
    //given
    Invoice invoice = InvoiceGeneratorForTests
        .generateOneInvoice();
    String expected = String.valueOf(invoice);
    //when
    String actual = invoice.toString();
    //then
    assertEquals(expected, actual);
  }

}