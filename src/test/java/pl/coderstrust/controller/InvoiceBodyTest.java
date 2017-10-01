package pl.coderstrust.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import pl.coderstrust.model.counterparty.Counterparty;
import pl.coderstrust.model.counterparty.CounterpartyBuilder;
import pl.coderstrust.model.invoiceItem.InvoiceItem;
import pl.coderstrust.model.invoiceItem.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class InvoiceBodyTest {

  @Test
  public void shouldReturnInvoiceBody() throws Exception {
    //given
    LocalDate expectedDate = LocalDate.now();
    Counterparty expectedCounterparty
        = CounterpartyBuilder.builder().withCompanyName("LPD").build();
    int expectedNumberOfItem = 1;
    String expectedDescription = "Petrol";
    BigDecimal expectedAmount = BigDecimal.TEN;
    BigDecimal expectedVatAmount = BigDecimal.ONE;
    Vat expectedVat = Vat.VAT_23;
    //when
    InvoiceBody invoiceBody = new InvoiceBody(expectedDate, expectedCounterparty, Arrays.asList(
        new InvoiceItem(expectedDescription, expectedNumberOfItem, expectedAmount,
            expectedVatAmount, expectedVat)));
    //Then
    assertNotNull(invoiceBody);
    assertEquals(expectedDate, invoiceBody.getDate());
    assertEquals(expectedCounterparty, invoiceBody.getCounterparty());
    assertEquals(expectedDescription, invoiceBody.getInvoiceItems().get(0).getDescription());
    assertEquals(expectedNumberOfItem, invoiceBody.getInvoiceItems().get(0).getNumberOfItems());
    assertEquals(expectedAmount, invoiceBody.getInvoiceItems().get(0).getAmount());
    assertEquals(expectedVatAmount, invoiceBody.getInvoiceItems().get(0).getVatAmount());
    assertEquals(expectedVat, invoiceBody.getInvoiceItems().get(0).getVat());
  }

  @Test
  public void shouldGetDate() throws Exception {
    //given
    LocalDate expected = LocalDate.now();
    Counterparty expectedCounterparty
        = CounterpartyBuilder.builder().withCompanyName("LPD").build();
    InvoiceBody invoiceBody = new InvoiceBody(expected, expectedCounterparty,
        Arrays.asList(new InvoiceItem("Petrol", 1, BigDecimal.TEN,
            BigDecimal.ONE, Vat.VAT_23)));
    //when
    LocalDate actual = invoiceBody.getDate();
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldGetCounterparty() throws Exception {
    //given
    String expected = "LPD";
    Counterparty expectedCounterparty
        = CounterpartyBuilder.builder().withCompanyName("LPD").build();
    InvoiceBody invoiceBody = new InvoiceBody(LocalDate.now(), expectedCounterparty,
        Arrays.asList(new InvoiceItem("Petrol", 1, BigDecimal.TEN,
            BigDecimal.ONE, Vat.VAT_23)));
    //when
    String actual = invoiceBody.getCounterparty().getCompanyName();
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldGetDescription() throws Exception {
    String expected = "Petrol";
    Counterparty expectedCounterparty
        = CounterpartyBuilder.builder().withCompanyName("LPD").build();
    InvoiceBody invoiceBody = new InvoiceBody(LocalDate.now(), expectedCounterparty, Arrays.asList(
        new InvoiceItem(expected, 1, BigDecimal.TEN, BigDecimal.ONE, Vat.VAT_23)));
    //when
    String actual = invoiceBody.getInvoiceItems().get(0).getDescription();
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldGetAmount() throws Exception {
    BigDecimal expected = BigDecimal.TEN;
    Counterparty expectedCounterparty
        = CounterpartyBuilder.builder().withCompanyName("LPD").build();
    InvoiceBody invoiceBody = new InvoiceBody(LocalDate.now(), expectedCounterparty, Arrays.asList(
        new InvoiceItem("", 1, expected, BigDecimal.ONE, Vat.VAT_23)));
    //when
    BigDecimal actual = invoiceBody.getInvoiceItems().get(0).getAmount();
    //then
    assertEquals(expected, actual);
  }

  @Test
  public void shouldGetVatAmount() throws Exception {
    BigDecimal expected = BigDecimal.ONE;
    Counterparty expectedCounterparty
        = CounterpartyBuilder.builder().withCompanyName("LPD").build();
    InvoiceBody invoiceBody = new InvoiceBody(LocalDate.now(), expectedCounterparty,
        Arrays.asList(new InvoiceItem("", 1, BigDecimal.TEN, expected, Vat.VAT_23)));
    //when
    BigDecimal actual = invoiceBody.getInvoiceItems().get(0).getVatAmount();
    //then
    assertEquals(expected, actual);
  }

}

