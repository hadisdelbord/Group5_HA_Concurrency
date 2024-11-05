package capstoneProject;

public class EnergySource {
	
	
	private String name;
	private int power;	// KW/h
	
	public EnergySource(String name, int power) {
		this.name = name;
		this.power = power;
	}

	@Override
	public String toString() {
		return "EnergySource [name=" + name + ", power=" + power + "]";
	}

	public String getName() {
		return name;
	}

	public int getPower() {
		return power;
	}

}
