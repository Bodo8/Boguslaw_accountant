package pl.coderstrust.generatorsfortests;

import pl.coderstrust.controller.InvoiceBody;
import pl.coderstrust.model.counterparty.CounterpartyBuilder;
import pl.coderstrust.model.invoiceItem.InvoiceItem;
import pl.coderstrust.model.invoiceItem.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class InvoiceBodyGenerator {

  public static InvoiceBody generateOneInvoiceBody() {
    return new InvoiceBody(LocalDate.now(),
        CounterpartyBuilder.builder().withCompanyName("").build(),
        Arrays.asList(new InvoiceItem("", 1, BigDecimal.TEN,
            BigDecimal.ONE, Vat.VAT_23)));
  }

  public static InvoiceBody generateOneInvoiceBody(LocalDate date, String counterpartyName,
      String description, BigDecimal amount, BigDecimal vatAmount) {
    return new InvoiceBody(date,
        CounterpartyBuilder.builder().withCompanyName(counterpartyName).build(),
        Arrays.asList(new InvoiceItem(description, 1, amount, vatAmount, Vat.VAT_23)));
  }

}
