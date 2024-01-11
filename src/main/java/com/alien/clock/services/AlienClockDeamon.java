package com.alien.clock.services;

import com.alien.clock.model.AlienClock;
import com.alien.clock.utils.AlienDateTimeConverter;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class AlienClockDeamon extends Thread {

  private AlienClock alienClock = new AlienClock();

  public AlienClockDeamon() {
    LocalDateTime earthTime = LocalDateTime.now();
    alienClock = AlienDateTimeConverter.convertToAlienTime(earthTime);
    this.setDaemon(true);
    this.start();
  }

  @Override
  public void run() {
    while (true) {
      alienClock.increaseTime();
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public AlienClock getAlienClock() {
    return this.alienClock;
  }
}
