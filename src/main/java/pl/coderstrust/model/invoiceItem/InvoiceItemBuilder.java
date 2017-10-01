package pl.coderstrust.model.invoiceItem;

import java.math.BigDecimal;

public class InvoiceItemBuilder {

  private String description = "";
  private int numberOfItem = 0;
  private BigDecimal amount = BigDecimal.ZERO;
  private BigDecimal vatAmount = BigDecimal.ZERO;
  private Vat vat;

  public static InvoiceItemBuilder builder() {
    return new InvoiceItemBuilder();
  }

  public InvoiceItemBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public InvoiceItemBuilder withNumberOfItem(int numberOfItem) {
    this.numberOfItem = numberOfItem;
    return this;
  }

  public InvoiceItemBuilder withAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public InvoiceItemBuilder withVatAmount(BigDecimal vatAmount) {
    this.vatAmount = vatAmount;
    return this;
  }

  public InvoiceItemBuilder withVat(Vat vat) {
    this.vat = vat;
    return this;
  }

  public InvoiceItem build() {
    return new InvoiceItem(description, numberOfItem, amount, vatAmount, vat);
  }


}