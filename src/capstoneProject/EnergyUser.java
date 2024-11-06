public class EnergyUser implements Runnable {
    private Battery battery;
    private int usageAmount;

    public EnergyUser(Battery battery, int usageAmount) {
        this.battery = battery;
        this.usageAmount = usageAmount;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (battery) {
                while (battery.getCurrentCharge() < usageAmount) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is waiting for enough charge...");
                        battery.wait(); // Wait for enough charge to be available
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                battery.useEnergy(usageAmount); // Request energy usage
            }
            try {
                Thread.sleep(2000); // Simulate time between usage
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
