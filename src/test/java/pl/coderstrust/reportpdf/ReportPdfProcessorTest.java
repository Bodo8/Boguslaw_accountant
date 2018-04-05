package pl.coderstrust.reportpdf;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PDF_PATH;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.coderstrust.controller.InvoiceBody;
import pl.coderstrust.database.impl.InvoiceProcessor;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.generatorsfortests.InvoiceBodyGenerator;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class ReportPdfProcessorTest {

  @InjectMocks
  private ReportPdfProcessor pdfProcessor;

  @Mock
  InvoiceProcessor invoiceProcessor;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void shouldReturnSaveReportPdfWithInvoices() throws Exception {
    //given
    ArgumentCaptor<List<Invoice>> listArgumentCaptor = ArgumentCaptor
        .forClass((Class<List<Invoice>>) (Class) List.class);
    ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
    LocalDate localDate = LocalDate.now();
    Year expectedYear = Year.now();
    Month expectedMonth = localDate.getMonth();
    InvoiceBody invoiceBody = invoiceBody = InvoiceBodyGenerator.generateOneInvoiceBody(
        localDate, "LPD", "Petrol", BigDecimal.TEN, BigDecimal.ONE);
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(invoiceBody.getDate(),
        invoiceBody.getCounterparty().getCompanyName(),
        invoiceBody.getInvoiceItems().get(0).getDescription(),
        invoiceBody.getInvoiceItems().get(0).getAmount(),
        invoiceBody.getInvoiceItems().get(0).getVatAmount());
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(invoice);
    //when
    pdfProcessor.SaveDataToReportPdf(invoices, expectedYear.getValue(), expectedMonth.toString());
    //then
    verify(invoiceProcessor, times(1))
        .saveToPdf(listArgumentCaptor.capture(), fileArgumentCaptor.capture());
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PDF_PATH));
  }
}