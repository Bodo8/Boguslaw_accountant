package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import pl.coderstrust.model.counterparty.Counterparty;
import pl.coderstrust.model.invoiceItem.InvoiceItem;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice implements Comparable<Invoice>, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "invoice_id")
  private int id;
  @ApiModelProperty(required = true, example = "2017-08-27")
  private LocalDate date = LocalDate.now();
  @ManyToOne(cascade = CascadeType.ALL)
  private Counterparty counterparty;
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<InvoiceItem> invoiceItems;


  /**
   * Create instance of Invoice class.
   *
   * @param date - date of issuing of the invoice.
   * @param counterparty - says w  hich counterparty the invoice was issues by.
   */
  @JsonCreator
  public Invoice(@JsonProperty("id") int id,
      @JsonProperty("date") LocalDate date,
      @JsonProperty("counterparty") Counterparty counterparty,
      @JsonProperty("invoiceItems") List<InvoiceItem> invoiceItems) {
    this.id = id;
    this.date = date;
    this.counterparty = counterparty;
    this.invoiceItems = invoiceItems;
  }

  public Invoice() {
  }

  public Invoice(LocalDate date, Counterparty counterparty,
      List<InvoiceItem> itemList) {
    this.date = date;
    this.counterparty = counterparty;
    this.invoiceItems = itemList;
  }

  public int getId() {
    return id;
  }

  public LocalDate getDate() {
    return date;
  }

  public Counterparty getCounterparty() {
    return counterparty;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setCounterparty(Counterparty counterparty) {
    this.counterparty = counterparty;
  }

  public List<InvoiceItem> getInvoiceItems() {
    return invoiceItems;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Invoice)) {
      return false;
    }

    Invoice invoice = (Invoice) obj;

    if (getId() != invoice.getId()) {
      return false;
    }
    if (!getDate().equals(invoice.getDate())) {
      return false;
    }
    return getCounterparty().equals(invoice.getCounterparty());
  }

  @Override
  public int hashCode() {
    int result = getId();
    result = 31 * result + getDate().hashCode();
    result = 31 * result + getCounterparty().hashCode();
    return result;
  }

  @Override
  public int compareTo(Invoice object) {
    if (object == null) {
      return -1;
    }
    if (this == null) {
      return -1;
    }
    int comparison = (this.date.getYear() - object.date.getYear());
    if (comparison == 0) {
      comparison = (this.date.getMonthValue() - object.date.getMonthValue());
      if (comparison == 0) {
        comparison = (this.date.getDayOfMonth() - object.date.getDayOfMonth());
      }
    }
    if (comparison < 0) {
      return -1;
    }
    if (comparison > 0) {
      return 1;
    }

    return 0;
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "id=" + id
        + ", date=" + date
        + ", counterparty='" + counterparty + '\''
        + ", invoiceItems=" + invoiceItems
        + '}';
  }
}