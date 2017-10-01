package pl.coderstrust.fileprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH;
import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_TEST_PATH_TO_PURGE;

import org.junit.Test;
import org.springframework.util.IdGenerator;
import pl.coderstrust.database.Database;
import pl.coderstrust.generatorsfortests.InvoiceGeneratorForTests;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileProcessorTest {

  @Test
  public void shouldGetInvoiceFilePath() {
    //given
    Database database = mock(Database.class);
    IdGenerator idGenerator = mock(IdGenerator.class);
    InvoiceBook invoiceBook = new InvoiceBook(database);
    LocalDate date = LocalDate.now();
    String directoryName = DATABASE_TEST_PATH;
    int year = date.getYear();
    String month = date.getMonth().toString();
    //  when(invoiceBook.getMaxInvoiceId()).thenReturn(1);
    Invoice invoice = InvoiceGeneratorForTests
        .generateOneInvoice();
    String expectedFilePath = directoryName + year + "/" + month + "/Invoices.json";
    //when
    File filePath = FileProcessor.getInvoiceFilePath(directoryName, invoice);
    String actualFilePath = replaceFileSeparatorInFIlePath(filePath.getPath());
    //then
    assertEquals(expectedFilePath, actualFilePath);
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  //
  @Test
  public void shouldPurgeDirectory() throws Exception {
    //given
    makeSubdirectoriesAndFiles();
    File directory = new File(DATABASE_TEST_PATH_TO_PURGE);
    assertTrue(directory.list().length > 0);
    //when
    FileProcessor.purgeDirectory(directory);
    //then
    assertTrue(directory.list().length == 0);
  }

  @Test
  public void shouldReturnListOfAllFilesAndFilesSubDirectories() throws Exception {
    //given
    final List<String> expected = makeSubdirectoriesAndFiles().stream().sorted()
        .collect(Collectors.toList());
    List<File> listOfFiles = new ArrayList<>();
    List<String> stringListOfFiles = new ArrayList<>();
    //when
    FileProcessor.getFilesIncludingSubdirectories(DATABASE_TEST_PATH, listOfFiles);
    for (int i = 0; i < listOfFiles.size(); i++) {
      stringListOfFiles.add(replaceFileSeparatorInFIlePath(listOfFiles.get(i).getPath()));
    }
    List<String> actualList = stringListOfFiles.stream().sorted().collect(Collectors.toList());
    List<String> actual = new ArrayList<>();
    for (String string : actualList) {
      int indexOfSubstring = string.indexOf("src");
      actual.add(string.substring(indexOfSubstring));
    }
    Collections.sort(actual);
    //then
    assertNotNull(listOfFiles);
    assertEquals(expected.size(), listOfFiles.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }

    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
  }

  private List<String> makeSubdirectoriesAndFiles() {
    FileProcessor.purgeDirectory(new File(DATABASE_TEST_PATH_TO_PURGE));
    List<String> list = new ArrayList<>();
    for (int i = 1; i < 10; i++) {
      for (int j = 1; j < 13; j++) {
        String directoryPath =
            DATABASE_TEST_PATH + "200" + String.valueOf(i) + "/" + Month.of(j) + "/";
        String filePath = directoryPath + "TestFile.txt";
        File directory = new File(directoryPath);
        directory.mkdirs();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
          bufferedWriter.write("TestFile.txt created.");
        } catch (IOException e) {
          e.printStackTrace();
        }
        File[] files = directory
            .listFiles((dir1, name) -> name.startsWith("TestFile") && name.endsWith(".txt"));
        if (files.length > 0) {
          for (File file : files) {
            list.add(replaceFileSeparatorInFIlePath(file.getPath()));
          }
        }
      }
    }
    Collections.sort(list);
    return list;
  }

  private String replaceFileSeparatorInFIlePath(String path) {
    String[] string = path.split(Pattern.quote(System.getProperty("file.separator")));
    StringBuilder stringBuilder = new StringBuilder();
    if (string.length != 0) {
      stringBuilder.append(string[0]);
      for (int i = 1; i < string.length; i++) {
        stringBuilder.append("/" + string[i]);
      }
    }
    return stringBuilder.toString();
  }
}
