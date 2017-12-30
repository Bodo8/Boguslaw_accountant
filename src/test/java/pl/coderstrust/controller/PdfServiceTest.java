package pl.coderstrust.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PDF_PATH;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.coderstrust.configuration.FileConfigurationProvider;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.generatorsfortests.InvoiceBodyGenerator;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class PdfServiceTest {

  @InjectMocks
  private PdfService service;

  @Mock
  InvoiceBook invoiceBook;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void shouldListInvoiceToReportPdfWithInvoices() throws Exception {
    //given
    ArgumentCaptor<Year> yearArgumentCaptor = ArgumentCaptor.forClass(Year.class);
    ArgumentCaptor<Month> monthArgumentCaptor = ArgumentCaptor.forClass(Month.class);
    final String directoryName = FileConfigurationProvider.DATABASE_TEST_PDF_PATH;
    LocalDate localDate = LocalDate.now();
    File filePath = FileProcessor
        .getInvoicePdfPath(directoryName, localDate.getYear(), localDate.getMonth().toString());
    Boolean expectedExistFile = true;
    InvoiceBody invoiceBody = invoiceBody = InvoiceBodyGenerator.generateOneInvoiceBody(
        localDate, "LPD", "Petrol", BigDecimal.TEN, BigDecimal.ONE);

    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(invoiceBody.getDate(),
        invoiceBody.getCounterparty().getCompanyName(),
        invoiceBody.getInvoiceItems().get(0).getDescription(),
        invoiceBody.getInvoiceItems().get(0).getAmount(),
        invoiceBody.getInvoiceItems().get(0).getVatAmount());
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(invoice);
    when(invoiceBook.getInvoices()).thenReturn(invoices);

    //when
    service.generateListInvoicesToPdfReport(localDate.getYear(),
        localDate.getMonth().toString());
    //then
    verify(invoiceBook, times(1))
        .getInvoices(yearArgumentCaptor.capture(), monthArgumentCaptor.capture());
    Boolean actualFile = filePath.exists();
    assertEquals(expectedExistFile, actualFile);
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PDF_PATH));
  }

}