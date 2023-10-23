import ch.bfh.exceptions.PathValidationException;
import ch.bfh.validator.PathValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathValidatorTest {

    @Test
    public void testValidate_EmptyInputPath() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate(""));
    }

    @Test
    public void testValidate_NullInputPath() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate(null));
    }

    @Test
    public void testValidate_InvalidPathSyntax() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate("::invalid::path"));
    }

    @Test
    public void testValidate_PathDoesNotExist() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate("/path/does/not/exist"));
    }

    @Test
    public void testValidate_PathIsNotReadable() {
        assertThrows(PathValidationException.class, () -> PathValidator.validate("test_files/notreadable.txt"));
    }
}

