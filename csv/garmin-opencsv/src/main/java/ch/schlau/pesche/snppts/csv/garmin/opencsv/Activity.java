package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import com.opencsv.bean.CsvBindByName;

public class Activity {

    @CsvBindByName(column = "Activity ID")
    private String id;

    @CsvBindByName(column = "Activity Name")
    private String name;

    @CsvBindByName(column = "Description")
    private String description;

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
}
