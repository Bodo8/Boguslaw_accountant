package pl.coderstrust.database.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests.generateOneInvoice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.repository.InvoiceRepository;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class SqlDatabaseTest {

  @MockBean
  InvoiceRepository invoiceRepository;

  @Test
  public void shouldSaveInvoiceToDatabase() {
    //given
    Invoice given = generateOneInvoice();

    //when
    invoiceRepository.save(given);
    when(invoiceRepository.save(given)).thenReturn(given);

    //then
    verify(invoiceRepository).save(given);
  }

  @Test
  public void shouldGetInvoicesFromDatabase() {
    //given
    Invoice given = generateOneInvoice();
    List<Invoice> expected = Arrays.asList(given);
    when(invoiceRepository.findAll()).thenReturn(expected);

    //when
    List<Invoice> actual = (List<Invoice>) invoiceRepository.findAll();

    //then
    verify(invoiceRepository).findAll();
    assertEquals(expected, actual);
  }

  @Test
  public void shouldDeleteInvoiceFromDatabase() {
    //given
    Invoice given = generateOneInvoice();
    int id = 123;
    given.setId(id);

    //when
    invoiceRepository.delete(Long.valueOf(id));

    //then
    verify(invoiceRepository).delete(Long.valueOf(id));
  }
}