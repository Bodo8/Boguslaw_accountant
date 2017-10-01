package pl.coderstrust.database.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.controller.InvoiceBody;
import pl.coderstrust.database.Database;
import pl.coderstrust.fileprocessor.FileProcessor;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InFileDatabase implements Database {

  private static Logger logger = LoggerFactory.getLogger(InFileDatabase.class);

  private final String directoryName;
  AtomicInteger atomicInteger = new AtomicInteger();
  InvoiceProcessor processor = new InvoiceProcessor();

  public InFileDatabase(String directoryName) {
    this.directoryName = directoryName;
  }


  public Integer getNextInvoiceId() {
    atomicInteger.set(getMaxInvoiceId());
    return atomicInteger.incrementAndGet();
  }

  /**
   * enables getting maximal invoice id.
   *
   * @return invoice id.
   */
  public Integer getMaxInvoiceId() {
    return getInvoices()
        .stream()
        .map(Invoice::getId)
        .max(Comparator.naturalOrder())
        .orElse(0);
  }

  /**
   * Saves given invoice into a pl.coderstrust.database.
   */
  public void saveInvoice(Invoice invoice) {
    try {
      File filePath = FileProcessor.getInvoiceFilePath(directoryName, invoice);
      List<Invoice> listOfExistingInvoices = new ArrayList<>();
      if (filePath.isFile()) {
        listOfExistingInvoices = getInvoicesFromGivenFile(filePath);
      }
      listOfExistingInvoices.add(invoice);
      Collections.sort(listOfExistingInvoices);
      processor.saveToFile(listOfExistingInvoices, filePath);
    } catch (IOException e) {
      logger.warn("Not able to save invoice to file", e);
    }
  }

  /**
   * Gets invoices from the pl.coderstrust.database.
   */
  @Override
  public List<Invoice> getInvoices() {
    List<Invoice> listOfInvoices = new ArrayList<>();
    List<File> fileList = new ArrayList<>();
    List<String> listOfInvoicePaths = new ArrayList<>();
    try {
      FileProcessor.getFilesIncludingSubdirectories(directoryName, fileList);
      for (File file : fileList) {
        listOfInvoicePaths.add(file.getPath());
      }
      for (String listOfInvoicePath : listOfInvoicePaths) {
        List<Invoice> helpList;
        helpList = processor.readFileAndConvertContentIntoListOfInvoices(listOfInvoicePath);
        listOfInvoices.addAll(helpList);
      }
    } catch (IOException e) {
      logger.warn("Not able to get all invoices from the file", e);
      throw new RuntimeException(e);
    }
    return listOfInvoices.stream().sorted().collect(Collectors.toList());
  }

  @Override
  public void removeInvoice(Invoice invoice) {
    try {
      File filePath = FileProcessor.getInvoiceFilePath(directoryName, invoice);
      if (filePath.isFile()) {
        List<Invoice> invoicesFromFile = getInvoicesFromGivenFile(filePath);

        Invoice foundInvoice = invoicesFromFile
            .stream()
            .filter(invoice1 -> invoice1.getId() == invoice.getId())
            .findFirst()
            .get();
        invoicesFromFile.remove(invoicesFromFile.indexOf(foundInvoice));
        if (invoicesFromFile.size() == 0) {
          filePath.delete();
        } else {
          processor.saveToFile(invoicesFromFile
              .stream()
              .sorted()
              .collect(Collectors.toList()), filePath);
        }
      }
    } catch (IOException e) {
      logger.warn("Not able to remove invoice from the file", e);
    }
  }

  private List<Invoice> getInvoicesFromGivenFile(File invoiceFile) {
    List<Invoice> list;
    try {
      String file = String.valueOf(invoiceFile);
      list = processor.readFileAndConvertContentIntoListOfInvoices(file);
    } catch (IOException e) {
      logger.warn("Not able to get invoices from given file", e);
      throw new RuntimeException(e);
    }
    return list.stream().sorted().collect(Collectors.toList());
  }

  @Override
  public Invoice createInvoice(InvoiceBody invoiceBody) {
    return InvoiceBuilder.builder().buildInvoiceWithGeneratedId(invoiceBody, getNextInvoiceId());
  }
}
