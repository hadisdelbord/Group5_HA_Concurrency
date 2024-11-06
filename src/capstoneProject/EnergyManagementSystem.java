package capstoneProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import capstoneProject.exceptions.LogFileArchiveException;
import capstoneProject.exceptions.LogFileCreationException;
import capstoneProject.exceptions.LogFileDoesNotExistException;
import capstoneProject.exceptions.LogFileReadException;
import capstoneProject.exceptions.LogNotFoundException;

public class EnergyManagementSystem {

	public static void showMenue() {
		System.out.println("Actions:");
		System.out.println("1. Create Log");
		System.out.println("2. Move Log");
		System.out.println("3. Delete Log");
		System.out.println("4. Archive Log");
		System.out.println("5. List Logs by Equipment");
		System.out.println("6. List Logs by Date");
		System.out.println("7. Create Energy Source");
		System.out.println("8. List of Energy Sources");
		System.out.println("9. Create Battery");
		System.out.println("10. List of Batteries");
		System.out.println("11. Connect Energy Source to Battery");
		System.out.println("0. Exit");
		System.out.print("? ");
	}

	public static void main(String[] args) throws LogFileArchiveException, LogFileDoesNotExistException,
			LogFileCreationException, LogFileReadException, LogNotFoundException {
		Logger.getLogger("java.util.concurrent").setLevel(Level.OFF);

		LogService logService = new LogService("logs");
		ArrayList<Battery> batteries = new ArrayList<Battery>();
		ArrayList<EnergySource> energySources = new ArrayList<EnergySource>();

		System.out.println("[Energy Management System]");

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				showMenue();

				int choice;
				try {
					choice = scanner.nextInt();
				} catch (Exception e) {
					System.err.println("[Error] Invalid input!");
					scanner.next(); // Consume invalid input
					continue;
				}

				scanner.nextLine(); // Consume newline

				switch (choice) {
				case 1:
					System.out.print("Enter charging station: ");
					String chargingStation = scanner.nextLine();
					System.out.print("Enter equipment name: ");
					String equipmentName = scanner.nextLine();
					System.out.print("Enter energy source: ");
					String energySource = scanner.nextLine();
					LogMetadata metadata = new LogMetadata(chargingStation, equipmentName, energySource);
					try {
						logService.createLogFile(metadata, "Log created at: " + metadata.getDate());
						System.out.println("Log created: " + metadata.getLogFileName());
					} catch (LogFileCreationException e) {
						System.err.println(e.getMessage());
					}
					break;

				case 2:
					System.out.print("Enter equipment name or date or log file name to move: ");
					String fileToMove = scanner.nextLine();
					System.out.print("Enter target directory name to move: ");
					String targetDirectory = scanner.nextLine();
					try {
						logService.moveLogFile(fileToMove, targetDirectory);
						System.out.println("Log file moved.");
					} catch (LogFileDoesNotExistException | LogFileCreationException e) {
						System.err.println(e.getMessage());
					}
					break;

				case 3:
					System.out.print("Enter equipment name or date or log file name to delete: ");
					String fileToDelete = scanner.nextLine();
					try {
						logService.deleteLogFile(fileToDelete);
						System.out.println("Log file deleted.");
					} catch (LogFileDoesNotExistException e) {
						System.err.println(e.getMessage());
					}
					break;

				case 4:
					System.out.print("Enter log file name to archive: ");
					String fileToArchive = scanner.nextLine();
					try {
						logService.archiveLogFile(fileToArchive);
						System.out.println("Log file archived.");
					} catch (LogFileArchiveException | LogFileCreationException e) {
						System.err.println(e.getMessage());
					}
					break;

				case 5:
					System.out.print("Enter equipment name to list logs: ");
					String equipNameToSearch = scanner.nextLine();
					try {
						List<String> logsByEquipment = logService.getLogFilesByEquipment(equipNameToSearch);
						logsByEquipment.forEach(System.out::println);
					} catch (LogFileReadException | LogFileDoesNotExistException | LogNotFoundException e) {
						System.err.println(e.getMessage());
					}
					break;

				case 6:
					System.out.print("Enter date (YYYY-MM-DD) to list logs: ");
					String dateToSearch = scanner.nextLine();
					try {
						List<String> logsByDate = logService.getLogFilesByDate(dateToSearch);
						logsByDate.forEach(System.out::println);
					} catch (LogNotFoundException | LogFileReadException | LogFileDoesNotExistException e) {
						System.err.println(e.getMessage());
					}
					break;

				case 7:
					System.out.print("Enter name of Energy Source:");
					String name = scanner.nextLine();
					System.out.print("Enter power of Energy Source(KW/h):");
					int power = scanner.nextInt();

					EnergySource es = new EnergySource(name, power);
					for (EnergySource e : energySources) {
						if (e.getName().equals(name)) {
							System.err.println("The energy source already exists");
							break;
						}
					}

					energySources.add(es);
					System.out.println("EnergySource '" + es.getName() + "' with power " + es.getPower() + " added");
					break;

				case 8:
					for (EnergySource e : energySources) {
						System.out.println(e);
					}
					break;

				case 9:
					System.out.print("Enter name of Battery:");
					name = scanner.nextLine();
					System.out.print("Enter Capacity of Battery(KW):");
					int capacity = scanner.nextInt();

					Battery battery = new Battery(name, capacity);
					for (Battery b : batteries) {
						if (b.getName().equals(name)) {
							System.err.println("The battery already exists");
							break;
						}

					}
					batteries.add(battery);
					System.out.println(battery + " added.");
					break;

				case 10:
					for (Battery b : batteries) {
						System.out.println(b);
					}
					break;

				case 11:
					System.out.print("Enter name of Battery:");
					String bName = scanner.nextLine();
					Battery b = null;
					for (Battery b_ : batteries) {
						if (b_.getName().equals(bName)) {
							b = b_;
							break;
						}

					}
					if (b == null) {
						System.out.print("The battery not found!");
						break;

					}

					System.out.print("Enter name of Energy Source:");
					String esName = scanner.nextLine();
					EnergySource es_ = null;
					for (EnergySource e_ : energySources) {
						if (e_.getName().equals(esName)) {
							es_ = e_;
							break;
						}

					}
					if (es_ == null) {
						System.out.print("The energy source not found!");
						break;

					}
					b.setEnergySource(es_);
					b.charge();

					break;

				case 0:
					System.out.println("Have a good day :)!");
					System.exit(0);
				default:
					System.out.println("[Error] Invalid Action!");
				}
			}
		} catch (Exception e) {
			System.err.println("[Error] An unexpected error occurred: " + e.getMessage());
		}
	}
}