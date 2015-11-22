import java.util.ArrayList;
import java.util.List;

public abstract class LearningAlgorithm {

	private NeuralNetwork neuralNetwork;
	public double learningRate;
	public int epochSize;
	public int numberOfEpochBetweenEachMeasure;
	public ToCsv csv;
	public ArrayList<Output> outputData;

	//Secondary constructors
	public LearningAlgorithm(NeuralNetwork neuralNetwork) {
		this(neuralNetwork, 0.1, 1, 1000);
	}

	//Primary constructor
	public LearningAlgorithm(NeuralNetwork neuralNetwork, double learningRate,
			int epochSize, int numberOfEpochBetweenEachMeasure) {
		this.neuralNetwork = neuralNetwork;
		//declare this algorithm as the algorithm of the network
		//in case we want to change the learning algorithm
		//this.neuralNetwork.setLearningAlgorithm(this);
		//I think it's useless
		this.learningRate = learningRate;
		this.epochSize = epochSize;
		this.numberOfEpochBetweenEachMeasure = numberOfEpochBetweenEachMeasure;
		this.csv = new ToCsv(this);
		this.outputData = new ArrayList<Output>();
	}

	abstract public void calculateActivations(double[] input);

	abstract public void calculateNeuronAndWeightDiffs(double[] ouput);

	abstract public void train(double[][] inputs, double[][] outputs);

	abstract public void globaltraining(double[][] inputsData,
			double[][] outputsData);

	abstract public void globaltraining(double[][] inputsDataTraining,
			double[][] outputsDataTraining, double[][] inputsDataTest,
			double[][] outputsDataTest);

	/* splits the inputs into epochs of size epochSize (attribute) */
	public List<double[][]> splitIntoEpochs(double[][] inputs) {

		int numberOfInputs = inputs.length;
		/*
		 * maybe we could allocate the last inputs into the previous epochs if
		 * they only constitute a half epoch or less ? see later
		 */
		int sizeLastEpoch = numberOfInputs % this.epochSize;
		int numberOfEpochs = (sizeLastEpoch == 0) ? numberOfInputs
				/ this.epochSize : numberOfInputs / this.epochSize + 1;

		List<double[][]> epochs = new ArrayList<double[][]>();

		for (int i = 1; i <= numberOfEpochs - 1; i++) {
			double[][] epoch = new double[this.epochSize][inputs[0].length];
			for (int j = 0; j <= this.epochSize - 1; j++) {
				epoch[j] = inputs[(i - 1) * this.epochSize + j];
			}
			epochs.add(epoch);
		}

		double[][] lastEpoch = (sizeLastEpoch == 0) ? new double[this.epochSize][inputs[0].length]
				: new double[sizeLastEpoch][inputs[0].length];
		for (int j = 0; j <= lastEpoch.length - 1; j++) {
			lastEpoch[j] = inputs[inputs.length - lastEpoch.length + j];
		}
		epochs.add(lastEpoch);

		return epochs;
	}

	public NeuralNetwork getNeuralNetwork() {
		return neuralNetwork;
	}

	public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	};

}
