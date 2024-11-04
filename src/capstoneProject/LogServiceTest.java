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

public class LogServiceTest {
    private static final String LOG_DIRECTORY = "temp-logs";
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
    public void tearDown() {
        // Cleanup: Delete the test directory after each test
      try {
            Files.walk(Paths.get(LOG_DIRECTORY))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  @Test
  public void testCreateLogFile()
      throws LogFileCreationException, LogFileReadException, LogFileDoesNotExistException, LogNotFoundException {

    final String EQUIPMENT_NAME = "random-eName";
    final LogMetadata logMetadata = new LogMetadata("eachDay", EQUIPMENT_NAME, "eSource");

    this.logService.createLogFile(logMetadata, "random-content-here");
    List<String> equipments = this.logService.getLogFilesByEquipment(EQUIPMENT_NAME);

    assertTrue(equipments.contains(logMetadata.getLogFileName()));
  }

  @Test
  public void testCreateLogFileException() {
    LogMetadata logMetadata = new LogMetadata("eachDay", "/../../../../../../../../../eName", "eSource");
    try {
      this.logService.createLogFile(logMetadata, "random-content-here");
    } catch (LogFileCreationException e) {
      assertEquals(e.getClass(), LogFileCreationException.class);
     } catch (Exception e) {
      assertEquals(e.getClass(), LogFileCreationException.class);
    }
  }

  @Test
  public void testCreateArchiveLogFileException() {
    final String LOG_FILE_ARCHIVE_NAME = "/../../../../../../../../random-archive-file-name";
    try {
      this.logService.archiveLogFile(LOG_FILE_ARCHIVE_NAME);
    } catch (LogFileCreationException e) {
      assertEquals(e.getClass(), LogFileCreationException.class);
    } catch (Exception e) {
      assertEquals(e.getClass(), LogFileCreationException.class);
    }
  }

  @Test
  public void testDeleteLogException() {

    final String NOT_EXIST_LOG_FILE_NAME = "random-log-file-name";

    try {
      this.logService.deleteLogFile(NOT_EXIST_LOG_FILE_NAME);
    } catch (LogFileDoesNotExistException e) {
      assertEquals(e.getClass(), LogFileDoesNotExistException.class);
    } catch (Exception e) {
      assertEquals(e.getClass(), LogFileDoesNotExistException.class);
    }
  }

  @Test
  public void testListEmptyLogEquipmentException() {

    final String NO_EXIST_LOG_FILE_NAME = "random-log-file-name";

    try {
      this.logService.getLogFilesByEquipment(NO_EXIST_LOG_FILE_NAME);
    } catch (LogNotFoundException e) {
      assertEquals(e.getClass(), LogNotFoundException.class);
    } catch (Exception e) {
      assertEquals(e.getClass(), LogNotFoundException.class);
    }
  }

}