package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDate;

public class Fit {

    private LocalDate datum;

    private double km;

    private Integer shoes;

    private Integer elevationGain;

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
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
}
