package ch.schlau.pesche.snppts.csv.garmin.supercsv;

import java.time.LocalDate;

public class Fit {

    private LocalDate date;

    private double km;

    private Integer shoes;

    private Integer elevationGain;

    private double mmSs;

    private Double minutes;

    private Double pace;

    private Double gradeAdjustedPace;

    private String name;

    private Integer heartRate;

    private Integer calories;

    private Integer caloriesPercent;

    private String notes;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public Integer getShoes() {
        return shoes;
    }

    public void setShoes(Integer shoes) {
        this.shoes = shoes;
    }

    public Integer getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(Integer elevationGain) {
        this.elevationGain = elevationGain;
    }

    public double getMmSs() {
        return mmSs;
    }

    public void setMmSs(double mmSs) {
        this.mmSs = mmSs;
    }

    public Double getMinutes() {
        return minutes;
    }

    public void setMinutes(Double minutes) {
        this.minutes = minutes;
    }

    public Double getPace() {
        return pace;
    }

    public void setPace(Double pace) {
        this.pace = pace;
    }

    public Double getGradeAdjustedPace() {
        return gradeAdjustedPace;
    }

    public void setGradeAdjustedPace(Double gradeAdjustedPace) {
        this.gradeAdjustedPace = gradeAdjustedPace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getCaloriesPercent() {
        return caloriesPercent;
    }

    public void setCaloriesPercent(Integer caloriesPercent) {
        this.caloriesPercent = caloriesPercent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
