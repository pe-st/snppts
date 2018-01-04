package ch.schlau.pesche.snppts.javatime310;

public class BorderCases {

    /**
     * Depending on the locale, this date belongs to week 1 of 2018 or week 52 of 2017
     */
    public static final String WEEK_LOCALE_DEPENDENT_20171231 = "2017-12-31";

    /**
     * This date belongs to week 1 of the next year
     */
    public static final String WEEK_FROM_NEXT_YEAR_20181231 = "2018-12-31";

    /**
     * This date is before the birth of Christ
     */
    public static final String DATE_BEFORE_CHRIST = "-0001-07-31";
}
