import application.utils.ValidationStatus;
import autofixture.publicinterface.Any;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;

public class ValidationResultSpecification {
    @Test
    public void shouldCreateSuccessfulValidationStatus() {
        // GIVEN

        final ValidationStatus r = ValidationStatus.success();

        // WHEN/THEN
        assertTrue(r.isSuccess());
        assertNull(r.validationFailureMessage());
    }

    @Test
    public void shouldCreateFailedValidationStatus() {
        // GIVEN

        final String failReason = Any.string();
        final ValidationStatus r = ValidationStatus.failure(failReason);

        // WHEN/THEN
        assertFalse(r.isSuccess());
        assertEquals(r.validationFailureMessage(), failReason);
    }
}

