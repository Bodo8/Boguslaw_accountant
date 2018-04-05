package pl.coderstrust.model.counterparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "address")
public class Address implements Serializable {

  @JsonIgnore
  private int id;
  @ApiModelProperty(required = true, example = "60325")
  private String zipCode;
  @ApiModelProperty(required = true, example = "Frankfurt Am Main")
  private String townName;
  @ApiModelProperty(required = true, example = "Taunusanlage")
  private String streetName;
  @ApiModelProperty(required = true, example = "12")
  private String houseNumber;

  public Address(String zipCode, String townName, String streetName, String houseNumber) {
    this.zipCode = zipCode;
    this.townName = townName;
    this.streetName = streetName;
    this.houseNumber = houseNumber;
  }

  public Address() {
  }

  @Id
  @Column(name = "address_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int getId() {
    return id;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getTownName() {
    return townName;
  }

  public String getStreetName() {
    return streetName;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public void setTownName(String townName) {
    this.townName = townName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  @Override
  public String toString() {
    return "Address{"
        + "zipCode='" + zipCode + '\''
        + ", townName='" + townName + '\''
        + ", streetName='" + streetName + '\''
        + ", houseNumber='" + houseNumber + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Address)) {
      return false;
    }

    Address address = (Address) o;

    if (!getZipCode().equals(address.getZipCode())) {
      return false;
    }
    if (!getTownName().equals(address.getTownName())) {
      return false;
    }
    if (!getStreetName().equals(address.getStreetName())) {
      return false;
    }
    return getHouseNumber().equals(address.getHouseNumber());
  }

  @Override
  public int hashCode() {
    int result = getZipCode().hashCode();
    result = 31 * result + getTownName().hashCode();
    result = 31 * result + getStreetName().hashCode();
    result = 31 * result + getHouseNumber().hashCode();
    return result;
  }
}