package pl.coderstrust.visitor;

import static pl.coderstrust.configuration.CompanyConfigurationProvider.COMPANY_NAME;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.invoiceItem.InvoiceItem;

import java.math.BigDecimal;

public class CostsCalculator implements InvoiceVisitor {

  @Override
  public BigDecimal visit(Invoice invoice) {
    return COMPANY_NAME.equals(invoice.getCounterparty().getCompanyName()) ?
        BigDecimal.ZERO : invoice.getInvoiceItems()
        .stream()
        .map(InvoiceItem::getAmount)
        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
  }
}