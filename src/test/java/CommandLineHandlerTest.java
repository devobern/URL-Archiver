import ch.bfh.handler.CommandLineHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/*
This test is commented more in detail, to better understand how to use mockito
todo: Remove details comments
 */

@RunWith(MockitoJUnitRunner.class)
// This annotation tells JUnit to use Mockito's test runner, which automatically sets up mocks before tests.
public class CommandLineHandlerTest {

    @Mock
    // This annotation indicates that 'fileInputHandler' should be treated as a mock object.
    private FileInputHandler fileInputHandler;

    private CommandLineHandler commandLineHandler;  // The class we're testing.
    private AutoCloseable closeable;
    // This 'AutoCloseable' instance is created when you open the mocks and must be closed in the @After method.

    @Before
    // This method runs before each test method. Used for setup.
    public void setUp() {
        // Initializes the mock objects and returns an AutoCloseable reference that should be closed after the tests run.
        closeable = MockitoAnnotations.openMocks(this);
        // Initializes the 'CommandLineHandler' object we're testing, providing the mocked 'FileInputHandler' as a dependency.
        commandLineHandler = new CommandLineHandler(fileInputHandler);
    }

    @After
    // This method runs after each test method. Used for teardown or cleanup.
    public void tearDown() throws Exception {
        // Closes resources associated with the mock setup. Prevents potential memory leaks.
        closeable.close();
    }

    @Test
    // Indicates that this method is a test method.
    public void testHandleArgs_withArgs() {
        // Setup
        String[] args = {"testPath"};

        // Execute
        // Calls the method being tested.
        commandLineHandler.handleArgs(args);

        // Verify
        // Verifies that 'processCommandLineInput' was called exactly once with the argument 'testPath'.
        verify(fileInputHandler, times(1)).processCommandLineInput(args[0]);
        // Verifies that 'promptUserForInput' was never called.
        verify(fileInputHandler, never()).promptUserForInput();
    }

    @Test
    // Another test method.
    public void testHandleArgs_noArgs() {
        // Setup
        String[] args = {};

        // Execute
        commandLineHandler.handleArgs(args);

        // Verify
        // Verifies that 'processCommandLineInput' was never called with any string as argument.
        verify(fileInputHandler, never()).processCommandLineInput(anyString());
        // Verifies that 'promptUserForInput' was called exactly once.
        verify(fileInputHandler, times(1)).promptUserForInput();
    }
}