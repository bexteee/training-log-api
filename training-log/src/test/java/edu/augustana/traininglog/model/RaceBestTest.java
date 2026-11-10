package edu.augustana.traininglog.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class RaceBestTest {
  @Test
  void validRaceBest() {
    RaceBest rb = new RaceBest("A01", StrokeType.FREESTYLE, 50, BestKind.RACE, StartType.DIVE, 21.40, LocalDate.of(2025, 2, 6));

    assertEquals(21.40, rb.time(), 0.001);
  }

  @Test
  void validRaceBestFloatNums() {
    RaceBest rb = new RaceBest("A01", StrokeType.FREESTYLE, 12.5, BestKind.RACE, StartType.DIVE, 8.0, LocalDate.of(2025, 2, 6));

    assertEquals(12.5, rb.distance(), 0.001);
  }

  @Test 
  void emptyAthleteId() {
    assertThrows(IllegalArgumentException.class,
      () -> new RaceBest("", StrokeType.FREESTYLE, 50, BestKind.RACE, StartType.DIVE, 21.40, LocalDate.of(2025, 2, 6))
    );
  }

  @Test 
  void zeroDistance() {
    assertThrows(IllegalArgumentException.class, 
      () -> new RaceBest("A01", StrokeType.FREESTYLE, 0, BestKind.RACE, StartType.DIVE, 21.40, LocalDate.of(2025, 2, 6))
    );
  }

  @Test
  void negativeTime() {
    assertThrows(IllegalArgumentException.class,
      () -> new RaceBest("A01", StrokeType.FREESTYLE, 50, BestKind.RACE, StartType.DIVE, -1.0, LocalDate.of(2025, 2, 6))
    );
  }

  @Test
  void missingStroke() {
    assertThrows(IllegalArgumentException.class,
      () -> new RaceBest("A01", null, 50, BestKind.RACE, StartType.DIVE, 21.40, LocalDate.of(2025, 2, 6))
    );
  }

  @Test void futureDate() {
    assertThrows(IllegalArgumentException.class,
      () -> new RaceBest("A01", StrokeType.FREESTYLE, 50, BestKind.RACE, StartType.DIVE, 21.40, LocalDate.now().plusDays(10))
    );
  }
}
