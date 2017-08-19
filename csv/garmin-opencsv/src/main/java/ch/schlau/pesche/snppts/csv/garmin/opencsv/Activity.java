package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDateTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import ch.schlau.pesche.snppts.csv.garmin.opencsv.converter.LocalDateTimeConverterAlmostRfc1123;

public class Activity {

    public static final String ACTIVITY_TYPE = "Activity Type";

    @CsvBindByName(column = "Activity ID")
    private String id;

    @CsvBindByName(column = "Activity Name")
    private String name;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvCustomBindByName(column = "Begin Timestamp", converter = LocalDateTimeConverterAlmostRfc1123.class)
    private LocalDateTime beginTimestamp;

    @CsvBindByName(column = ACTIVITY_TYPE)
    private String activityType;

    @CsvBindByName(column = "Average Heart Rate (bpm)")
    private Integer heartRate;

    @CsvBindByName(column = "Calories (Raw)")
    private Double calories;

    @CsvBindByName(column = "Duration (Raw Seconds)")
    private double duration;

    @CsvBindByName(column = "Distance (Raw)")
    private double distance;

    @CsvBindByName(column = "Elevation Gain (Raw)")
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
