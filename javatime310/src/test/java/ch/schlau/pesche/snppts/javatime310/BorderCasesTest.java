package ch.schlau.pesche.snppts.javatime310;

import static ch.schlau.pesche.snppts.javatime310.BorderCases.DATE_BEFORE_CHRIST;
import static ch.schlau.pesche.snppts.javatime310.BorderCases.WEEK_FROM_NEXT_YEAR_20181231;
import static ch.schlau.pesche.snppts.javatime310.BorderCases.WEEK_LOCALE_DEPENDENT_20171231;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class BorderCasesTest {

    private static final Locale SWITZERLAND_DE = new Locale("de", "ch");
    private static final Locale SWITZERLAND_FR = new Locale("fr", "ch");

    @Test
    void week_localeDependent_output() {

        // given
        final DateTimeFormatter dateSundayFirst = DateTimeFormatter.ofPattern("YYYY-MM-dd", Locale.US);
        final DateTimeFormatter dateMondayFirst = DateTimeFormatter.ofPattern("YYYY-MM-dd", SWITZERLAND_DE);

        // when
        final LocalDate ld = LocalDate.parse(WEEK_LOCALE_DEPENDENT_20171231, DateTimeFormatter.ISO_DATE);

        // then
        assertThat(ld.format(dateSundayFirst), is("2018-12-31"));
        assertThat(ld.format(dateMondayFirst), is("2017-12-31"));
    }

    @Test
    void week_fromNextYear_output() {

        // given
        final DateTimeFormatter yearOfEra = DateTimeFormatter.ofPattern("yyyy-MM-dd", SWITZERLAND_DE);
        final DateTimeFormatter weekBasedYear = DateTimeFormatter.ofPattern("YYYY-MM-dd", SWITZERLAND_DE);

        // when
        final LocalDate ld = LocalDate.parse(WEEK_FROM_NEXT_YEAR_20181231, DateTimeFormatter.ISO_DATE);

        // then
        assertThat(ld.format(yearOfEra), is("2018-12-31"));
        assertThat(ld.format(weekBasedYear), is("2019-12-31"));
    }

    @Test
    void week_fromNextYear_parse() {

        // given
        final DateTimeFormatter prolepticYear = DateTimeFormatter.ofPattern("uuuu-MM-dd", SWITZERLAND_DE);
        final DateTimeFormatter yearOfEra = DateTimeFormatter.ofPattern("yyyy-MM-dd", SWITZERLAND_DE);
//        final DateTimeFormatter weekBasedYear = DateTimeFormatter.ofPattern("YYYY-MM-dd", SWITZERLAND_DE);

        // when
        final LocalDate ldProleptic = LocalDate.parse(WEEK_FROM_NEXT_YEAR_20181231, prolepticYear);
        final LocalDate ldEra = LocalDate.parse(WEEK_FROM_NEXT_YEAR_20181231, yearOfEra);
//        final LocalDate ldWeekBased = LocalDate.parse(WEEK_FROM_NEXT_YEAR_20181231, weekBasedYear);

        // then
        assertThat(ldProleptic.getYear(), is(2018));
        assertThat(ldEra.getYear(), is(2018));
//        assertThat(ldWeekBased.getYear(), is(1000));
    }

    @Test
    void date_beforeChrist_output() {

        // given
        final DateTimeFormatter prolepticYear = DateTimeFormatter.ofPattern("uuuu-MM-dd", SWITZERLAND_FR);
        final DateTimeFormatter yearOfEra = DateTimeFormatter.ofPattern("yyyy-MM-dd G", SWITZERLAND_DE);

        // when
        final LocalDate ld = LocalDate.parse(DATE_BEFORE_CHRIST, DateTimeFormatter.ISO_DATE);

        // then
        assertThat(ld.format(prolepticYear), is("-0001-07-31"));
        assertThat(ld.format(yearOfEra), is("0002-07-31 v. Chr."));
    }
}