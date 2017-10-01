package pl.coderstrust.dateprocessor;


import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * DateProcessor class provides static methods used in Database interface implementations.
 */
public class LocalDateProcessor {

  /**
   * Class is responsible for providing date information needed in invoice operations.
   *
   * @param date - given date as LocalDate type.
   * @param fromDate - as LocalDate type, sets the start of the period .
   * @param toDate - as LocalDate type, sets the end od the period.
   * @return - if date is from given period (fromDate, toDate), method returns true value, if given
   * date is not from given period, it returns false value.
   */
  public static boolean isDateFromGivenPeriod(
      LocalDate date, LocalDate fromDate, LocalDate toDate) {
    return !(date.isBefore(fromDate) || date.isAfter(toDate));
  }

  /**
   * Method provides the very first day of given current year.
   *
   * @param month - the month of current year.
   * @return - returns the first date of the first day of the given month in the current year.
   */
  public static LocalDate getFirstDayOfMonthInYear(Year year, Month month) {
    return LocalDate.of(year.getValue(), month, 1);
  }

  /**
   * Method provides the last day of given current year.
   *
   * @param month - the month of current year.
   * @return - returns the last date of the last day of the given month in the current year.
   */
  public static LocalDate getLastDayOfMonthInYear(Year year, Month month) {
    return LocalDate.of(year.getValue(), month,
        LocalDate.of(year.getValue(), month, 1).lengthOfMonth());
  }
}