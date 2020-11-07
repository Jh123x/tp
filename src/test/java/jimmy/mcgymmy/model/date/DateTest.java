package jimmy.mcgymmy.model.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

/**
 *  "dd-M-yyyy",
 *  "yyyy/MM/dd",
 *  "dd/MM/yyyy",
 *  "dd/M/yyyy",
 *  "d/M/yyyy",
 */

class DateTest {
    private static final String INVALID_DAY_STRING = "2019-04-31";
    private static final String INVALID_YEAR_STRING = "-100-12-28";
    private static final String INVALID_MONTH_STRING = "2019-13-28";
    private static final String INVALID_DATE_STRING_1 = "31/13/2020";
    private static final String INVALID_FORMAT_STRING_2 = "20/04/20";
    private static final String INVALID_FORMAT_STRING_1 = "20//04/2020";
    private static final String VALID_DATE_STRING_2_D_MMM_YYYY = "2 Sep 2020";
    private static final String VALID_DATE_STRING_1_YYYY_MM_DD = "2020-04-20";

    private static final String VALID_DATE_STRING_1_DD_MMM_YYYY = "20 Apr 2020";

    private static final String VALID_DATE_STRING_1_SLASH_D_M_YYYY = "2/9/2020";
    private static final String VALID_DATE_STRING_1_SLASH_DD_M_YYYY = "20/4/2020";
    private static final String VALID_DATE_STRING_1_SLASH_YYYY_MM_DD = "2020/04/20";
    private static final String VALID_DATE_STRING_1_SLASH_DD_MM_YYYY = "20/04/2020";

    private static final String VALID_DATE_STRING_1_DASH_D_M_YYYY = "2-9-2020";
    private static final String VALID_DATE_STRING_1_DASH_YYYY_M_D = "2020-9-2";
    private static final String VALID_DATE_STRING_1_DASH_DD_MM_YYYY = "20-04-2020";
    private static final String VALID_DATE_STRING_1_DASH_YYYY_M_DD = "2020-4-20";
    private static final String VALID_DATE_STRING_1_DASH_DD_M_YYYY = "20-4-2020";

    private static final String VALID_DATE_STRING_LEAP_YEAR_FEB = "29 Feb 2020";
    private static final String INVALID_DATE_STRING_NON_LEAP_YEAR_FEB = "29 Feb 2019";

    @Test
    void currentDate_returnsCorrectCurrentDate() throws IllegalValueException {
        assertEquals(Date.currentDate(), new Date(LocalDate.now().toString()));
    }

    @Test
    void leapYearValidTest() throws IllegalValueException {
        //Valid leap year Date
        assertEquals(new Date(VALID_DATE_STRING_LEAP_YEAR_FEB).toString(), VALID_DATE_STRING_LEAP_YEAR_FEB);

        //Invalid leap year date
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_DATE_STRING_NON_LEAP_YEAR_FEB));

    }

    @Test
    void constructorAndToString_supportedFormat_createsDateObject() throws IllegalValueException {
        // format: "d/M/yyyy",
        assertEquals(new Date(VALID_DATE_STRING_1_SLASH_D_M_YYYY).toString(), VALID_DATE_STRING_2_D_MMM_YYYY);
        // format: "d-M-yyyy"
        assertEquals(new Date(VALID_DATE_STRING_1_DASH_D_M_YYYY).toString(), VALID_DATE_STRING_2_D_MMM_YYYY);
        // format: "yyyy-M-d"
        assertEquals(new Date(VALID_DATE_STRING_1_DASH_YYYY_M_D).toString(), VALID_DATE_STRING_2_D_MMM_YYYY);
        // format: "yyyy-M-dd"
        assertEquals(new Date(VALID_DATE_STRING_1_DASH_YYYY_M_DD).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "dd-M-yyyy"
        assertEquals(new Date(VALID_DATE_STRING_1_DASH_DD_M_YYYY).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "dd/M/yyyy"
        assertEquals(new Date(VALID_DATE_STRING_1_SLASH_DD_M_YYYY).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "dd-MM-yyyy"
        assertEquals(new Date(VALID_DATE_STRING_1_DASH_DD_MM_YYYY).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "yyyy/MM/dd"
        assertEquals(new Date(VALID_DATE_STRING_1_SLASH_YYYY_MM_DD).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "dd/MM/yyyy"
        assertEquals(new Date(VALID_DATE_STRING_1_SLASH_DD_MM_YYYY).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "dd MMM yyyy"
        assertEquals(new Date(VALID_DATE_STRING_1_DD_MMM_YYYY).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);
        // format: "yyyy-MM-dd"
        assertEquals(new Date(VALID_DATE_STRING_1_YYYY_MM_DD).toString(), VALID_DATE_STRING_1_DD_MMM_YYYY);

    }

    @Test
    void constructor_unsupportedFormat_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_DAY_STRING));
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_YEAR_STRING));
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_DATE_STRING_1));
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_FORMAT_STRING_2));
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_FORMAT_STRING_1));
        assertThrows(IllegalValueException.class, () -> new Date(INVALID_MONTH_STRING));

    }

    @Test
    void equals_returnsCorrectComparison() throws IllegalValueException {
        Date testDate1 = new Date(VALID_DATE_STRING_2_D_MMM_YYYY);
        Date testDate2 = new Date(VALID_DATE_STRING_1_DD_MMM_YYYY);


        // identical -> returns true
        assertEquals(testDate2, testDate2);

        // same date -> returns true
        assertEquals(new Date(VALID_DATE_STRING_1_YYYY_MM_DD), testDate2);

        // different date -> returns false
        assertNotEquals(testDate1, testDate2);

        // not instance of Date -> returns false
        assertNotEquals(VALID_DATE_STRING_1_DD_MMM_YYYY, testDate2);
    }

    @Test
    void hasCode_returnsCorrectHashCode() throws IllegalValueException {
        Date actual = new Date(VALID_DATE_STRING_1_YYYY_MM_DD);
        LocalDate expected = LocalDate.parse(VALID_DATE_STRING_1_YYYY_MM_DD);
        assertEquals(actual.hashCode(), expected.hashCode());
    }
}
