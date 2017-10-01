package pl.coderstrust.model;

import pl.coderstrust.controller.InvoiceBody;
import pl.coderstrust.model.counterparty.AddressBuilder;
import pl.coderstrust.model.counterparty.Counterparty;
import pl.coderstrust.model.counterparty.CounterpartyBuilder;
import pl.coderstrust.model.invoiceItem.InvoiceItem;
import pl.coderstrust.model.invoiceItem.InvoiceItemBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvoiceBuilder {

  private int id;
  private LocalDate date = LocalDate.now();
  private Counterparty counterparty;
  private List<InvoiceItem> invoiceItems;

  public static InvoiceBuilder builder() {
    return new InvoiceBuilder();
  }

  public InvoiceBuilder withId(int id) {
    this.id = id;
    return this;
  }

  public InvoiceBuilder withDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public InvoiceBuilder withCounterparty(Counterparty counterparty) {
    this.counterparty = counterparty;
    return this;
  }

  public InvoiceBuilder withInvoiceItems(List<InvoiceItem> invoiceItems) {
    this.invoiceItems = invoiceItems;
    return this;
  }

  public InvoiceBuilder withInvoiceItems(InvoiceItemBuilder... invoiceItemBuilders) {
    this.invoiceItems = new ArrayList<>();
    for (InvoiceItemBuilder invoiceItemBuilder : invoiceItemBuilders) {
      this.invoiceItems.add(invoiceItemBuilder.build());
    }
    return this;
  }

  public Invoice build() {
    return new Invoice(id, date, counterparty, invoiceItems);
  }


  public Invoice buildInvoiceWithGeneratedId(InvoiceBody invoiceBody, int id) {
    return InvoiceBuilder
        .builder()
        .withId(id)
        .withDate(invoiceBody.getDate())
        .withCounterparty(CounterpartyBuilder
            .builder()
            .withCompanyName(invoiceBody.getCounterparty().getCompanyName())
            .withBankName(invoiceBody.getCounterparty().getBankName())
            .withBankNumber(invoiceBody.getCounterparty().getBankNumber())
            .withNIP(invoiceBody.getCounterparty().getNip())
            .withPhoneNumber(invoiceBody.getCounterparty().getPhoneNumber())
            .withAddress(AddressBuilder
                .builder()
                .withHouseNumber(invoiceBody.getCounterparty().getAddress().getHouseNumber())
                .withStreetName(invoiceBody.getCounterparty().getAddress().getStreetName())
                .withZipCode(invoiceBody.getCounterparty().getAddress().getZipCode())
                .withTownName(invoiceBody.getCounterparty().getAddress().getTownName())
                .build())
            .build())
        .withInvoiceItems(Arrays.asList(
            InvoiceItemBuilder
                .builder()
                .withDescription(invoiceBody.getInvoiceItems().get(0).getDescription())
                .withNumberOfItem(invoiceBody.getInvoiceItems().get(0).getNumberOfItems())
                .withAmount(invoiceBody.getInvoiceItems().get(0).getAmount())
                .withVatAmount(invoiceBody.getInvoiceItems().get(0).getVatAmount())
                .build()))
        .build();
  }

  public Invoice buildInvoiceWithoutId(InvoiceBody invoiceBody) {
    return InvoiceBuilder
        .builder()
        .withDate(invoiceBody.getDate())
        .withCounterparty(CounterpartyBuilder
            .builder()
            .withCompanyName(invoiceBody.getCounterparty().getCompanyName())
            .withBankName(invoiceBody.getCounterparty().getBankName())
            .withBankNumber(invoiceBody.getCounterparty().getBankNumber())
            .withNIP(invoiceBody.getCounterparty().getNip())
            .withPhoneNumber(invoiceBody.getCounterparty().getPhoneNumber())
            .withAddress(AddressBuilder
                .builder()
                .withHouseNumber(invoiceBody.getCounterparty().getAddress().getHouseNumber())
                .withStreetName(invoiceBody.getCounterparty().getAddress().getStreetName())
                .withZipCode(invoiceBody.getCounterparty().getAddress().getZipCode())
                .withTownName(invoiceBody.getCounterparty().getAddress().getTownName())
                .build())
            .build())
        .withInvoiceItems(Arrays.asList(
            InvoiceItemBuilder
                .builder()
                .withDescription(invoiceBody.getInvoiceItems().get(0).getDescription())
                .withNumberOfItem(invoiceBody.getInvoiceItems().get(0).getNumberOfItems())
                .withAmount(invoiceBody.getInvoiceItems().get(0).getAmount())
                .withVatAmount(invoiceBody.getInvoiceItems().get(0).getVatAmount())
                .build()))
        .build();
  }

}

