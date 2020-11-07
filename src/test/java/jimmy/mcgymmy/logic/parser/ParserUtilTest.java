package jimmy.mcgymmy.logic.parser;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.model.tag.Tag;
import jimmy.mcgymmy.testutil.TypicalIndexes;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PROTEIN_1 = "+651%)";
    private static final String INVALID_PROTEIN_2 = "2000";
    private static final String INVALID_FAT_1 = " ";
    private static final String INVALID_FAT_2 = "2000 ";
    private static final String INVALID_CARB_1 = "example.com";
    private static final String INVALID_CARB_2 = " 1000 ";

    private static final String INVALID_TAG = "#food";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PROTEIN = "123";
    private static final String VALID_FAT = "123";
    private static final String VALID_CARB = "213";
    private static final String VALID_TAG_1 = "food";
    private static final String VALID_TAG_2 = "lunch";

    private static final String VALID_FILE = Paths
            .get("src", "test", "data", "JsonSerializableMcGymmyTest", "typicalFoodMcGymmy.json").toString();
    private static final String INVALID_FILE_1 = Paths
            .get("src", "test", "data", "JsonSerializableMcGymmyTest", "typical.json").toString();
    private static final String VALID_DIR = Paths
            .get("src", "test", "data", "JsonSerializableMcGymmyTest").toString();
    private static final String INVALID_DIR = Paths
            .get("src", "test", "data", "J").toString();

    private static final String NO_JSON_NAME = "mc";
    private static final String JSON_NAME = "mc.json";

    private static final String INVALID_DATE = "invalid date";
    private static final String DOES_NOT_EXIST_DATE = "invalid date";
    private static final String VALID_DATE = "12/12/2020";
    private static final String VALID_DATE_2 = "12-12-2020";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        // not an integer
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
        // signed integer
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("+10"));
        // negative integer
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("-10"));
        // zero
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("0"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, Index.MESSAGE_INDEX_TOO_LARGE, () ->
                ParserUtil.parseIndex(Long.toString((long) Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(TypicalIndexes.INDEX_FIRST_FOOD, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(TypicalIndexes.INDEX_FIRST_FOOD, ParserUtil.parseIndex("  1  "));

        // long string
        assertEquals(TypicalIndexes.INDEX_FIRST_FOOD, ParserUtil.parseIndex("00000000000000000000000000000000000001"));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName(null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseProtein_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseProtein(null));
    }

    @Test
    public void parseProtein_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseProtein(INVALID_PROTEIN_1));
        assertThrows(ParseException.class, () -> ParserUtil.parseProtein(INVALID_PROTEIN_2));
    }

    @Test
    public void parseProtein_validValueWithoutWhitespace_returnsProtein() throws Exception {
        Protein expectedProtein = new Protein(Integer.parseInt(VALID_PROTEIN));
        assertEquals(expectedProtein, ParserUtil.parseProtein(VALID_PROTEIN));
    }

    @Test
    public void parseProtein_validValueWithWhitespace_returnsTrimmedProtein() throws Exception {
        String proteinWithWhitespace = WHITESPACE + VALID_PROTEIN + WHITESPACE;
        Protein expectedProtein = new Protein(Integer.parseInt(VALID_PROTEIN));
        assertEquals(expectedProtein, ParserUtil.parseProtein(proteinWithWhitespace));
    }

    @Test
    public void parseCarb_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseCarb(null));
    }

    @Test
    public void parseCarb_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCarb(INVALID_CARB_1));
        assertThrows(ParseException.class, () -> ParserUtil.parseCarb(INVALID_CARB_2));
    }

    @Test
    public void parseCarb_validValueWithoutWhitespace_returnsCarbohydrate() throws Exception {
        Carbohydrate expectedCarbohydrate = new Carbohydrate(Integer.parseInt(VALID_CARB));
        Assertions.assertEquals(expectedCarbohydrate, ParserUtil.parseCarb(VALID_CARB));
    }

    @Test
    public void parseCarb_validValueWithWhitespace_returnsTrimmedCarbohydrate() throws Exception {
        String carbohydrateWithWhitespace = WHITESPACE + VALID_CARB + WHITESPACE;
        Carbohydrate expectedCarbohydrate = new Carbohydrate(Integer.parseInt(VALID_CARB));
        Assertions.assertEquals(expectedCarbohydrate, ParserUtil.parseCarb(carbohydrateWithWhitespace));
    }

    @Test
    public void parseFat_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseFat(null));
    }

    @Test
    public void parseFat_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseFat(INVALID_FAT_1));
        assertThrows(ParseException.class, () -> ParserUtil.parseFat(INVALID_FAT_2));
    }

    @Test
    public void parseFat_validValueWithoutWhitespace_returnsFat() throws Exception {
        Fat expectedFat = new Fat(Integer.parseInt(VALID_FAT));
        assertEquals(expectedFat, ParserUtil.parseFat(VALID_FAT));
    }

    @Test
    public void parseFat_validValueWithWhitespace_returnsTrimmedFat() throws Exception {
        String fatWithWhitespace = WHITESPACE + VALID_FAT + WHITESPACE;
        Fat expectedFat = new Fat(Integer.parseInt(VALID_FAT));
        assertEquals(expectedFat, ParserUtil.parseFat(fatWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseFile_withInvalidFilePath() {
        assertThrows(ParseException.class, () -> ParserUtil.parseFile(INVALID_FILE_1));
    }

    @Test
    public void parseFile_withFolderPath() {
        assertThrows(ParseException.class, () -> ParserUtil.parseFile(VALID_DIR));
    }

    @Test
    public void parseFile_withValidFilePath() throws ParseException {
        Path path = Paths.get(VALID_FILE);
        assertEquals(path, ParserUtil.parseFile(VALID_FILE));
    }

    @Test
    public void parseDir_withInvalidFolderPath() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDir(VALID_FILE));
    }

    @Test
    public void parseDir_withNotFoundFolderPath() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDir(INVALID_DIR));
    }

    @Test
    public void parseDir_withValidFolderPath() throws Exception {
        Path path = Paths.get(VALID_DIR);
        assertEquals(path, ParserUtil.parseDir(VALID_DIR));
    }

    @Test
    public void parseOutputName_withEmptyString() {
        //Empty name
        assertThrows(ParseException.class, () -> ParserUtil.parseOutputName(""));

        //With spaces
        assertThrows(ParseException.class, () -> ParserUtil.parseOutputName(" "));
        assertThrows(ParseException.class, () -> ParserUtil.parseOutputName("  "));
    }

    @Test
    public void parseOutputName_withNoJsonName() throws Exception {
        //No spaces around it
        String name = ParserUtil.parseOutputName(NO_JSON_NAME);
        assertEquals(name, NO_JSON_NAME + ".json");

        //Leading and trailing spaces
        String name2 = ParserUtil.parseOutputName(" " + NO_JSON_NAME + " ");
        assertEquals(name2, NO_JSON_NAME + ".json");
    }

    @Test
    public void parseOutputName_withJsonName() throws Exception {

        //Test valid name
        String name = ParserUtil.parseOutputName(JSON_NAME);
        assertEquals(name, JSON_NAME);

        //Test with leading and trailing spaces
        String name2 = ParserUtil.parseOutputName(" " + JSON_NAME + " ");
        assertEquals(name2, JSON_NAME);
    }

    @Test
    public void parseDate_withInvalidDate() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate(INVALID_DATE));
    }

    @Test
    public void parseDate_withNoSuchDate() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate(DOES_NOT_EXIST_DATE));
    }

    @Test
    public void parseDate_withValidDate() throws Exception {
        Date date = ParserUtil.parseDate(VALID_DATE);
        Date date2 = ParserUtil.parseDate(VALID_DATE_2);
        assertEquals(date, date2);
    }
}
