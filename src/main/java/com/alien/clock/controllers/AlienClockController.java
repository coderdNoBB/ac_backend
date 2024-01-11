package com.alien.clock.controllers;

import com.alien.clock.exception.InvalidAlienDateException;
import com.alien.clock.model.AlienClock;
import com.alien.clock.services.AlienClockDeamon;
import com.alien.clock.utils.AlienDateTimeConverter;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alien-clock")
public class AlienClockController {

  @Autowired
  private AlienClockDeamon alienClockDeamon;

  @GetMapping("/currentTime")
  public AlienClock getCurrentTime() {
    return alienClockDeamon.getAlienClock();
  }

  @GetMapping("/convertToEarthTime")
  public LocalDateTime convertToEarthTime() {
    return AlienDateTimeConverter.convertToEarthTime(
      alienClockDeamon.getAlienClock()
    );
  }

  @PostMapping("/setDateAndTime")
  public AlienClock setTime(@RequestBody AlienClock newTime) {
    if (newTime.validateDateAndTime()) {
      alienClockDeamon.getAlienClock().setYear(newTime.getYear());
      alienClockDeamon.getAlienClock().setMonth(newTime.getMonth());
      alienClockDeamon.getAlienClock().setDay(newTime.getDay());
      alienClockDeamon.getAlienClock().setHour(newTime.getHour());
      alienClockDeamon.getAlienClock().setMinute(newTime.getMinute());
      alienClockDeamon.getAlienClock().setSecond(newTime.getSecond());
    } else {
      throw new InvalidAlienDateException("Invalid value.");
    }

    return alienClockDeamon.getAlienClock();
  }
}
