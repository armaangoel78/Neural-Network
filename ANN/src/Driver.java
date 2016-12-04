import java.util.Random;
import java.util.Scanner;

public class Driver {
	private static Random r = new Random();
	private static double learningRate = .8;
	private static double marginOfError = .01;
	private static boolean promptMode = false;
	private static Scanner s = new Scanner(System.in);


	
	public static double data[][][] = {
			{ {0, 0}, {0} },
			{ {1, 1}, {0} },
			{ {1, 0}, {1} }, 
			{ {0, 1}, {1} }
			};
	

	private static Neuron neurons[][] = {
			{new Neuron(), new Neuron(), new Neuron()}, 
			{new Neuron()}
			};
	
	private static Synapse synapses[][][] = {
		{{new Synapse(), new Synapse()}, {new Synapse(), new Synapse()}, {new Synapse(), new Synapse()}},
		{{new Synapse(), new Synapse(), new Synapse()}}
	};
	
	private static Toolkit toolkit = new Toolkit(data, neurons, synapses, learningRate);

	private static double[] errors = new double[data.length];
	
	public static void main(String args[]) {
		
		if (promptMode == true) {
			System.out.println("Enter learning rate: ");
			learningRate = s.nextDouble();
			System.out.println("Enter margin of error: ");
			marginOfError = s.nextDouble();	
		}
		
		long startTime = System.currentTimeMillis();
		boolean exit = false;
		int epoch = 0;
		while (!exit) {
			for (int dataCase = 0; dataCase < data.length; dataCase++){
				double[][] output = toolkit.forwardProp(data[dataCase][0]);
				double outputOfNet = output[output.length-1][0];
				toolkit.backwardProp(dataCase);
				errors[dataCase] = toolkit.getError(dataCase);
				if (epoch % 1 == 0) toolkit.display(epoch, dataCase, errors[dataCase], outputOfNet, data[dataCase][1][0]);
			}
			exit = toolkit.checkError(errors, marginOfError);
			epoch++;
		}
		System.out.println("");
		for (int i = 0; i < synapses.length; i++) {
			for (int x = 0; x < synapses[i].length; x++) {
				for (int y = 0; y < synapses[i][x].length; y++) {
					System.out.println("Synapse: " + i + "|" + x + "|" + y +  " Final weight: " + synapses[i][x][y].getWeight());
				}
			}
		}
		System.out.println("");
		System.out.println("Training Time: " + (System.currentTimeMillis() - startTime) + " Milliseconds");
		while (true) {
			System.out.println("");
			System.out.println("Enter inputs: ");
			double inputs[] = {s.nextDouble(), s.nextDouble()};
			System.out.println(toolkit.forwardProp(inputs)[1][0] > .5 ? 1 : 0);
		}
	}	
}
