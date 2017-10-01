package pl.coderstrust.model.counterparty;

public class CounterpartyBuilder {

  private String companyName = "";
  private Address address = new Address("", "", "", "");
  private String phoneNumber = "";
  private String nip = "";
  private String bankName = "";
  private String bankNumber = "";

  public static CounterpartyBuilder builder() {
    return new CounterpartyBuilder();
  }

  public CounterpartyBuilder withCompanyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  public CounterpartyBuilder withAddress(Address address) {
    this.address = address;
    return this;
  }

  public CounterpartyBuilder withPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public CounterpartyBuilder withAddress(AddressBuilder addressBuilder) {
    this.address = addressBuilder.build();
    return this;
  }


  public CounterpartyBuilder withNIP(String nip) {
    this.nip = nip;
    return this;
  }

  public CounterpartyBuilder withBankName(String bankName) {
    this.bankName = bankName;
    return this;
  }

  public CounterpartyBuilder withBankNumber(String bankNumber) {
    this.bankNumber = bankNumber;
    return this;
  }

  public Counterparty build() {
    return new Counterparty(companyName, address, phoneNumber, nip, bankName, bankNumber);
  }

}