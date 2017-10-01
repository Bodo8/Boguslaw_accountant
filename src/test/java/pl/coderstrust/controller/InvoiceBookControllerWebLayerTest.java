package pl.coderstrust.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceBookController.class)
public class InvoiceBookControllerWebLayerTest {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  InvoiceBookService service;

  @Test
  public void shouldGetSingleInvoiceInService() throws Exception {
    //given
    LocalDate date = LocalDate.now();
    Invoice invoice = InvoiceGeneratorForTests.generateOneInvoice(date, "LPT",
        "Petrol", BigDecimal.TEN, BigDecimal.ONE);
    when(service.getSingleInvoice(any(Integer.class))).thenReturn(invoice);
    //when
    mockMvc.perform(
        get("/invoices/1")
            .content("1").contentType("application/json; charset=UTF-8"))
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json; charset=UTF-8"))
        .andExpect(jsonPath("$.id").value(invoice.getId()))
        .andExpect(jsonPath("$.date[0]", is(date.getYear())))
        .andExpect(jsonPath("$.date[1]", is(date.getMonthValue())))
        .andExpect(jsonPath("$.date[2]", is(date.getDayOfMonth())))
        .andExpect(jsonPath("$.counterparty.companyName",
            is(invoice.getCounterparty().getCompanyName())))
        .andExpect(jsonPath("$.invoiceItems[0].description",
            is(invoice.getInvoiceItems().get(0).getDescription())))
        .andExpect(jsonPath("$.invoiceItems[0].amount", is(10)))
        .andExpect(jsonPath("$.invoiceItems[0].vatAmount", is(1)))
        .andDo(print());
    verify(service, times(1)).getSingleInvoice(1);
    verifyNoMoreInteractions(service);
  }

  @Test
  public void shouldGetInvoicesFromGivenMonthInService() throws Exception {
    //given
    final List<Invoice> invoices = InvoiceGeneratorForTests.generateListOfInvoicesFromApril2017();
    when(service.getInvoicesFromGivenMonth(2017, "April")).thenReturn(invoices);
    String yearAndMonthAsString = String.valueOf("?year=" + 2017 + "&month=April");

    //when
    mockMvc.perform(
        get("/invoices/?year=2017&month=April")
            .content(yearAndMonthAsString)
            .contentType("application/json; charset=UTF-8"))
        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(20)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[*].date[0]").value(hasItem(2017)))
        .andExpect(jsonPath("$[*].date[1]").value(hasItem(4)))
        .andExpect(jsonPath("$[0].date[2]", is(1)))
        .andExpect(jsonPath("$[*].counterparty.companyName").value(hasItem("")))
        .andExpect(jsonPath("$[*]invoiceItems[0].description").value(hasItem("")))
        .andExpect(jsonPath("$[*]invoiceItems[0].amount").value(hasItem(10)))
        .andExpect(jsonPath("$[*]invoiceItems[0].vatAmount").value(hasItem(1)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].date[2]", is(2)))
        .andExpect(jsonPath("$[2].id", is(3)))
        .andExpect(jsonPath("$[2].date[2]", is(3)))
        .andExpect(jsonPath("$[3].id", is(4)))
        .andExpect(jsonPath("$[3].date[2]", is(4)))
        .andExpect(jsonPath("$[19].id", is(20)))
        .andExpect(jsonPath("$[19].date[2]", is(20)));

    verify(service, times(1))
        .getInvoicesFromGivenMonth(2017, "April");
    verifyNoMoreInteractions(service);
  }

  @Test
  public void shouldGetInvoicesInService() throws Exception {
    //given
    final List<Invoice> invoices = InvoiceGeneratorForTests.generateListOfInvoicesFromApril2017();
    when(service.getInvoices()).thenReturn(invoices);
    mockMvc.perform(
        get("/invoices"))
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json; charset=UTF-8"))
        .andExpect(jsonPath("$", hasSize(20)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[*].date[0]").value(hasItem(2017)))
        .andExpect(jsonPath("$[*].date[1]").value(hasItem(4)))
        .andExpect(jsonPath("$[0].date[2]", is(1)))
        .andExpect(jsonPath("$[*].counterparty.companyName").value(hasItem("")))
        .andExpect(jsonPath("$[*]invoiceItems[0].description").value(hasItem("")))
        .andExpect(jsonPath("$[*]invoiceItems[0].amount").value(hasItem(10)))
        .andExpect(jsonPath("$[*]invoiceItems[0].vatAmount").value(hasItem(1)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].date[2]", is(2)))
        .andExpect(jsonPath("$[2].id", is(3)))
        .andExpect(jsonPath("$[2].date[2]", is(3)))
        .andExpect(jsonPath("$[3].id", is(4)))
        .andExpect(jsonPath("$[3].date[2]", is(4)))
        .andExpect(jsonPath("$[19].id", is(20)))
        .andExpect(jsonPath("$[19].date[2]", is(20)));
    verify(service, times(1)).getInvoices();
    verifyNoMoreInteractions(service);
  }

  @Test
  public void shouldReturnPostInvoiceInService() throws Exception {
    //given
    ArgumentCaptor<InvoiceBody> captor = ArgumentCaptor.forClass(InvoiceBody.class);
    LocalDate date = LocalDate.now();
    int year = date.getYear();
    InvoiceBody invoiceBody = InvoiceBodyGenerator.generateOneInvoiceBody();
    final String invoiceBodyAsString = mapper.writeValueAsString(invoiceBody);
    //when
    mockMvc.perform(
        post("/invoices")
            .content(invoiceBodyAsString)
            .contentType("application/json; charset=UTF-8"))
        //then
        .andExpect(status().is(201))
        .andDo(print());

    verify(service, times(1)).postInvoice(captor.capture());
    verifyNoMoreInteractions(service);

    InvoiceBody result = captor.getValue();
    assertNotNull(result);
    assertThat(result.getDate(), is(date));
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
    ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
    LocalDate date = LocalDate.now();
    int year = date.getYear();
    InvoiceBody invoiceBodyForUpdate = InvoiceBodyGenerator.generateOneInvoiceBody();
    final String invoiceBodyForUpdateAsString = mapper.writeValueAsString(invoiceBodyForUpdate);
    final String urlTemplate = "/invoices/1";
    //when
    mockMvc.perform(
        put(urlTemplate)
            .content("1")
            .content(invoiceBodyForUpdateAsString)
            .contentType("application/json; charset=UTF-8"))
        //then
        .andExpect(status().is(204))
        .andDo(print());

    verify(service, times(1))
        .putInvoice(idCaptor.capture(), captor.capture());
    verifyNoMoreInteractions(service);

    Integer id = idCaptor.getValue();
    assertThat(id, is(1));
    InvoiceBody actualUpdatedInvoice = captor.getValue();
    assertNotNull(actualUpdatedInvoice);
    assertThat(actualUpdatedInvoice.getDate(), is(invoiceBodyForUpdate.getDate()));
    assertThat(actualUpdatedInvoice.getCounterparty(), is(invoiceBodyForUpdate.getCounterparty()));
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
    ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
    String expectedIdAsString = mapper.writeValueAsString(1);
    Boolean expected = true;
    when(service.deleteInvoice(1)).thenReturn(expected);
    //when
    mockMvc.perform(delete("/invoices/1")
        .content(expectedIdAsString).contentType("application/json; charset=UTF-8"))
        //then
        .andExpect(status().isOk())
        .andDo(print());
    verify(service, times(1))
        .deleteInvoice(idCaptor.capture());
    verifyNoMoreInteractions(service);
    Integer id = idCaptor.getValue();
    assertThat(id, is(1));
  }
}