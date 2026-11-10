package edu.augustana.traininglog.model;
import java.time.LocalDate;

public record RaceBest(String athleteId, StrokeType stroke, double distance, BestKind kind, StartType start, double time, LocalDate date) {
  public RaceBest {
    if (athleteId == null || athleteId.isBlank()) {
      throw new IllegalArgumentException("ID must not be empty.");
    }

    if(stroke == null) {
      throw new IllegalArgumentException("stroke type must be: FREESTYLE, BACKSTROKE, BREASTSTROKE or BUTTERFLY.");
    }

    if(distance <= 0.0) {
      throw new IllegalArgumentException("distance must be positive.");
    }

    if(kind == null) {
      throw new IllegalArgumentException("kind type must be: RACE or TEST.");
    }

    if(start == null) {
      throw new IllegalArgumentException("start type must be: PUSH or DIVE.");
    }

    if(time <= 0.0) {
      throw new IllegalArgumentException("time must be positive and in seconds.");
    }

    if(date == null || date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("date must not be empty or a future date.");
    }
  }
}
