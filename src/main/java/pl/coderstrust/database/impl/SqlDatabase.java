package pl.coderstrust.database.impl;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.controller.InvoiceBody;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBuilder;
import pl.coderstrust.repository.InvoiceRepository;

import java.util.List;

@Repository
@ConditionalOnProperty(name = {"active.database"}, havingValue = "sql")
@Transactional
public class SqlDatabase implements Database {

  @Autowired
  InvoiceRepository invoiceRepository;

  public void saveInvoice(Invoice invoice) {
    invoiceRepository.save(invoice);
  }

  @Override
  public void removeInvoice(Invoice invoice) {
    invoiceRepository.delete(invoice);
  }

  @Override
  public List<Invoice> getInvoices() {
    return (List<Invoice>) invoiceRepository.findAll();
  }

  @Override
  public Invoice createInvoice(InvoiceBody invoiceBody) {
    return InvoiceBuilder.builder().buildInvoiceWithoutId(invoiceBody);
  }
}
