package pl.coderstrust.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.generatorsfortests.InvoiceBodyGenerator;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceBookController.class)

public class InvoiceBookControllerTest {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  InvoiceBookService service;

  @Test
  public void shouldGetSingleInvoiceInService() throws Exception {
    //given
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice();
    String invoiceAsString = mapper.writeValueAsString(invoice);
    String idAsString = String.valueOf(1);
    when(service.getSingleInvoice(any(Integer.class))).thenReturn(invoice);

    //when

    mockMvc.perform(
        get("/invoices/1")
            .content(idAsString).contentType("application/json; charset=UTF-8"))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json; charset=UTF-8"))
        .andDo(print());

    Invoice actual = service.getSingleInvoice(1);
    String actualInvoiceAsString = mapper.writeValueAsString(actual);
    //then
    assertEquals(invoiceAsString, actualInvoiceAsString);
  }

  @Test
  public void shouldGetInvoicesFromGivenMonthInService() throws Exception {
    //given
    final List<Invoice> invoices = InvoiceGeneratorForTests.generateListOfInvoicesFromApril2017();
    final String invoicesAsString = mapper.writeValueAsString(invoices);
    String yearAndMonthAsString = String.valueOf(2017 + "/April");
    when(service.getInvoicesFromGivenMonth(2017, "April")).thenReturn(invoices);
    //when
    mockMvc.perform(
        get("/invoices/?year=2017&month=April")
            .content(yearAndMonthAsString).contentType("application/json; charset=UTF-8"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json; charset=UTF-8"));
    List<Invoice> actual = service.getInvoicesFromGivenMonth(2017, "April");
    String actualInvoicesAsString = mapper.writeValueAsString(actual);
    //then
    assertEquals(invoicesAsString, actualInvoicesAsString);
  }

  @Test
  public void shouldGetInvoicesInService() throws Exception {
    //given
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice();
    final List<Invoice> invoices = Arrays.asList(invoice);
    final String invoicesAsString = mapper.writeValueAsString(invoices);
    when(service.getInvoices()).thenReturn(invoices);
    //when
    mockMvc.perform(
        get("/invoices"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json; charset=UTF-8"));

    List<Invoice> actual = service.getInvoices();
    String actualInvoicesAsString = mapper.writeValueAsString(actual);
    //then
    assertEquals(invoicesAsString, actualInvoicesAsString);
  }

  @Test
  public void shouldReturnPostInvoiceInService() throws Exception {
    //given
    ArgumentCaptor<InvoiceBody> argumentCaptor = ArgumentCaptor.forClass(InvoiceBody.class);
    InvoiceBody invoiceBody = InvoiceBodyGenerator.generateOneInvoiceBody();
    final String invoiceBodyAsString = mapper.writeValueAsString(invoiceBody);
    //when
    mockMvc.perform(
        post("/invoices")
            .content(invoiceBodyAsString)
            .contentType("application/json; charset=UTF-8"))
        .andExpect(status().isCreated());

    verify(service).postInvoice(argumentCaptor.capture());
    InvoiceBody result = argumentCaptor.getValue();
    //then
    assertEquals(invoiceBody.getDate(), result.getDate());
    assertEquals(invoiceBody.getCounterparty(), result.getCounterparty());
    assertEquals(invoiceBody.getInvoiceItems().get(0).getAmount(),
        result.getInvoiceItems().get(0).getAmount());
    assertEquals(invoiceBody.getInvoiceItems().get(0).getDescription(),
        result.getInvoiceItems().get(0).getDescription());
    assertEquals(invoiceBody.getInvoiceItems().get(0).getVatAmount(),
        result.getInvoiceItems().get(0).getVatAmount());
  }

  @Test
  public void shouldPutInvoiceInService() throws Exception {
    //given
    ArgumentCaptor<InvoiceBody> captor = ArgumentCaptor.forClass(InvoiceBody.class);
    InvoiceBody invoiceBodyForUpdate = InvoiceBodyGenerator.generateOneInvoiceBody();
    final String invoiceBodyForUpdateAsString = mapper.writeValueAsString(invoiceBodyForUpdate);
    final String urlTemplate = "/invoices/1";
    //when
    mockMvc.perform(
        put(urlTemplate)
            .content(invoiceBodyForUpdateAsString).contentType("application/json; charset=UTF-8"))
        .andExpect(status().isNoContent())
        .andDo(print());

    verify(service).putInvoice(any(Integer.class), captor.capture());
    InvoiceBody actualUpdatedInvoice = captor.getValue();
    //then
    assertEquals(invoiceBodyForUpdate.getDate(), actualUpdatedInvoice.getDate());
    assertEquals(invoiceBodyForUpdate.getCounterparty(), actualUpdatedInvoice.getCounterparty());
    assertEquals(invoiceBodyForUpdate.getInvoiceItems().get(0).getAmount(),
        actualUpdatedInvoice.getInvoiceItems().get(0).getAmount());
    assertEquals(invoiceBodyForUpdate.getInvoiceItems().get(0).getDescription(),
        actualUpdatedInvoice.getInvoiceItems().get(0).getDescription());
    assertEquals(invoiceBodyForUpdate.getInvoiceItems().get(0).getVatAmount(),
        actualUpdatedInvoice.getInvoiceItems().get(0).getVatAmount());
  }

  @Test
  public void shouldDeleteInvoiceInService() throws Exception {
    //given
    ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
    Integer expectedId = 1;
    String expectedIdAsString = mapper.writeValueAsString(expectedId);
    final String urlTemplate = "/invoices/1";
    Boolean expected = true;
    when(service.deleteInvoice(expectedId)).thenReturn(expected);
    //when
    mockMvc.perform(delete("/invoices/1")
        .content(expectedIdAsString).contentType("application/json; charset=UTF-8"))
        .andExpect(status().isOk())
        .andDo(print());

    verify(service).deleteInvoice(argumentCaptor.capture());
    //then
    Integer actualId = argumentCaptor.getValue();
    assertEquals(expectedId, actualId);
  }
}