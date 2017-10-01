package pl.coderstrust.model.counterparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import pl.coderstrust.model.Invoice;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "counterparty")
public class Counterparty implements Serializable {

  @Id
  @Column(name = "counterparty_id")
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ApiModelProperty(required = true, example = "1252437")
  @Column(name = "nip")
  private String nip;
  @ApiModelProperty(required = true, example = "Schell GmbH & Co. KG")
  private String companyName;
  @ApiModelProperty(required = true, example = "+48152396066")
  private String phoneNumber;
  @ApiModelProperty(required = true, example = "Deutsche Bank AG")
  private String bankName;
  @ApiModelProperty(required = true, example = "1267354800016354")
  @Column(name = "bank_number")
  private String bankNumber;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private Address address;
  @OneToMany(mappedBy = "counterparty", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Invoice> invoices;

  public Counterparty(String companyName, Address address, String phoneNumber, String nip,
      String bankName,
      String bankNumber) {
    this.companyName = companyName;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.nip = nip;
    this.bankName = bankName;
    this.bankNumber = bankNumber;
  }

  public Counterparty() {
  }

  public int getId() {
    return id;
  }

  public String getNip() {
    return nip;
  }

  public String getCompanyName() {
    return companyName;
  }

  public Address getAddress() {
    return address;
  }


  public String getBankName() {
    return bankName;
  }

  public String getBankNumber() {
    return bankNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public List<Invoice> getInvoices() {
    return invoices;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public void setBankNumber(String bankNumber) {
    this.bankNumber = bankNumber;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setInvoices(List<Invoice> invoices) {
    this.invoices = invoices;
  }

  @Override
  public String toString() {
    return "Counterparty{"
        + "companyName='" + companyName + '\''
        + ", address=" + address
        + ", phoneNumber='" + phoneNumber + '\''
        + ", NIP='" + nip + '\''
        + ", bankName='" + bankName + '\''
        + ", bankNumber='" + bankNumber + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Counterparty)) {
      return false;
    }

    Counterparty that = (Counterparty) o;

    if (nip != that.nip) {
      return false;
    }
    if (!getCompanyName().equals(that.getCompanyName())) {
      return false;
    }
    if (!getPhoneNumber().equals(that.getPhoneNumber())) {
      return false;
    }
    if (!getBankName().equals(that.getBankName())) {
      return false;
    }
    if (!getBankNumber().equals(that.getBankNumber())) {
      return false;
    }
    return getAddress().equals(that.getAddress());
  }

  @Override
  public int hashCode() {
    int result = getNip().hashCode();
    result = 31 * result + getCompanyName().hashCode();
    result = 31 * result + getPhoneNumber().hashCode();
    result = 31 * result + getBankName().hashCode();
    result = 31 * result + getBankNumber().hashCode();
    result = 31 * result + getAddress().hashCode();
    return result;
  }
}
