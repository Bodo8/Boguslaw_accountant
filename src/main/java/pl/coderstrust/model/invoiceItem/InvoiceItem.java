package pl.coderstrust.model.invoiceItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pl.coderstrust.model.Invoice;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
public class InvoiceItem implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  @JsonIgnore
  private int id;
  @ApiModelProperty(required = true, example = "Fuel top-up")
  private String description;
  @ApiModelProperty(required = true, example = "1")
  private int numberOfItems;
  @ApiModelProperty(required = true, example = "12.98")
  private BigDecimal amount;
  @ApiModelProperty(required = true, example = "3.02")
  private BigDecimal vatAmount;
  @JoinColumn(name = "vat", referencedColumnName = "vat_code")
  @Enumerated(EnumType.ORDINAL)
  @ApiModelProperty(required = true, example = "VAT_23")
  private Vat vat;
  @ManyToOne(cascade = {CascadeType.DETACH,
      CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH})
  @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
  @JsonIgnore
  private Invoice invoice;

  public InvoiceItem(String description, int numberOfItems, BigDecimal amount,
      BigDecimal vatAmount, Vat vat) {
    this.description = description;
    this.numberOfItems = numberOfItems;
    this.amount = amount;
    this.vatAmount = vatAmount;
    this.vat = vat;
  }

  public InvoiceItem() {
  }


  public String getDescription() {
    return description;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BigDecimal getVatAmount() {
    return vatAmount;
  }

  public int getNumberOfItems() {
    return numberOfItems;
  }

  public Vat getVat() {
    return vat;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setNumberOfItems(int numberOfItems) {
    this.numberOfItems = numberOfItems;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setVatAmount(BigDecimal vatAmount) {
    this.vatAmount = vatAmount;
  }

  public void setVat(Vat vat) {
    this.vat = vat;
  }

  @Override
  public String toString() {
    return "InvoiceItem{ "
        + "description = " + description + '\''
        + ", numberOfItems = " + numberOfItems
        + ", amount= " + amount
        + ", vatAmount= " + vatAmount
        + ", VAT = " + vat
        + '}';
  }
}