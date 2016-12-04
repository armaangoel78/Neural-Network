
public class Toolkit {
	private double data[][][];
	private Synapse synapses[][][];
	private Neuron neurons[][];
	private double learningRate;
	private double[] input;
	private double[][] outputs; 
	
	public Toolkit(double[][][] data, Neuron neurons[][], Synapse synapses[][][], double learningRate) {
		this.data = data;
		this.synapses = synapses;
		this.neurons = neurons;
		this.learningRate = learningRate;
	}
	
	
	private double getSigmoidDeriv(double input) { //derivative of sigmoid/logistic
		double numerator = Math.pow(2.7182818284590452353602874713527, input);
		double denomantor = Math.pow((numerator + 1), 2);
		
		return numerator/denomantor;
	}
	
	private double getOutputResultDeriv() {
		// dOutputReult/dOutputSum
		return getSigmoidDeriv(neurons[1][0].getSum());
	}
	
	private double[] getOutputSumDerivForWeights() {
		// dOutputSum/dOutputWeight
		double[] outputSumDerivs = new double[outputs[0].length];
		
		for (int i = 0; i < outputSumDerivs.length; i++) {
			outputSumDerivs[i] = outputs[0][i]; 
		}
		
		return outputSumDerivs;
	}
	
	private double[] getOutputWeightDerivs () {
		//dOutputResult/dOutputWeight : chain outputSumDeriv + outputResultDeiv		
		double[] weightDerivs = new double[synapses[1][0].length];

		for (int i = 0; i < weightDerivs.length; i++) {
			weightDerivs[i]  = getOutputSumDerivForWeights()[i] * getOutputResultDeriv();
		}
		return weightDerivs;
	}
	
	private void changeOutputWeights(int dataCase){
		double outputWeightDerivs[] = getOutputWeightDerivs();
		for (int i = 0; i < outputWeightDerivs.length; i++) {
			synapses[1][0][i].adjustWeight(outputWeightDerivs[i] * learningRate * getError(dataCase));
		}
	}
	
	/*
	  \\---------Output Weights Backprop---------//
	                                              
	  
	  //---------Hidden Weights Backprop---------\\	
	*/
	
	private double[] getOutputSumDerivsForHiddenOutput() {
		//dOutputSum/dHiddenOutput
		double[] outputSumDerivs = new double[synapses[1][0].length];
		
		for (int i = 0 ; i < outputSumDerivs.length; i++) {
			outputSumDerivs[i] = synapses[1][0][i].getWeight();
		}
		
		return outputSumDerivs;
	}
	
	private double[] getHiddenOutputDerivs() {
		//dHiddenOuput/dHiddenOutputSum
		double[] outputDerivs = new double[neurons[0].length];
		
		for (int i = 0; i < outputDerivs.length; i++) {
			outputDerivs[i] = getSigmoidDeriv(neurons[0][i].getSum());
		}
		
		return outputDerivs;
	}
	
	private double[][] getHiddenSumDerivs(){
		//dHiddenOutputSum/dHiddenWeights
		double[] hiddenOutputDerivs = getHiddenOutputDerivs();
		double[][] hiddenSumDerivs = new double [synapses[0].length][synapses[0][0].length];
		
		for (int i = 0; i < hiddenSumDerivs.length; i++) {
			for (int x = 0; x < hiddenSumDerivs[i].length; x++) {
				hiddenSumDerivs[i][x] = input[x];
			}
		}
		
		return hiddenSumDerivs;
	}
	
	private double[][] getHiddenWeightDerivs() {
		double[][] hiddenWeightDerivs = new double[synapses[0].length][synapses[0][0].length];
		
		for (int r = 0; r < hiddenWeightDerivs.length; r++) {
			for (int c = 0; c < hiddenWeightDerivs[r].length; c++) {
				hiddenWeightDerivs[r][c] = 
						  getOutputResultDeriv() 
						* getOutputSumDerivsForHiddenOutput()[r]
						* getHiddenOutputDerivs()[r] 
						* getHiddenSumDerivs()[r][c];
			}
		}
		
		return hiddenWeightDerivs;
	}
	
	public void changeHiddenWeights(int dataCase) {
		double hiddenWeightDerivs[][] = getHiddenWeightDerivs();
		for (int r = 0; r < hiddenWeightDerivs.length; r++) {
			for (int c = 0; c < hiddenWeightDerivs[r].length; c++) {
				synapses[0][r][c].adjustWeight(hiddenWeightDerivs[r][c] * learningRate * getError(dataCase));
			}
		}
	}
	
	/*
	  \\---------Hidden Weights Backprop---------//
	                                              
	  
	  //-------------Public Methods-------------\\	
	*/
	
	public double getError(int dataCase){//target - outputOfNet
		return data[dataCase][1][0] - outputs[1][0];
	}
	
	public void backwardProp(int dataCase) {
		changeOutputWeights(dataCase);
		changeHiddenWeights(dataCase);
	}
	
	public double[][] forwardProp(double[] input) {
		this.input = input;
		double outputs[][] = {{0,0,0},{0}};
		for (int i = 0; i < neurons[0].length; i++) {
			outputs[0][i] = neurons[0][i].output(getNeuronInput(0, i, input));
		}
		outputs[outputs.length-1][0] = neurons[neurons.length-1][0].output(getNeuronInput(neurons.length-1, 0, outputs[0]));
		this.outputs = outputs;
		return outputs;
	}
	
	public double[] getNeuronInput(int layer, int neuron, double inputToSynapse[]) {
		// the input to the synapse is an array of outputs from each neuron in the previous layer
		double[] inputs = new double[synapses[layer][neuron].length]; //# of inputs is the same as the coresponding number of weights for the neuron
		for (int i = 0 ; i < inputs.length; i++) {//each synapse is attached to each input  
			inputs[i] = synapses[layer][neuron][i].applyWeight(inputToSynapse[i]);
		}
		return inputs;
	}
	public boolean checkError(double[] errors, double maxAcceptableError) {		
		for (int dataCase = 0; dataCase < errors.length; dataCase++) {
			if (Math.abs(errors[dataCase]) >= maxAcceptableError) return false;
		}
		
		return true;
	}
	
	public void display(int epoch, int dataCase, double percentError, double output, double target) {
		System.out.println("Epoch: " + epoch + " DataCase: " + dataCase + " Error: " +  percentError + " Output: " + output + " Target: " + target);

	}
	
}
