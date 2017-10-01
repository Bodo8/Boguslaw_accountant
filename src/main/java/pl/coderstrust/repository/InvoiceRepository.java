package pl.coderstrust.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderstrust.model.Invoice;

@Transactional
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}