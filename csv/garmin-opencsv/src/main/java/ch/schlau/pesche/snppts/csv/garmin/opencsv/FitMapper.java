package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FitMapper {

    FitMapper INSTANCE = Mappers.getMapper(FitMapper.class);

    @Mapping(source = "beginTimestamp", target = "datum")
    @Mapping(source = "distance", target = "km")
    @Mapping(source = "duration", target = "mmSs", qualifiedByName = "MMSS")
    Fit activityToFit(Activity activity);

    default LocalDate map(LocalDateTime value) {
        return value == null
                ? null
                : value.toLocalDate();
    }

    /**
     * Convert seconds into minutes with the seconds as decimal fraction from 00 to 59
     *
     * @param duration
     * @return
     */
    @Named("MMSS")
    default double mmss(double duration) {
        int minutes = (int) (duration / 60);
        long seconds = Math.round(duration % 60);

        return minutes + (double) seconds / 100;
    }
}
