package jimmy.mcgymmy.model.date;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.commons.util.AppUtil;

public class Date {
    private static final String[] SUPPORTED_FORMATS = {
        "yyyy-MM-dd",
        "yyyy-M-dd",
        "yyyy-M-d",
        "dd-MM-yyyy",
        "dd-M-yyyy",
        "d-M-yyyy",
        "yyyy/MM/dd",
        "dd/MM/yyyy",
        "dd/M/yyyy",
        "d/M/yyyy",
        "d MMM yyyy"
    };

    public static final String MESSAGE_CONSTRAINTS = "Date string should be in one of the following formats:\n"
            + String.join("\n", SUPPORTED_FORMATS);

    private static final String OUTPUT_FORMAT = "d MMM yyyy";

    private LocalDate date;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date string.
     */
    public Date(String date) throws IllegalValueException {
        requireNonNull(date);
        Optional<String> format = isValid(date);
        AppUtil.checkArgument(format.isPresent(), MESSAGE_CONSTRAINTS);
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern(format.get()));
    }

    private Date() {
        date = LocalDate.now();
    }

    /**
     * Construct a {@code date} that contains the current date
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * Checks of the date format is valid.
     * @param date Date as a String.
     * @return If date can be parsed
     */
    public static Optional<String> isValid(String date) {
        String correctFormat = "";
        for (String format : SUPPORTED_FORMATS) {
            try {
                // check if the date is in this format
                LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
                return Optional.of(format);
            } catch (DateTimeParseException e) {
                /* exception is thrown means that cannot parse date in that format
                if cannot parse date in any format, throw IllegalArgumentException
                using checkArgument below.
                implicit `continue;` here
                */
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
            || (other instanceof Date)
            && this.date.equals(((Date) other).date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern(OUTPUT_FORMAT));
    }
}
