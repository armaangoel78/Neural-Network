
public class Synapse {
	private double weight = Math.random();

	public double applyWeight(double input) {
		return input * weight;
	}
	
	//getter-setters for weight
	public void setWeight(double newWeight) {
		weight = newWeight;
	}
	public double getWeight() {
		return weight;
	}
	public void adjustWeight(double adjustment) {
		weight += adjustment;
	}
}
