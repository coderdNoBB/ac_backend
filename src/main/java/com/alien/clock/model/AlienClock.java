package com.alien.clock.model;

import com.alien.clock.utils.AlienDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlienClock {

  private int year = 0;
  private int month = 1;
  private int day = 1;
  private int hour = 0;
  private int minute = 0;
  private int second = 0;

  public boolean validateDateAndTime() {
    if (
      this.year < 0 ||
      this.month < 1 ||
      this.month > 18 ||
      this.day < 1 ||
      this.day > AlienDateTimeConverter.getDaysInMonth(this.month) ||
      this.hour < 0 ||
      this.hour >= 90 ||
      this.minute < 0 ||
      this.minute >= 90 ||
      this.second < 0 ||
      this.second >= 90
    ) {
      return false;
    }
    return true;
  }

  public void increaseTime() {
    this.second++;
    if (this.second >= 90) {
      this.second = 0;
      this.minute++;
      if (this.minute >= 90) {
        this.minute = 0;
        this.hour++;
        if (this.hour >= 36) {
          this.hour = 0;
          this.day++;
          if (this.day >= this.getDaysInMonth()) {
            this.day = 1;
            this.month++;
            if (this.month > 18) {
              this.month = 1;
              this.year++;
            }
          }
        }
      }
    }
  }

  private int getDaysInMonth() {
    return AlienDateTimeConverter.ALIEN_DAYS_IN_MONTH[this.month - 1];
  }
}
