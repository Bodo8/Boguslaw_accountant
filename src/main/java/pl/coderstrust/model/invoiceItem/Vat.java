package pl.coderstrust.model.invoiceItem;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vat_code")
public enum Vat {
  VAT_23(23),
  VAT_8(8),
  VAT_5(5),
  VAT_0(0);

  @Id
  private final int vatCode;

  Vat(int vatCode) {
    this.vatCode = vatCode;
  }

  public int getVat() {
    return this.vatCode;
  }
}
