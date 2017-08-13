package ch.schlau.pesche.snppts.csv.garmin.opencsv;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FitMapper {

    FitMapper INSTANCE = Mappers.getMapper(FitMapper.class);

    @Mapping(source = "beginTimestamp", target = "datum")
    @Mapping(source = "distance", target = "km")
    Fit activityToFit(Activity activity);

    default LocalDate map(LocalDateTime value) {
        return value == null
                ? null
                : value.toLocalDate();
    }
}
