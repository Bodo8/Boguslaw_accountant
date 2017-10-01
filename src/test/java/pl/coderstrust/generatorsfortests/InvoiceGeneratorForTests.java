package pl.coderstrust.generatorsfortests;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBuilder;
import pl.coderstrust.model.counterparty.Counterparty;
import pl.coderstrust.model.counterparty.CounterpartyBuilder;
import pl.coderstrust.model.invoiceItem.InvoiceItem;
import pl.coderstrust.model.invoiceItem.InvoiceItemBuilder;
import pl.coderstrust.model.invoiceItem.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceGeneratorForTests {

  public static Invoice generateOneInvoiceWithoutId(LocalDate date, String counterpartyName,
      String description, BigDecimal amount, BigDecimal vatAmount) {
    return new Invoice(date,
        CounterpartyBuilder.builder().withCompanyName(counterpartyName).build(),
        Arrays.asList(new InvoiceItem(description, 1, amount, vatAmount, Vat.VAT_23)));
  }

  public static Invoice generateOneInvoice() {
    return InvoiceBuilder.builder()
        .withId(1)
        .withDate(LocalDate.now())
        .withCounterparty(CounterpartyBuilder.builder().withCompanyName("LPT").build())
        .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
            .builder()
            .withDescription("Petrol")
            .withAmount(BigDecimal.TEN)
            .withVatAmount(BigDecimal.ONE)
            .build()))
        .build();
  }

  public static Invoice generateOneInvoice(LocalDate date, String counterpartyName,
      String description, BigDecimal amount, BigDecimal vatAmount) {
    return InvoiceBuilder.builder()
        .withId(1)
        .withDate(date)
        .withCounterparty(CounterpartyBuilder.builder().withCompanyName(counterpartyName).build())
        .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
            .builder()
            .withDescription(description)
            .withAmount(amount)
            .withVatAmount(vatAmount)
            .build()))
        .build();
  }

  public static Invoice generateOneInvoice(LocalDate date, Counterparty counterparty,
      String description, BigDecimal amount, BigDecimal vatAmount) {
    return InvoiceBuilder.builder()
        .withId(1)
        .withDate(date)
        .withCounterparty(counterparty)
        .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
            .builder()
            .withDescription(description)
            .withAmount(amount)
            .withVatAmount(vatAmount)
            .build()))
        .build();
  }

  /**
   * @param counterpartyName - counterpartyName.
   * @param description - description.
   * @param numberOfInvoices - numberOfInvoices.
   * @return - list of example invoices.
   */
  public static List<Invoice> generateListOfInvoices(
      String counterpartyName, String description, int numberOfInvoices) {

    List<Invoice> list = new ArrayList<>();
    LocalDate fromDate = LocalDate.of(2015, 1, 1);
    for (int i = 1; i <= numberOfInvoices; i++) {
      LocalDate date = DateGeneratorForTests.generateDateFromGivenDateTillCurrentDate(fromDate);
      list.add(InvoiceBuilder.builder()
          .withId(i)
          .withDate(date)
          .withCounterparty(CounterpartyBuilder.builder().withCompanyName(counterpartyName).build())
          .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
              .builder()
              .withDescription(description)
              .withAmount(BigDecimal.TEN)
              .withVatAmount(BigDecimal.ONE)
              .build()))
          .build());
    }
    return list.stream().sorted().collect(Collectors.toList());
  }

  public static List<Invoice> generateListOfInvoicesFromApril2017() {
    List<Invoice> list = new ArrayList<>();
    for (int i = 1; i <= 20; i++) {
      list.add(InvoiceBuilder.builder()
          .withId(i)
          .withDate(LocalDate.of(2017, 4, i))
          .withCounterparty(CounterpartyBuilder.builder().withCompanyName("").build())
          .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
              .builder()
              .withDescription("")
              .withAmount(BigDecimal.TEN)
              .withVatAmount(BigDecimal.ONE)
              .build()))
          .build());
    }
    return list.stream().sorted().collect(Collectors.toList());
  }

  /**
   * @param fromDate - LocalDate.
   * @param numberOfInvoices - number of invoices.
   * @return - list of invoices.
   */
  public static List<Invoice> generateListOfInvoices(LocalDate fromDate,
      int numberOfInvoices) {
    List<Invoice> list = new ArrayList<>();
    if (fromDate.isAfter(LocalDate.now()) || fromDate.equals(LocalDate.now())) {
      fromDate = LocalDate.of(2010, 1, 1);
    }
    for (int i = 1; i <= numberOfInvoices; i++) {
      LocalDate date = DateGeneratorForTests.generateDateFromGivenDateTillCurrentDate(fromDate);
      list.add(InvoiceBuilder.builder()
          .withId(i)
          .withDate(date)
          .withCounterparty(CounterpartyBuilder.builder().withCompanyName("").build())
          .withInvoiceItems(Arrays.asList(InvoiceItemBuilder
              .builder()
              .withDescription("")
              .withAmount(BigDecimal.TEN)
              .withVatAmount(BigDecimal.ONE)
              .build()))
          .build());
    }
    return list.stream().sorted().collect(Collectors.toList());
  }

}
