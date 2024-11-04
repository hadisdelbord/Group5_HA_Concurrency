package capstoneProject;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import capstoneProject.exceptions.*;

public class LogServiceResourceManagementTest {

    private static final String LOG_DIRECTORY = "testLogs";
    private LogService logService;

    @Before
    public void setUp() throws LogFileCreationException {
        // Initialize LogService with a test log directory
        logService = new LogService(LOG_DIRECTORY);
        try {
			Files.createDirectories(Paths.get(LOG_DIRECTORY));
		} catch (IOException e) {
			e.printStackTrace();
		}  // Create the test directory
    }

    @After
    public void tearDown() throws IOException {
        // Cleanup: Delete the test directory after each test
        Files.walk(Paths.get(LOG_DIRECTORY))
                .map(Path::toFile)
                .forEach(file -> file.delete());
    }

    @Test
    public void testCreateLogFile() throws LogFileCreationException, IOException {
        // Arrange
        LogMetadata metadata = new LogMetadata("Station1", "Equipment1", "Solar");
        String logContent = "Test log content";

        // Act
        logService.createLogFile(metadata, logContent);

        // Assert: Check if file is created and content is written
        Path logFilePath = Paths.get(LOG_DIRECTORY, metadata.getLogFileName());
        assertTrue("Log file should exist", Files.exists(logFilePath));
        List<String> lines = Files.readAllLines(logFilePath);
        assertTrue("Log file should contain the log content", lines.contains(logContent));
        assertTrue("Log file should contain metadata", lines.contains("Charging Station: Station1"));
    }

    @Test
    public void testDeleteLogFile() throws IOException, LogFileDoesNotExistException {
        // Arrange
        Path logFilePath = Paths.get(LOG_DIRECTORY, "testLogFile.log");
        Files.createFile(logFilePath);

        // Act
        logService.deleteLogFile("testLogFile");

        // Assert: Ensure the file is deleted
        assertFalse("Log file should be deleted", Files.exists(logFilePath));
    }

    @Test
    public void testMoveLogFile() throws LogFileCreationException, LogFileDoesNotExistException, IOException {
        // Arrange
        String targetDirectory = "archive";
        Path logFilePath = Paths.get(LOG_DIRECTORY, "testLogFile.log");
        Files.createFile(logFilePath);

        // Act
        logService.moveLogFile("testLogFile", targetDirectory);

        // Assert: Ensure the file is moved to the target directory
        Path newFilePath = Paths.get(LOG_DIRECTORY, targetDirectory, "testLogFile.log");
        assertTrue("Log file should be moved to target directory", Files.exists(newFilePath));
        assertFalse("Original log file should be deleted", Files.exists(logFilePath));
    }

    @Test
    public void testArchiveLogFile() throws LogFileCreationException, LogFileArchiveException, IOException {
        // Arrange
        Path logFilePath = Paths.get(LOG_DIRECTORY, "testLogFile.log");
        Files.createFile(logFilePath);

        // Act
        logService.archiveLogFile("testLogFile");

        // Assert: Ensure ZIP archive is created
        Path zipFilePath = Paths.get(LOG_DIRECTORY, "archive", "testLogFile.zip");
        assertTrue("ZIP archive should be created", Files.exists(zipFilePath));
    }

    @Test
    public void testGetLogFilesByEquipment() throws IOException, LogFileDoesNotExistException, LogFileReadException, LogNotFoundException {
        // Arrange
        Path logFilePath = Paths.get(LOG_DIRECTORY, "Equipment1_2023-10-03.log");
        Files.createFile(logFilePath);

        // Act
        List<String> logs = logService.getLogFilesByEquipment("Equipment1");

        // Assert: Ensure the log file is returned
        assertEquals("Should return one log file", 1, logs.size());
        assertTrue("Returned log file should contain the equipment name", logs.get(0).contains("Equipment1"));
    }

    @Test
    public void testGetLogFilesByDate() throws IOException, LogFileDoesNotExistException, LogFileReadException, LogNotFoundException {
        // Arrange
        Path logFilePath = Paths.get(LOG_DIRECTORY, "Station1_2023-10-03.log");
        Files.createFile(logFilePath);

        // Act
        List<String> logs = logService.getLogFilesByDate("2023-10-03");

        // Assert: Ensure the log file is returned
        assertEquals("Should return one log file", 1, logs.size());
        assertTrue("Returned log file should contain the date", logs.get(0).contains("2023-10-03"));
    }
}

