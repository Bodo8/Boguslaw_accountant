package pl.coderstrust.database.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.model.InvoiceBook;


public class InMemoryDatabaseTest extends DatabaseTest {

  @Override
  protected Database provideDatabase() {
    return new InMemoryDatabase();
  }

  @Test
  public void shouldReturnNextInvoiceIdInMemoryDatabase() throws Exception {
    //given
    InMemoryDatabase database = new InMemoryDatabase();
    InvoiceBook invoiceBook = new InvoiceBook(new InMemoryDatabase());
    for (int expectedId = 1; expectedId < 110; expectedId++) {
      int actualId = database.getNextInvoiceId();
      assertEquals(expectedId, actualId);
    }
  }
}