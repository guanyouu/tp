package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Remark;

public class JsonAdaptedRemarkTest {

    private static final String VALID_TEXT = "Participates actively in class";
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 4, 7);
    private static final String VALID_DATE_STRING = "2026-04-07";

    private static final String INVALID_DATE = "07-04-2026";

    @Test
    public void toModelType_validRemarkDetails_returnsRemark() throws Exception {
        JsonAdaptedRemark jsonAdaptedRemark = new JsonAdaptedRemark(VALID_TEXT, VALID_DATE_STRING);
        Remark expectedRemark = new Remark(VALID_TEXT, VALID_DATE);

        assertEquals(expectedRemark, jsonAdaptedRemark.toModelType());
    }

    @Test
    public void toModelType_nullText_throwsIllegalValueException() {
        JsonAdaptedRemark jsonAdaptedRemark = new JsonAdaptedRemark(null, VALID_DATE_STRING);

        String expectedMessage = String.format(JsonAdaptedRemark.MISSING_FIELD_MESSAGE_FORMAT, "text");
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedRemark::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedRemark jsonAdaptedRemark = new JsonAdaptedRemark(VALID_TEXT, null);

        String expectedMessage = String.format(JsonAdaptedRemark.MISSING_FIELD_MESSAGE_FORMAT, "date");
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedRemark::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedRemark jsonAdaptedRemark = new JsonAdaptedRemark(VALID_TEXT, INVALID_DATE);

        assertThrows(IllegalValueException.class,
                "Invalid remark date: " + INVALID_DATE,
                jsonAdaptedRemark::toModelType);
    }

    @Test
    public void constructor_fromModelType_correctlyCopiesFields() throws Exception {
        Remark remark = new Remark(VALID_TEXT, VALID_DATE);
        JsonAdaptedRemark jsonAdaptedRemark = new JsonAdaptedRemark(remark);

        assertEquals(remark, jsonAdaptedRemark.toModelType());
    }
}
