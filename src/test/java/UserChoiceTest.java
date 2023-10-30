import ch.bfh.model.UserChoice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link UserChoice} enumeration.
 */
public class UserChoiceTest {

    /**
     * Test method for {@link UserChoice#getCommand()}.
     * Validates the correct command associated with each enumeration value.
     */
    @Test
    public void testGetCommand() {
        assertEquals("o", UserChoice.OPEN.getCommand());
        assertEquals("a", UserChoice.ARCHIVE.getCommand());
        assertEquals("n", UserChoice.NEXT.getCommand());
        assertEquals("h", UserChoice.HELP.getCommand());
        assertEquals("q", UserChoice.QUIT.getCommand());
    }

    /**
     * Test method for {@link UserChoice#fromCommand(String)} with valid commands.
     * Validates that the correct enumeration value is returned for each valid command.
     */
    @Test
    public void testFromCommand_ValidCommands() {
        assertEquals(UserChoice.OPEN, UserChoice.fromCommand("o"));
        assertEquals(UserChoice.ARCHIVE, UserChoice.fromCommand("a"));
        assertEquals(UserChoice.NEXT, UserChoice.fromCommand("n"));
        assertEquals(UserChoice.HELP, UserChoice.fromCommand("h"));
        assertEquals(UserChoice.QUIT, UserChoice.fromCommand("q"));
    }

    /**
     * Test method for {@link UserChoice#fromCommand(String)} with an invalid command.
     * Validates that a null value is returned for an unrecognized command.
     */
    @Test
    public void testFromCommand_InvalidCommand() {
        assertNull(UserChoice.fromCommand("z"));
    }

    /**
     * Test method for {@link UserChoice#fromCommand(String)} with case-insensitive commands.
     * Validates that the method is case-insensitive and returns the correct enumeration value.
     */
    @Test
    public void testFromCommand_CaseInsensitive() {
        assertEquals(UserChoice.OPEN, UserChoice.fromCommand("O"));
        assertEquals(UserChoice.ARCHIVE, UserChoice.fromCommand("A"));
    }
}
