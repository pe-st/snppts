package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDateTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import ch.schlau.pesche.snppts.csv.garmin.opencsv.converter.LocalDateTimeConverterAlmostRfc1123;

public class Activity {

    @CsvBindByName(column = "Activity ID")
    private String id;

    @CsvBindByName(column = "Activity Name")
    private String name;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvCustomBindByName(column = "Begin Timestamp", converter = LocalDateTimeConverterAlmostRfc1123.class)
    private LocalDateTime beginTimestamp;

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
}
