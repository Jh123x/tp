package jimmy.mcgymmy.logic.parser;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import javafx.util.Pair;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.commons.util.FileUtil;
import jimmy.mcgymmy.commons.util.StringUtil;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String validationRegex = "[0-9]+";
        String trimmedIndex = oneBasedIndex.trim();

        // if contains non-digit -> invalid index
        if (!trimmedIndex.matches(validationRegex)) {
            throw new ParseException(Index.MESSAGE_INVALID_INDEX);
        }

        // contains all digit but still cannot parse -> index to large
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(Index.MESSAGE_INDEX_TOO_LARGE);
        }

        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        try {
            return new Name(trimmedName);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Gets integer from string value.
     *
     * @param value        String containing value of nutrient.
     * @param errorMessage String containing the error message.
     * @return Integer value of string.
     * @throws ParseException if the value is invalid.
     */
    private static int getNutrientValue(String value, String errorMessage) throws ParseException {
        requireNonNull(value);
        String trimmedValue = value.trim();
        int nutrientValue;
        try {
            nutrientValue = Integer.parseInt(trimmedValue);
        } catch (NumberFormatException numberFormatException) {
            throw new ParseException(errorMessage);
        }
        return nutrientValue;
    }

    /**
     * Parses a {@code String protein} into a {@code Protein}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Protein} is invalid.
     */
    public static Protein parseProtein(String protein) throws ParseException {
        int proteinValue = getNutrientValue(protein, Protein.MESSAGE_CONSTRAINTS);
        try {
            return new Protein(proteinValue);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage());
        }
    }


    /**
     * Parses a {@code String carb} into an {@code Carbohydrate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code carb} is invalid.
     */
    public static Carbohydrate parseCarb(String carb) throws ParseException {
        int carbValue = getNutrientValue(carb, Carbohydrate.MESSAGE_CONSTRAINTS);
        try {
            return new Carbohydrate(carbValue);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses a {@code String fat} into an {@code Fat}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code fat} is invalid.
     */
    public static Fat parseFat(String fat) throws ParseException {
        int fatValue = getNutrientValue(fat, Fat.MESSAGE_CONSTRAINTS);
        try {
            return new Fat(fatValue);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        try {
            return new Tag(trimmedTag);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String filepath} into an {@code File}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code filepath} is invalid.
     */
    public static Path parseFile(String filepath) throws ParseException {

        //If filepath is invalid or does not exist throw an exception
        if (!FileUtil.isValidPath(filepath)) {
            throw new ParseException(String.format("Invalid Filepath %s", filepath));
        }

        if (!FileUtil.isFileExists(Path.of(filepath))) {
            throw new ParseException(String.format("File at %s does not exist", filepath));
        }

        //Return Path object of file
        return Path.of(filepath);
    }

    /**
     * Parses {@Code String directory} into a {@code File}.
     *
     * @param directory Directory to store the file.
     * @return Path containing directoryPath.
     * @throws ParseException if directory does not exist.
     */
    public static Path parseDir(String directory) throws ParseException {
        //Create the directory
        Path path = Path.of(directory);
        File file = new File(directory);
        if (!file.exists() || !file.isDirectory()) {
            throw new ParseException(String.format("Directory does not exist %s", path.toString()));
        }
        return path;
    }

    /**
     * Parse {@code String outputName} into a {@code String}.
     *
     * @param outputPath Name to save the output file.
     * @return outputPath as string.
     * @throws ParseException when outputPath is empty.
     */
    public static String parseOutputName(String outputPath) throws ParseException {
        outputPath = outputPath.trim();
        if (outputPath.equals("")) {
            throw new ParseException("Filename cannot be empty");
        }
        if (!outputPath.contains(".json")) {
            outputPath += ".json";
        }
        return outputPath;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Date parseDate(String date) throws ParseException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        try {
            return new Date(trimmedDate);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Helper function for commons-cli's HelpFormatter.
     * Generates the usage string from commons-cli options.
     *
     * @param commandName name of the command.
     * @param header extra text to be included before usage statement.
     * @param options commons-cli options to format.
     * @return usage string.
     */
    public static String getUsageFromHelpFormatter(String commandName, String header, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        formatter.printHelp(
            printWriter,
            formatter.getWidth(),
            commandName,
            header,
            options,
            formatter.getLeftPadding(),
            formatter.getDescPadding(),
            "");
        return stringWriter.toString();
    }

    /**
     * Splits a string using the delimiter,
     * storing the first string as the head, and the rest as the tail.
     *
     * @param input raw input string.
     * @param delimiter Java regex string to split the string by.
     * @return Pair of (first word, array of the rest of the words)
     */
    public static Pair<String, String[]> splitString(String input, String delimiter) {
        try {
            String[] headTail = input.split(delimiter);
            return new Pair<>(headTail[0], Arrays.copyOfRange(headTail, 1, headTail.length));
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Pair<>("", new String[]{""});
        }
    }

    /**
     * Splits a string by whitespaces,
     * storing the first string as the head, and the rest as the tail.
     *
     * @param input raw input string.
     * @return Pair of (first word, array of the rest of the words)
     */
    public static Pair<String, String[]> splitString(String input) {
        return splitString(input, "\\s+");
    }
}
