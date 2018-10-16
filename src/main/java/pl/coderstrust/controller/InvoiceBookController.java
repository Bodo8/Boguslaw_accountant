package pl.coderstrust.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.ws.rs.QueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBody;

import java.io.IOException;
import java.util.List;

/**
 * InvoiceBook controller.
 */
@RestController
@RequestMapping("/invoices")
public class InvoiceBookController {

  private final InvoiceBookService service;

  public InvoiceBookController(InvoiceBookService service) {
    this.service = service;
  }

  @ApiOperation(value = "Get an invoice of provided id",
      response = Invoice.class)
  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Invoice getSingleInvoice(
      @ApiParam(value = "Invoice id")
      @PathVariable int id) {
    return service.getSingleInvoice(id);
  }

  @ApiOperation(value = "Get invoices from given year and month",
      response = Invoice.class, responseContainer = "List")
  @GetMapping(params = {"year", "month"})
  @ResponseStatus(HttpStatus.OK)
  public List<Invoice> getInvoicesFromGivenMonth(
      @ApiParam(value = "Year when the invoice was issued")
      @QueryParam("year") Integer year,
      @ApiParam(value = "Month  when the invoice was issued")
      @QueryParam("month") String month) {
    return service.getInvoicesFromGivenMonth(year, month);
  }

  @ApiOperation(value = "Get list of all invoices",
      response = Invoice.class, responseContainer = "List")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Invoice> getInvoices() throws IOException {
    return service.getInvoices();
  }

  /**
   * @param invoiceBody - body of invoice.
   * @return - id of invoice.
   */
  @ApiOperation(value = "Post another invoice",
      response = Integer.class)
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public int postInvoice(
      @ApiParam(value = "Invoice data")
      @RequestBody InvoiceBody invoiceBody) throws IOException {
    return service.postInvoice(invoiceBody);
  }

  /**
   * @param id - id of invoice.
   * @param invoiceBody - body of invoice.
   * @throws IOException - when no success in reading and saving invoice.
   */
  @ApiOperation(value = "Change an invoice of provided id")
  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void putInvoice(
      @ApiParam(value = "Id of invoice to be changed")
      @PathVariable Integer id,
      @ApiParam(value = "Updated invoice data")
      @RequestBody InvoiceBody invoiceBody)
      throws IOException {
    service.putInvoice(id, invoiceBody);
  }

  /**
   * @param id - id of invoice.
   * @return - the number and description of exception.
   * @throws IOException - when problems deleting invoice.
   */
  @ApiOperation(value = "Delete an invoice of provided id",
      response = ResponseEntity.class, responseContainer = "List")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteInvoice(
      @ApiParam(value = "Id of invoice to be removed")
      @PathVariable Integer id)
      throws IOException {
    return (service.deleteInvoice(id) ? ResponseEntity.ok() : ResponseEntity.notFound()).build();
  }
}