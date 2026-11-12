package edu.augustana.traininglog.model;
import java.time.LocalDate;
import java.util.List;

public record Workout(String athleteId, LocalDate date, List<SwimSet> sets) {
  public Workout {
    // check if athleteId is either null or blank.
    if(athleteId == null || athleteId.isBlank()) {
      throw new IllegalArgumentException("athlete Id must not be null or blank.");
    }

    // check if date is null or a future date.
    if(date == null || date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("date must not be null or a future date");
    }

    // check if sets is null.
    if(sets == null) {
      throw new IllegalArgumentException("sets must not be null.");
    }

    // check if sets is empty
    if(sets.isEmpty()) {
      throw new IllegalArgumentException("sets must not be empty.");
    }

    // check if the sets are empty, null, conatins null or positions with gaps or duplicates
    for(int index = 0; index < sets.size(); index++) {
      
      // gets the set at this index
      SwimSet set = sets.get(index);

      // checks if the list contains null objects
      if(set == null) {
        throw new IllegalArgumentException("position at null " + null + " cannot be null.");
      }
      
      // checks if the expected position is different from the actual index position
      int expectedPosition = index + 1;
      int actualPosition = set.position();
      
      if(expectedPosition != actualPosition) {
        throw new IllegalArgumentException("position at index " + index + " should be " + expectedPosition + " but was " + actualPosition);
      }
    }

    sets = List.copyOf(sets);
  }

  public int setCount() {
    return sets.size();
  }
}
