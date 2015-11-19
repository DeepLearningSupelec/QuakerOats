import java.util.ArrayList;
import java.util.List;

public abstract class LearningAlgorithm {
	
	private NeuralNetwork neuralNetwork;
	public double learningRate;
	public int epochSize;
	public ArrayList<Double> trainError;
	public ArrayList<Double> testError;
	public ToCsv csv;
	public OutputData outputdata;
	
	public LearningAlgorithm(NeuralNetwork neuralNetwork){
		this.setNeuralNetwork(neuralNetwork);
		this.learningRate=0.01;
		this.epochSize=20;
		this.csv = new ToCsv(this);
		trainError = new ArrayList<Double>();
		testError = new ArrayList<Double>();
		outputdata = new OutputData(new ArrayList<Output>());
	}
	
	public LearningAlgorithm(NeuralNetwork neuralNetwork, double learningRate, int epochSize){
		this.setNeuralNetwork(neuralNetwork);
		this.learningRate=learningRate;
		this.epochSize=epochSize;
		this.csv = new ToCsv(this);
		trainError = new ArrayList<Double>();
		testError = new ArrayList<Double>();
		outputdata = new OutputData(new ArrayList<Output>());
	}
	
	abstract public void calculateActivations(double[] input);
	
	abstract public void calculateNeuronAndWeightDiffs(double[] ouput);
	
//	abstract public void train(double[][] inputsTraining,
//			double[][] outputsTraining);
//
//	abstract public void globaltraining(double[][] inputsTraining,
//			double[][] outputsTraining);
//
//	abstract public void train(double[][] inputsTraining,
//			double[][] outputsTraining, double[][] inputsTest,
//			double[][] outputsTest);
//
//	abstract public void globaltraining(double[][] inputsTraining,
//			double[][] outputsTraining, double[][] inputsTest,
//			double[][] outputsTest);
	
	/*splits the inputs into epochs of size epochSize (attribute)*/
	public List<double[][]> splitIntoEpochs(double[][] inputs){

		
		int numberOfInputs = inputs.length;
		/*maybe we could allocate the last inputs into the previous epochs if they only constitute a half epoch or less ? see later*/
		int sizeLastEpoch = numberOfInputs % this.epochSize;
		int numberOfEpochs = (sizeLastEpoch==0) ? numberOfInputs / this.epochSize : numberOfInputs / this.epochSize + 1;
		
		List<double[][]> epochs = new ArrayList<double[][]>();
		
		for(int i=1; i<=numberOfEpochs-1; i++){
			double[][] epoch = new double[this.epochSize][inputs[0].length];
			for(int j=0; j<=this.epochSize-1; j++){
				epoch[j] = inputs[(i-1)*this.epochSize+j];
			}
			epochs.add(epoch);
		}
		
		double[][] lastEpoch = (sizeLastEpoch==0) ? new double[this.epochSize][inputs[0].length] : new double[sizeLastEpoch][inputs[0].length];
		for(int j=0; j<=lastEpoch.length-1; j++){
			lastEpoch[j] = inputs[inputs.length-lastEpoch.length+j];
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
