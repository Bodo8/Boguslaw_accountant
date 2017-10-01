package pl.coderstrust.model;

import static org.junit.Assert.assertEquals;
import static pl.coderstrust.generatorsfortests.InvoiceBodyGenerator.generateOneInvoiceBody;
import static pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests.generateOneInvoice;
import static pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests.generateOneInvoiceWithoutId;

import org.junit.Test;
import pl.coderstrust.controller.InvoiceBody;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceCreatorTest {

  @Test
  public void shouldCreateInvoiceWithId() {
    //given
    InvoiceBody invoiceBody = generateOneInvoiceBody(LocalDate.now(),
        "", "", BigDecimal.TEN, BigDecimal.ONE);
    int id = 2372;
    Invoice givenInvoice = generateOneInvoice(LocalDate.now(),
        "", "", BigDecimal.TEN, BigDecimal.ONE);
    givenInvoice.setId(id);

    //when
    Invoice actualInvoice = InvoiceBuilder.builder().buildInvoiceWithGeneratedId(invoiceBody, id);

    //then
    assertEquals(givenInvoice, actualInvoice);
  }

  @Test
  public void shouldCreateInvoiceWithoutId() {
    //given
    InvoiceBody invoiceBody = generateOneInvoiceBody(LocalDate.now(),
        "", "", BigDecimal.TEN, BigDecimal.ONE);
    Invoice givenInvoice = generateOneInvoiceWithoutId(LocalDate.now(),
        "", "", BigDecimal.TEN, BigDecimal.ONE);

    //when
    Invoice actualInvoice = InvoiceBuilder.builder().buildInvoiceWithoutId(invoiceBody);

    //then
    assertEquals(givenInvoice, actualInvoice);
  }
}
