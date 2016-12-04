
public class Neuron {
	private double input[];
	private double sum;
	
	public double output(double[] input) { //just the other two functions called one after the other
		this.input = input;
		return applyActivationFunction(getWeightedSum(input));
	}
	
	public double getWeightedSum(double[] weightedInputs) {
		double sum = 0;
		for (int i = 0; i < input.length; i++) {
			sum += weightedInputs[i]; //adds all inputs
		}
		this.sum = sum;
		return sum;
	} 
	
	public double getSum() {
		return sum;
	}
	public double applyActivationFunction(double weightedSum) {
		// applies sigmoid/logistic activation function
		
		 /*  1
		 * ______
		 *    -x
		 * 1+e
		 */
		return 1 / (1 + Math.pow(2.7182818284590452353602874713527, -weightedSum)); 
	}
	
}
