package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByPosition;

public class Fit {

    @CsvBindByPosition(position = 0)
    private LocalDate datum;

    @CsvBindByPosition(position = 1)
    private double km;

    @CsvBindByPosition(position = 2)
    private Integer shoes;

    @CsvBindByPosition(position = 3)
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
