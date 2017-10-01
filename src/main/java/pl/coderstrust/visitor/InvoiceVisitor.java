package pl.coderstrust.visitor;

import pl.coderstrust.model.Invoice;

import java.math.BigDecimal;

public interface InvoiceVisitor {

  BigDecimal visit(Invoice invoice);
}