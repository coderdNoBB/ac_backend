package com.alien.clock.utils;

import com.alien.clock.model.AlienClock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

public class AlienDateTimeConverter {

  private static final double ALIEN_SECOND_IN_EARTH_SECONDS = 0.5;
  private static final int ALIEN_SECONDS_IN_MINUTE = 90;
  private static final int ALIEN_MINUTES_IN_HOUR = 90;
  private static final int ALIEN_HOURS_IN_DAY = 36;
  private static final int ALIEN_MONTHS_IN_YEAR = 18;
  private static final int ALIEN_SECONDS_IN_DAY =
    ALIEN_HOURS_IN_DAY * ALIEN_MINUTES_IN_HOUR * ALIEN_SECONDS_IN_MINUTE;
  public static final int[] ALIEN_DAYS_IN_MONTH = {
    44,
    42,
    48,
    40,
    48,
    44,
    40,
    44,
    42,
    40,
    40,
    42,
    44,
    48,
    42,
    40,
    44,
    38,
  };
  private static final int ALIEN_DAYS_IN_YEAR = IntStream
    .of(ALIEN_DAYS_IN_MONTH)
    .sum();
  private static final int ALIEN_SECOND_IN_YEAR =
    ALIEN_DAYS_IN_YEAR * ALIEN_SECONDS_IN_DAY;

  public static void main(String[] args) {
    LocalDateTime earthTime = LocalDateTime.now();
    AlienClock alienTime = convertToAlienTime(earthTime);
    System.out.println("Earth Time: " + earthTime);
    System.out.println("Alien Time: " + alienTime);
    System.out.println("convertToEarthTime: " + convertToEarthTime(alienTime));
  }

  public static AlienClock convertToAlienTime(LocalDateTime earthTime) {
    long earthSeconds = ChronoUnit.SECONDS.between(
      LocalDateTime.of(1970, 1, 1, 12, 0, 0),
      earthTime
    );

    // Calculate total Alien seconds
    double alienSeconds = earthSeconds / ALIEN_SECOND_IN_EARTH_SECONDS;

    int alienYear = 2804;
    int alienMonth = 18;
    int alienDay = 31;
    int alienHour = 2;
    int alienMinute = 2;
    int alienSecond = 88;

    alienSecond += (int) (alienSeconds % ALIEN_SECONDS_IN_MINUTE);
    alienMinute +=
      (int) ((alienSeconds / ALIEN_SECONDS_IN_MINUTE) % ALIEN_MINUTES_IN_HOUR);
    alienHour +=
      (int) (
        (alienSeconds / (ALIEN_SECONDS_IN_MINUTE * ALIEN_MINUTES_IN_HOUR)) %
        ALIEN_HOURS_IN_DAY
      );

    // Ensure that alienSecond is in the range [0, 89]
    alienSecond = alienSecond % ALIEN_SECONDS_IN_MINUTE;

    // Ensure that alienMinute is in the range [0, 89]
    alienMinute = alienMinute % ALIEN_MINUTES_IN_HOUR;

    // Ensure that alienHour is in the range [0, 35]
    alienHour = alienHour % ALIEN_MINUTES_IN_HOUR;

    while (alienSeconds >= ALIEN_SECONDS_IN_DAY) {
      int daysInMonth = getDaysInMonth(alienMonth);
      alienSeconds -= ALIEN_SECONDS_IN_DAY;
      alienDay++;
      if (alienDay > daysInMonth) {
        alienDay = 1;
        alienMonth++;
        if (alienMonth > ALIEN_MONTHS_IN_YEAR) {
          alienMonth = 1;
          alienYear++;
        }
      }
    }

    return new AlienClock(
      alienYear,
      alienMonth,
      alienDay,
      alienHour,
      alienMinute,
      alienSecond
    );
  }

  public static LocalDateTime convertToEarthTime(AlienClock alienTime) {
    long alienSeconds = calculateTotalAlienSeconds(alienTime);
    LocalDateTime earthTime = LocalDateTime
      .of(1970, 1, 1, 12, 0, 0)
      .plusSeconds(alienSeconds);
    return earthTime;
  }

  private static long calculateTotalAlienSeconds(AlienClock alienTime) {
    // Alien Date time 2085-01-01 00:00:00 minus 2084-18-31 02:02:88 // seconds + minutess + hour + days
    long total2084secondsBefore2085 =
      2 + (89 - 2) * 90 + (35 - 2) * 90 * 90 + (38 - 31) * ALIEN_SECONDS_IN_DAY;

    long remainSeconds =
      calculateDaysUntilMonth(alienTime.getMonth(), alienTime.getDay()) *
      ALIEN_SECONDS_IN_DAY +
      alienTime.getHour() *
      ALIEN_MINUTES_IN_HOUR *
      ALIEN_SECONDS_IN_MINUTE +
      alienTime.getMinute() *
      ALIEN_SECONDS_IN_MINUTE +
      alienTime.getSecond();

    BigDecimal totalAlienSeconds = BigDecimal
      .valueOf(alienTime.getYear() - 2805)
      .multiply(BigDecimal.valueOf(ALIEN_SECOND_IN_YEAR))
      .add(BigDecimal.valueOf(total2084secondsBefore2085))
      .add(BigDecimal.valueOf(remainSeconds))
      .multiply(BigDecimal.valueOf(ALIEN_SECOND_IN_EARTH_SECONDS));

    return totalAlienSeconds.setScale(0, RoundingMode.HALF_UP).longValue();
  }

  private static long calculateDaysUntilMonth(int month, int day) {
    long days = 0;
    for (int m = 1; m < month; m++) {
      days += getDaysInMonth(m);
    }
    return days + day - 1;
  }

  public static int getDaysInMonth(int month) {
    return ALIEN_DAYS_IN_MONTH[month - 1];
  }
}
