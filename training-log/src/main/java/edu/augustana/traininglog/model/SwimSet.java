package edu.augustana.traininglog.model;
import java.util.List;

public record SwimSet(int round, StrokeType stroke, int repDistance, double sendOffInterval, List<Double> repTimes) {
  // constructor with validation and then copy
  public SwimSet {

    // check to see if round input are less than 1
    if(round < 1) {
      throw new IllegalArgumentException("set round must be at least 1.");
    }

    // check to see if the stroke input is null
    if(stroke == null) {
      throw new IllegalArgumentException("stroke type must be: FREESTYLE, BACKSTROKE, BREASTSTROKE or BUTTERFLY.");
    }

    if(repDistance <= 0) {
      throw new IllegalArgumentException("rep distance must be greater than 0.");
    }

    if(sendOffInterval <= 0) {
      throw new IllegalArgumentException("send-off interval must be greater than 0");
    }

    // reptTimes check 1: if the list is null
    if(repTimes == null) {
      throw new IllegalArgumentException("rep times list must not be null.");
    }

    // repTimes check 2: if the list is empty
    if(repTimes.isEmpty()) {
      throw new IllegalArgumentException("rep times list must not be empty");
    }

    // repTimes check 3: check if each time is <= 0
    // loop that runs through the reptimes list
    // used Double object type so it can be compared to null
    for (Double time : repTimes) {
      if(time == null || time <= 0) {
        throw new IllegalArgumentException("times must be inputted as doubles and cannot be less or equal to 0.");
      }
    }

    // giving tthe set its own and unmodifiable list.
    repTimes = List.copyOf(repTimes);
  }

  // returns how many rep times were stored in the List
  public int repCount() {
    return repTimes.size();
  }

  // method to calculate the average rest during a set
  public double averageRest() {
    double sum = 0.0;

    for (double time : repTimes) {
      sum += Math.max(0.0, sendOffInterval - time);
    }

    double average = sum / repTimes.size();

    return average;
  }
}
