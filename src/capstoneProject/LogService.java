package capstoneProject;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import capstoneProject.exceptions.LogFileArchiveException;
import capstoneProject.exceptions.LogFileCreationException;
import capstoneProject.exceptions.LogFileDoesNotExistException;
import capstoneProject.exceptions.LogFileReadException;
import capstoneProject.exceptions.LogNotFoundException;

public class LogService {
 private final String logDirectory;

 public LogService(String logDirectory) throws LogFileCreationException {
  this.logDirectory = logDirectory;
  createLogDirectory();
 }

 private void createLogDirectory() throws LogFileCreationException {
  try {
   Files.createDirectories(Paths.get(logDirectory));
  } catch (IOException e) {
   // Chaining the exception while re-throwing
   throw new LogFileCreationException(e.getMessage(), e);
  }
 }

 private void createLogDirectory(String directory) throws LogFileCreationException {
  try {
   Files.createDirectories(Paths.get(directory));
  } catch (IOException e) {
   // Chaining the exception while re-throwing
   throw new LogFileCreationException(e.getMessage(), e);
  }
 }

 public void createLogFile(LogMetadata metadata, String content) throws LogFileCreationException {
  try (BufferedWriter writer = new BufferedWriter(
    new FileWriter(logDirectory + "/" + metadata.getLogFileName(), true))) {
   writer.write(content);
   writer.newLine();
   writer.write("Charging Station: " + metadata.getChargingStation());
   writer.newLine();
   writer.write("Equipment Name: " + metadata.getEquipmentName());
   writer.newLine();
   writer.write("Energy Source: " + metadata.getEnergySource());
   writer.newLine();
   writer.write("Date: " + metadata.getDate());
   writer.newLine();
   writer.write("=======================================");
   writer.newLine();
  } catch (IOException e) {
   // Chaining the exception while re-throwing
   throw new LogFileCreationException(e.getMessage(), e);
  }
 }

 public void deleteLogFile(String input) throws LogFileDoesNotExistException {
  Pattern pattern = Pattern.compile(input);
  File dir = new File(logDirectory);

  if (dir.exists() && dir.isDirectory()) {
   File[] logFiles = dir.listFiles();

   if (logFiles != null && logFiles.length > 0) {
    for (File logFile : logFiles) {
     if (pattern.matcher(logFile.getName()).find()) {
      try {
       Files.delete(Paths.get(logFile.getPath()));
       System.out.println("Deleted log file: " + logFile.getName());
      } catch (IOException e) {
       // Chaining the exception while re-throwing
       throw new LogFileDoesNotExistException("Failed to delete log file: " + logFile.getName(), e);
      }
     }
    }
   } else {
    throw new LogFileDoesNotExistException("Log directory does not exist.");
   }
  } else {
   throw new LogFileDoesNotExistException("Log directory does not exist.");
  }
 }

 public void moveLogFile(String input, String targetDirectory)
   throws LogFileDoesNotExistException, LogFileCreationException {
  String newDirectory = logDirectory + "/" + targetDirectory;
  createLogDirectory(newDirectory);

  Pattern pattern = Pattern.compile(input);
  File dir = new File(logDirectory);

  if (dir.exists() && dir.isDirectory()) {
   File[] logFiles = dir.listFiles();

   if (logFiles != null) {
    for (File logFile : logFiles) {
     if (pattern.matcher(logFile.getName()).find()) {
      try {
       Files.move(Paths.get(logDirectory + "/" + logFile.getName()),
         Paths.get(newDirectory + "/" + logFile.getName()),
         StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
       // Chaining the exception while re-throwing
       throw new LogFileCreationException("Failed to move log file: " + logFile.getName(), e);
      }
     }
    }
   }
  } else {
   throw new LogFileDoesNotExistException("Log directory does not exist.");
  }
 }
 public void archiveLogFile(String logFileName) throws LogFileArchiveException, LogFileCreationException {
	  String archiveDirectory = logDirectory + "/archive";
	  createLogDirectory(archiveDirectory);
	  
	  String zipFileName = archiveDirectory + File.separator + logFileName + ".zip";

	  try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {
	   // Get all log files in the directory
	   try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logDirectory), "*.log")) {

	    for (Path entry : stream) {
	     // Add each log file to the ZIP file
	     try (FileInputStream fileInputStream = new FileInputStream(entry.toFile())) {
	      ZipEntry zipEntry = new ZipEntry(entry.getFileName().toString());
	      zipOutputStream.putNextEntry(zipEntry);

	      byte[] buffer = new byte[1024];
	      int length;
	      while ((length = fileInputStream.read(buffer)) >= 0) {
	       zipOutputStream.write(buffer, 0, length);
	      }
	      zipOutputStream.closeEntry();
	     } catch (IOException e) {
	      // Chaining the exception while re-throwing
	      throw new LogFileArchiveException("Failed to add file to ZIP: " + entry.getFileName(), e);
	     }
	    }
	   }
	   System.out.println("Log files archived successfully to: " + zipFileName);
	  } catch (IOException e) {
	   // Chaining the exception while re-throwing
	   throw new LogFileCreationException("Failed to create ZIP file: " + e.getMessage(), e);
	  }
	 }

	 public List<String> getLogFilesByEquipment(String equipmentName)
	   throws LogFileReadException, LogFileDoesNotExistException, LogNotFoundException {
	  List<String> matchingLogs = new ArrayList<>();
	  try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logDirectory),
	    "*" + equipmentName + "*.log")) {
	   for (Path entry : stream) {

	    matchingLogs.add(entry.getFileName().toString());

	    try (BufferedReader reader = new BufferedReader(
	      new FileReader(logDirectory + "/" + entry.getFileName().toString()))) {
	     String line;
	     while ((line = reader.readLine()) != null) {
	      matchingLogs.add(line);
	     }
	    } catch (IOException e) {
	     // Chaining the exception while re-throwing
	     throw new LogFileReadException(e.getMessage(), e);
	    }
	   }
	  } catch (IOException e) {
	   // Chaining the exception while re-throwing
	   throw new LogFileDoesNotExistException(e.getMessage(), e);
	  }
	  if (matchingLogs.isEmpty()) {
	   throw new LogNotFoundException("No logs found for equipment: " + equipmentName);
	  }
	  return matchingLogs;
	 }

	 public List<String> getLogFilesByDate(String date)
	   throws LogNotFoundException, LogFileReadException, LogFileDoesNotExistException {
	  List<String> matchingLogs = new ArrayList<>();
	  try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(logDirectory), "*_" + date + "*.log")) {
	   for (Path entry : stream) {
	    matchingLogs.add(entry.getFileName().toString());

	    try (BufferedReader reader = new BufferedReader(
	      new FileReader(logDirectory + "/" + entry.getFileName().toString()))) {
	     String line;
	     while ((line = reader.readLine()) != null) {
	      matchingLogs.add(line);
	     }
	    } catch (IOException e) {
	     // Chaining the exception while re-throwing
	     throw new LogFileReadException(e.getMessage(), e);
	    }
	   }
	  } catch (IOException e) {
	   // Chaining the exception while re-throwing
	   throw new LogFileDoesNotExistException(e.getMessage(), e);
	  }
	  if (matchingLogs.isEmpty()) {
	   throw new LogNotFoundException("No logs found for date: " + date);
	  }
	  return matchingLogs;
	 }
	}