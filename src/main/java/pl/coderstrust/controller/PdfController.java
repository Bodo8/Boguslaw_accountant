package pl.coderstrust.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.ws.rs.QueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

import java.io.IOException;

/**
 * Report Pdf file controller.
 */
@RestController
@RequestMapping("/reportPdf")
public class PdfController {

  private final InvoiceBookService service;

  public PdfController(InvoiceBookService service) {
    this.service = service;
  }

  /**
   * @param year given year witch invoices come
   * @param month given month witch invoices come
   * @throws IOException When save to file fails.
   */
  @ApiOperation(value = "Get invoices to report pdf from given year and month",
      response = Invoice.class, responseContainer = "List")
  @GetMapping(params = {"year", "month"})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void getInvoicesFromGivenMonthToPdf(
      @ApiParam(value = "Year when the invoice was issued")
      @QueryParam("year") Integer year,
      @ApiParam(value = "Month  when the invoice was issued")
      @QueryParam("month") String month) throws IOException {
    service.generateListInvoicesToPdfReport(year, month);
  }
}
