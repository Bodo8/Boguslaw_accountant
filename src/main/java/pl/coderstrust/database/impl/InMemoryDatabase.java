package pl.coderstrust.database.impl;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.coderstrust.controller.InvoiceBody;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = {"active.database"}, havingValue = "memory")
public class InMemoryDatabase implements Database {

  private static AtomicInteger atomicInteger = new AtomicInteger(1);
  private final List<Invoice> invoices = new ArrayList<>();

  public Integer getNextInvoiceId() {
    return atomicInteger.getAndIncrement();
  }

  @Override
  public void saveInvoice(Invoice invoice) {
    invoices.add(invoice);
  }

  @Override
  public List<Invoice> getInvoices() {
    return invoices.stream().sorted().collect(Collectors.toList());
  }

  @Override
  public void removeInvoice(Invoice invoice) {
    Invoice foundInvoice = invoices
        .stream()
        .filter(invoice1 -> invoice1.getId() == invoice.getId())
        .findFirst().get();
    invoices.remove(invoices.indexOf(foundInvoice));
  }

  @Override
  public Invoice createInvoice(InvoiceBody invoiceBody) {
    return InvoiceBuilder.builder().buildInvoiceWithGeneratedId(invoiceBody, getNextInvoiceId());
  }
}