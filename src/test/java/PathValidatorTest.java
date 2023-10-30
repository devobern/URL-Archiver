import ch.bfh.exceptions.PathValidationException;
import ch.bfh.helper.I18n;
import ch.bfh.helper.PathValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link PathValidator} class.
 * This class ensures that the path validation logic behaves as expected.
 */
public class PathValidatorTest {

    /**
     * Sets up the test environment before each test case.
     * This method resets the ResourceBundle to ensure a fresh instance for each test case.
     */
    @BeforeEach
    void setup() {
        // Reset the ResourceBundle to null before each test to ensure
        // fresh loading of the bundle for each test case.
        I18n.getResourceBundle(Locale.ENGLISH);
    }

    /**
     * Tests the path validation logic for an empty input path.
     * The method should throw a {@link PathValidationException}.
     */
    @Test
    public void testValidate_EmptyInputPath() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate(""));
    }

    /**
     * Tests the path validation logic for a null input path.
     * The method should throw a {@link PathValidationException}.
     */
    @Test
    public void testValidate_NullInputPath() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate(null));
    }

    /**
     * Tests the path validation logic for an invalid path syntax.
     * The method should throw a {@link PathValidationException}.
     */
    @Test
    public void testValidate_InvalidPathSyntax() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate("::invalid::path"));
    }

    /**
     * Tests the path validation logic for a path that does not exist.
     * The method should throw a {@link PathValidationException}.
     */
    @Test
    public void testValidate_PathDoesNotExist() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate("/path/does/not/exist"));
    }

    /**
     * Tests the path validation logic for a path that exists but is not readable.
     * The method should throw a {@link PathValidationException}.
     */
    @Test
    public void testValidate_PathIsNotReadable() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate("test_files/notreadable.txt"));
    }
}

