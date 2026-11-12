package edu.augustana.traininglog.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

class WorkoutTest {
  // helper method creates a set. 
  // take position as an argument so I can modify it when I need to test my sets positions.
  private SwimSet setAtPosition(int position) {
    return new SwimSet(position, StrokeType.FREESTYLE, 50, 60, List.of(25.0, 25.5, 26.0, 30.0));
  }

  @Test 
  void validWorkout() {

    List<SwimSet> sets = List.of(setAtPosition(1), setAtPosition(2), setAtPosition(3));

    Workout test = new Workout("A01", LocalDate.of(2026, 11, 9), sets);

    assertEquals(3, test.setCount());
  }

  @Test 
  void singleSet() {
    List<SwimSet> set = List.of(setAtPosition(1));

    Workout test = new Workout("A01", LocalDate.of(2026, 11, 10), set);

    assertEquals(1, test.setCount());
  }

  @Test 
  void emptyAthlete() {
    List<SwimSet> set  = List.of(setAtPosition(1));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("", LocalDate.of(2026, 11, 9), set)
    );
  }

  @Test 
  void nullDate() {
    List<SwimSet> set  = List.of(setAtPosition(1));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", null, set)
    );
  }

  @Test
  void futureDate() {
    List<SwimSet> set  = List.of(setAtPosition(1));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.now().plusDays(3), set)
    );
  }

  @Test
  void nullList() {

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.of(2026, 11, 3), null)
    );
  }

  @Test 
  void emptyList() {
    assertThrows(IllegalArgumentException.class, 
      () -> new Workout("A01", LocalDate.of(2026, 10, 9), List.of())
    );
  }

  @Test 
  void nullSetInList() {
    ArrayList<SwimSet> sets = new ArrayList<>();

    sets.add(setAtPosition(1));
    sets.add(null);

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.of(2026, 10, 9), sets)
    );
  }

  @Test
  void gapError() {
    List<SwimSet> sets  = List.of(setAtPosition(1), setAtPosition(3));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.of(2026, 10, 8), sets)
    );
  }

  @Test
  void duplicatePositions() {
    List<SwimSet> sets = List.of(setAtPosition(1), setAtPosition(1));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.of(2026, 11, 9), sets)
    );
  }

  @Test 
  void positionNotStartAt1() {
    List<SwimSet> sets = List.of(setAtPosition(2), setAtPosition(3));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.of(2026, 11, 8), sets)
    );
  }

  @Test 
  void positionOutOfOrder() {
    List<SwimSet> sets = List.of(setAtPosition(2), setAtPosition(1));

    assertThrows(IllegalArgumentException.class,
      () -> new Workout("A01", LocalDate.of(2026, 11, 5), sets)
    );
  }

  @Test 
  void changeWorkout() {
    ArrayList<SwimSet> sets = new ArrayList<>();
    sets.add(setAtPosition(1));

    Workout test = new Workout("A01", LocalDate.of(2026, 11, 7), sets);

    sets.add(setAtPosition(2));
    
    assertEquals(1, test.setCount());
  }
}
