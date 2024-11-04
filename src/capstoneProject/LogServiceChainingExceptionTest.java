package capstoneProject;

import static org.junit.Assert.*;


import java.io.File;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import capstoneProject.exceptions.LogFileCreationException;
import capstoneProject.exceptions.LogFileDoesNotExistException;
import capstoneProject.exceptions.LogFileReadException;
import capstoneProject.exceptions.LogNotFoundException;

public class LogServiceChainingExceptionTest {

    private LogService logService;
    private final String logDirectory = "testLogs";

    @Before
    public void setUp() throws LogFileCreationException {
        logService = new LogService(logDirectory);
    }

    @After
    public void tearDown() {
        // Clean up test directory after each test
        try {
            Files.walk(Paths.get(logDirectory))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateLogFile() throws LogFileCreationException {
        LogMetadata metadata = new LogMetadata("log1.log", "Station1", "Equipment1");
        String content = "This is a test log entry.";

        logService.createLogFile(metadata, content);

        File logFile = new File(logDirectory + "/log1.log");
        assertTrue("Log file should be created.", logFile.exists());
    }

    @Test
    public void testCreateMultipleLogFiles() throws LogFileCreationException {
        // Create multiple log files
        for (int i = 1; i <= 5; i++) {
            LogMetadata metadata = new LogMetadata("log" + i + ".log", "Station" + i, "Equipment" + i);
            logService.createLogFile(metadata, "Content for log " + i);
        }

        // Verify all log files are created
        for (int i = 1; i <= 5; i++) {
            File logFile = new File(logDirectory + "/log" + i + ".log");
            assertTrue("Log file log" + i + ".log should be created.", logFile.exists());
        }
    }

    @Test
    public void testDeleteLogFile() throws LogFileCreationException, LogFileDoesNotExistException {
        logService.createLogFile(new LogMetadata("logToDelete.log", "Station1", "Equipment1"), "Content");

        logService.deleteLogFile("logToDelete");

        File logFile = new File(logDirectory + "/logToDelete.log");
        assertFalse("Log file should be deleted.", logFile.exists());
    }

    @Test(expected = LogFileDoesNotExistException.class)
    public void testDeleteLogFileNotFound() throws LogFileDoesNotExistException {
        logService.deleteLogFile("nonexistentLog");
    }

    @Test
    public void testMoveLogFile() throws LogFileCreationException, LogFileDoesNotExistException {
        logService.createLogFile(new LogMetadata("logToMove.log", "Station1", "Equipment1"), "Content");
        logService.moveLogFile("logToMove", "movedLogs");

        File movedFile = new File(logDirectory + "/movedLogs/logToMove.log");
        assertTrue("Log file should be moved.", movedFile.exists());
    }

    @Test
    public void testGetLogFilesByEquipment() throws LogFileCreationException, LogFileReadException, LogNotFoundException, LogFileDoesNotExistException {
        logService.createLogFile(new LogMetadata("logByEquipment.log", "Station1", "Equipment1"), "Equipment log content.");

        List<String> logs = logService.getLogFilesByEquipment("Equipment1");
        assertFalse("Should retrieve logs for Equipment1.", logs.isEmpty());
        assertTrue("Log content should be present.", logs.contains("Equipment log content."));
    }

    @Test(expected = LogNotFoundException.class)
    public void testGetLogFilesByEquipmentNotFound() throws LogFileReadException, LogNotFoundException, LogFileDoesNotExistException {
        logService.getLogFilesByEquipment("NonExistentEquipment");
    }

    @Test
    public void testGetLogFilesByDate() throws LogFileCreationException, LogFileReadException, LogNotFoundException, LogFileDoesNotExistException {
        logService.createLogFile(new LogMetadata("logByDate.log", "Station1", "Equipment1"), "Date log content.");

        List<String> logs = logService.getLogFilesByDate("2024-10-19");
        assertFalse("Should retrieve logs for the specified date.", logs.isEmpty());
        assertTrue("Log content should be present.", logs.contains("Date log content."));
    }

    @Test(expected = LogNotFoundException.class)
    public void testGetLogFilesByDateNotFound() throws LogFileReadException, LogNotFoundException, LogFileDoesNotExistException {
        logService.getLogFilesByDate("2024-10-20");
    }
}
