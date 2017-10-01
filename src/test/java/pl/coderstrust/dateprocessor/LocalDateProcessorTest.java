package pl.coderstrust.dateprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.getFirstDayOfMonthInYear;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.getLastDayOfMonthInYear;
import static pl.coderstrust.dateprocessor.LocalDateProcessor.isDateFromGivenPeriod;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LocalDateProcessorTest {

  @Test
  public void shouldCheckIfDateIsFromGivenPeriod() throws Exception {
    //given
    LocalDate fromDate = LocalDate.of(2010, 3, 1);
    LocalDate toDate = LocalDate.of(2015, 7, 12);
    List<LocalDate> list = getListOfLocalDates();
    final List<LocalDate> expectedList = list
        .stream()
        .filter(date ->
            (date.isAfter(fromDate) && date.isBefore(toDate)) || date.equals(fromDate) || date
                .equals(toDate))
        .collect(Collectors.toList());
    //when
    List<LocalDate> actualList = new ArrayList<>();
    actualList = list.stream()
        .filter(date -> isDateFromGivenPeriod(date, fromDate, toDate))
        .sorted()
        .collect(Collectors.toList());
    //then
    assertNotNull(actualList);
    assertEquals(expectedList.size(), actualList.size());
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  @Test
  public void shouldReturnFirstDayOfGivenMonthInCurrentYear() throws Exception {
    //given
    Month[] monthsList = Month.values();
    Year year = Year.of(2017);
    List<LocalDate> expectedList = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      expectedList.add(LocalDate.of(2017, i + 1, 1));
    }
    Collections.sort(expectedList);
    //when
    List<LocalDate> actualList = new ArrayList<>();
    for (Month month : monthsList) {
      actualList.add(getFirstDayOfMonthInYear(year, month));
    }
    Collections.sort(actualList);
    //then
    assertNotNull(actualList);
    assertEquals(expectedList.size(), actualList.size());
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  @Test
  public void shouldReturnLastDayOfGivenMonthInGivenYear() throws Exception {
    //given
    Month[] monthsList = Month.values();
    Year year = Year.of(2017);
    List<LocalDate> expectedList = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      LocalDate date = LocalDate.of(year.getValue(), i + 1, 1);
      expectedList.add(LocalDate.of(year.getValue(), date.getMonth(), date.lengthOfMonth()));
    }
    Collections.sort(expectedList);
    //when
    List<LocalDate> actualList = new ArrayList<>();
    for (Month month : monthsList) {
      actualList.add(getLastDayOfMonthInYear(year, month));
    }
    Collections.sort(actualList);
    //then
    assertNotNull(actualList);
    assertEquals(expectedList.size(), actualList.size());
    for (int i = 0; i < expectedList.size(); i++) {
      assertEquals(expectedList.get(i), actualList.get(i));
    }
  }

  private List<LocalDate> getListOfLocalDates() {
    List<LocalDate> localDataList = new ArrayList<>();
    for (int year = 2000; year < 2018; year++) {
      for (int month = 1; month < 13; month++) {
        for (int day = 1; day < 28; day += 5) {
          localDataList.add(LocalDate.of(year, month, day));
        }
      }
    }
    Collections.sort(localDataList);
    return localDataList;
  }

}