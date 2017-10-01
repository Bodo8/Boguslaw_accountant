package pl.coderstrust.generatorsfortests;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Date generator.
 */
public class DateGeneratorForTests {

  /**
   * @param date - LocalDate.
   * @return - LocalDate.
   */
  public static LocalDate generateDateFromGivenDateTillCurrentDate(LocalDate date) {
    long randomDay = ThreadLocalRandom.current()
        .nextLong(date.toEpochDay(), LocalDate.now().toEpochDay());
    return LocalDate.ofEpochDay(randomDay);
  }

}
