package pl.coderstrust.visitor;

import java.io.IOException;
import java.math.BigDecimal;

public interface InvoiceBookCalculator {

  BigDecimal accept(InvoiceVisitor visitor) throws IOException;
}