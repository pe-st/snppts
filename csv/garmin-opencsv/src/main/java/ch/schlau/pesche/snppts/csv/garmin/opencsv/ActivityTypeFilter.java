package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.MappingStrategy;

public class ActivityTypeFilter implements CsvToBeanFilter {

    private final MappingStrategy strategy;
    private final String activityType;

    public ActivityTypeFilter(MappingStrategy strategy, String activityType) {
        this.strategy = strategy;
        this.activityType = activityType;
    }

    @Override
    public boolean allowLine(String[] line) {
        if (activityType == null || activityType.isEmpty()) {
            return true;
        }

        Integer index = strategy.getColumnIndex(Activity.ACTIVITY_TYPE);
        if (index != null) {
            String value = line[index];
            return activityType.equals(value);
        }
        return true;
    }
}
