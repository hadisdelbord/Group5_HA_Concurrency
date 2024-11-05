package capstoneProject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class Battery {
	private String name;
	private int capacity; // KW
	private int currentCharge; // KW
	private EnergySource energySource;

	public Battery(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
		this.currentCharge = 0;
	}

	public EnergySource getEnergySource() {
		return energySource;
	}

	public void setEnergySource(EnergySource energySource) {
		this.energySource = energySource;
	}

	@Override
	public String toString() {
		return "Battery [name=" + name + ", capacity=" + capacity + ", currentCharge=" + currentCharge + "]";
	}

	public int getCapacity() {
		return capacity;
	}

	public String getName() {
		return name;
	}

	public synchronized void charge() {
		if (this.energySource == null) {
			System.err.println("There is no energy source!");
			return;
		}
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(() -> {
			while (true) {
				// TODO: Because of divide it is better to use double instead of integer
				int amount = this.energySource.getPower() / 3600;
				if (amount <= 0) {
					System.err.println("Amount charge is invalid!");
					return;
				}
				if (currentCharge + amount <= capacity) {
					currentCharge += amount;
					System.out.println(Thread.currentThread().getName() + " charged battery by " + amount
							+ ", current charge: " + currentCharge);
				} else {
					currentCharge = capacity;
					System.out.println("Battery full! Current charge: " + currentCharge);
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}

		});
		executor.shutdown();
	}

	public synchronized void useEnergy(int amount) {
		if (amount <= 0) {
			System.err.println("Amount charge is invalid!");
			return;
		}
		if (currentCharge >= amount) {
			currentCharge -= amount;
			System.out.println(
					Thread.currentThread().getName() + " used " + amount + ", remaining charge: " + currentCharge);
		} else {
			currentCharge = 0;
			System.out.println("Overload prevented! Insufficient charge.");
		}
	}

	public int getCurrentCharge() {
		return currentCharge;
	}
}
