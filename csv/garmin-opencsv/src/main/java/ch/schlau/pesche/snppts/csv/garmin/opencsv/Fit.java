package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByPosition;

public class Fit {

    @CsvBindByPosition(position = 0)
    private LocalDate date;

    @CsvBindByPosition(position = 1)
    private double km;

    @CsvBindByPosition(position = 2)
    private Integer shoes;

    @CsvBindByPosition(position = 3)
    private Integer elevationGain;

    @CsvBindByPosition(position = 4)
    private double mmSs;

    @CsvBindByPosition(position = 5)
    private Double minutes;

    @CsvBindByPosition(position = 6)
    private Double pace;

    @CsvBindByPosition(position = 7)
    private Double gradeAdjustedPace;

    @CsvBindByPosition(position = 8)
    private String name;

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
}
