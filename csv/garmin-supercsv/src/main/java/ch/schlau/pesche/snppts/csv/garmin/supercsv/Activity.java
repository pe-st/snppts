package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import java.time.LocalDateTime;

public class Activity {

    private String id;

    private String name;

    private String description;

    private LocalDateTime beginTimestamp;

    private String activityType;

    private Integer heartRate;

    private Double calories;

    private double duration;

    private double distance;

    private Double elevationGain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getBeginTimestamp() {
        return beginTimestamp;
    }

    public void setBeginTimestamp(LocalDateTime beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(Double elevationGain) {
        this.elevationGain = elevationGain;
    }
}
