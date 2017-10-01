package pl.coderstrust.fileprocessor;

import pl.coderstrust.model.Invoice;

import java.io.File;
import java.util.List;

/**
 * FileProcessor class is responsible for file operations.
 */
public class FileProcessor {

  /**
   * Method provides file path for an invoice to be saved in.
   *
   * @param directoryName - the name of main directory where subdirectories should be created.
   * @param invoice - the invoice the method should provide the file path for.
   * @return - the method returns File object as a file Path.
   */
  public static File getInvoiceFilePath(String directoryName, Invoice invoice) {
    int year = invoice.getDate().getYear();
    String month = invoice.getDate().getMonth().toString();
    String stringDirectory = directoryName + year + "/" + month + "/";
    File directory = new File(stringDirectory);
    directory.mkdirs();
    return new File(stringDirectory + "/Invoices.json");
  }

  public static File getInvoicePdfPath(String directoryName, Integer year, String month) {
    String stringDirectory = directoryName + year + "/" + month;
    File directory = new File(stringDirectory);
    directory.mkdirs();
    return new File(stringDirectory + "/Invoices.pdf");
  }

  /**
   * Method  deletes all subdirectories and files from given one.
   *
   * @param directory - the directory from which other subdirectories and files are to be removed.
   */
  public static void purgeDirectory(File directory) {
    if (directory.exists() && directory.isDirectory()) {
      for (File file : directory.listFiles()) {
        if (file.isDirectory()) {
          purgeDirectory(file);
        }
        file.delete();
      }
    }
  }

  /**
   * Method makes a list of directories, subdirectories and files.
   *
   * @param directoryName - from which the list of subdirectories is to be made.
   * @param files - a collection object which the list of subdirectories is to be referred to.
   */
  public static void getFilesIncludingSubdirectories(String directoryName, List<File> files) {
    File directory = new File(directoryName);
    for (File file : directory.listFiles()) {
      if (file.isFile()) {
        files.add(file);
      } else if (file.isDirectory()) {
        getFilesIncludingSubdirectories(file.getAbsolutePath(), files);
      }
    }
  }
}