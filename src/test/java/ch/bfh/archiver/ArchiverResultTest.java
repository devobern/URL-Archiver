package ch.bfh.archiver;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

class ArchiverResultTest {

    @Test
    void givenArchivedUrlsAndUnavailableArchivers_whenGettersCalled_thenCorrectDataReturned() {
        List<String> archivedUrls = List.of("http://archived.example.com/wayback");
        List<String> unavailableArchivers = List.of("ArchiveToday");

        ArchiverResult result = new ArchiverResult(archivedUrls, unavailableArchivers);

        assertEquals(archivedUrls, result.archivedUrls());
        assertEquals(unavailableArchivers, result.unavailableArchivers());
    }
}
