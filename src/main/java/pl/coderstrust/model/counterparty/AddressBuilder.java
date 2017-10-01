package pl.coderstrust.model.counterparty;

public class AddressBuilder {

  private String zipCode = "";
  private String townName = "";
  private String streetName = "";
  private String houseNumber = "";

  public static AddressBuilder builder() {
    return new AddressBuilder();
  }

  public AddressBuilder withZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  public AddressBuilder withTownName(String townName) {
    this.townName = townName;
    return this;
  }

  public AddressBuilder withStreetName(String streetName) {
    this.streetName = streetName;
    return this;
  }

  public AddressBuilder withHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
    return this;
  }

  public Address build() {
    return new Address(zipCode, townName, streetName, houseNumber);
  }
}