package pl.coderstrust.database;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBody;

import java.util.List;

/**
 * Interface Database - provides methods save and get invoices from pl.coderstrust.database.
 */


public interface Database {

  void saveInvoice(Invoice invoice);

  void removeInvoice(Invoice invoice);

  List<Invoice> getInvoices();

  Invoice createInvoice(InvoiceBody invoiceBody);
}
