package edu.augustana.traininglog.model;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SwimSetTest {
  @Test 
  void validAverage() {
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 50, 60, List.of(25.0, 25.5, 26.0, 30.0));

    double averageTest = test.averageRest();

    // test the average
    assertEquals(33.375, averageTest, 0.001);

    // test rep count
    assertEquals(4, test.repCount());
  }

  @Test
  void repSlower() {
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 100, 90, List.of(88.0, 92.00));

    double averageRest = test.averageRest();

    assertEquals(1.0, averageRest, 0.001);
  }

  @Test 
  void floatNumDIstance() {
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 12.5, 30, List.of(8.0, 8.5, 9.0));

    assertEquals(12.5, test.repDistance(), 0.001);
  }

  @Test
  void everyRepSlower() {
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 50, 30, List.of(35.0, 40.0));

    double averageRest = test.averageRest();

    assertEquals(0.0, averageRest, 0.001);
  }

  @Test
  void singleRep() {
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 50, 60, List.of(25.00));

    double averageRest = test.averageRest();

    // rep average
    assertEquals(35.0, averageRest, 0.001);

    // rep count
    assertEquals(1, test.repCount());
  }

  @Test
  void lowerReps() {
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 15, 60, List.of(8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0));

    double averageRest = test.averageRest();

    assertEquals(52.0, averageRest, 0.001);

    assertEquals(10, test.repCount());
  }

  @Test 
  void invalidRound() {
    assertThrows(IllegalArgumentException.class,
     () -> new SwimSet(-1, StrokeType.FREESTYLE, 50, 60, List.of(25.0, 25.5, 26.0, 30.0))
    );

    assertThrows(IllegalArgumentException.class,
     () -> new SwimSet(0, StrokeType.FREESTYLE, 50, 60, List.of(25.0, 25.5, 26.0, 30.0))
    );
  }

  @Test 
  void missingStroke() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, null, 50, 60, List.of(25.0, 25.5, 26.0, 30.0))
    );
  }

  @Test
  void zeroDistance() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, StrokeType.FREESTYLE, 0, 60, List.of(25.0, 25.5, 26.0, 30.0))
    );
  }

  @Test
  void zeroInterval() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, StrokeType.FREESTYLE, 50, 0, List.of(25.0, 25.5, 26.0, 30.0))
    );
  }

  @Test
  void emptyList() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, StrokeType.FREESTYLE, 50, 60, List.of())
    );
  }

  @Test 
  void noList() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, StrokeType.FREESTYLE, 50, 60, null)
    );
  }

  @Test
  void invalidRepNegative() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, StrokeType.FREESTYLE, 50, 60, List.of(-1.0))
    );
  }

  @Test 
  void invalidRepZero() {
    assertThrows(IllegalArgumentException.class, ()-> new SwimSet(1, StrokeType.FREESTYLE, 50, 60, List.of(0.0))
    );
  }

  @Test 
  void invalidRepNull() {
    List<Double> timesWithNull = new ArrayList<>();
    timesWithNull.add(25.0);
    timesWithNull.add(null);
    timesWithNull.add(27.5);
    assertThrows(IllegalArgumentException.class,
     () -> new SwimSet(1, StrokeType.FREESTYLE, 50, 60, timesWithNull)
    );
  }

  @Test
  // test to check if the set keeps a refrence to the original list
  void changeSet() {
    List<Double> changeSet = new ArrayList<>();
    changeSet.add(40.0);
    changeSet.add(45.0);
    changeSet.add(47.5);
    changeSet.add(50.0);
    
    SwimSet test = new SwimSet(1, StrokeType.FREESTYLE, 50, 60, changeSet);

    changeSet.add(55.0);

    assertEquals(4, test.repCount());
  }
}
