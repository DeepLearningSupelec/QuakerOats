import java.util.ArrayList;
import java.util.List;

public abstract class NeuralNetwork {

	private int[] constructorTab;
	private ActivationFunction activationFunction;
//	private List<InputNeuron> inputLayer;
	private InputLayer inputLayer;
	private List<IntermediateLayer> hiddenLayers;
	private OutputLayer outputLayer;
	private LearningAlgorithm learningAlgorithm;
	
	/*If the user doesn't give an activation function*/
	public NeuralNetwork(int[] constructorTab) throws InvalidNetworkConstruction{
		/*exception if the constructor array length is too small*/
		if(constructorTab.length<3){
			throw new InvalidNetworkConstruction();
		}
		else{
			this.constructorTab = constructorTab;
			this.learningAlgorithm = new BackProp(this);
			int length = constructorTab.length;
			/*only 1 intermediate layer for now, it's ok to give the "same" random bias, see later*/
			double randomBias = 2*(Math.random()-0.5)*2.4/constructorTab[1];
			double randomBiasOutput = 2*(Math.random()-0.5)*2.4/constructorTab[length-1];
			
			ActivationFunction activationFunction = new Sigmoid();
			
			List<InputNeuron> inputLayer = new ArrayList<InputNeuron>(constructorTab[0]);
			for(int i=0; i<=inputLayer.size()-1; i++){
				inputLayer.add(new InputNeuron(activationFunction));
			}
			
			List<OutputNeuron> outputLayer = new ArrayList<OutputNeuron>(constructorTab[length-1]);
			for(int i=0; i<=outputLayer.size()-1; i++){
				outputLayer.add(new OutputNeuron(activationFunction, randomBiasOutput));
			}
			
			List<IntermediateLayer> hiddenLayers = new ArrayList<IntermediateLayer>(length-2);
			for(int i=1; i<=length-2; i++){
				IntermediateLayer res = new IntermediateLayer();
				for(int k=1; k<=constructorTab[i]; k++){
					res.getLayer().add(new IntermediateNeuron(activationFunction, randomBias));
				}
				hiddenLayers.add(res);
			}
			
			this.inputLayer.setLayer(inputLayer);
			this.hiddenLayers = hiddenLayers;
			this.outputLayer.setLayer(outputLayer);
		}
	}
	/*If the user specifies an activation function and the learning algorithm*/
	public NeuralNetwork(int[] constructorTab, ActivationFunction activationFunction, LearningAlgorithm learningAlgorithm) throws InvalidNetworkConstruction{
		if(constructorTab.length<3){
			throw new InvalidNetworkConstruction();
		}
		else{
			this.setActivationFunction(activationFunction);
			this.setLearningAlgorithm(learningAlgorithm);
			learningAlgorithm.setNeuralNetwork(this);
			this.constructorTab = constructorTab;
			int length = constructorTab.length;
			/*only 1 intermediate layer for now, it's ok to give the "same" random bias, see later*/
			double randomBias = 2*(Math.random()-0.5)*2.4/constructorTab[1];
			double randomBiasOutput = 2*(Math.random()-0.5)*2.4/constructorTab[length-1];
			
			List<InputNeuron> inputLayer = new ArrayList<InputNeuron>(constructorTab[0]);
			for(int i=0; i<=inputLayer.size()-1; i++){
				inputLayer.add(new InputNeuron(activationFunction));
			}
			
			List<OutputNeuron> outputLayer = new ArrayList<OutputNeuron>(constructorTab[length-1]);
			for(int i=0; i<=outputLayer.size()-1; i++){
				outputLayer.add(new OutputNeuron(activationFunction, randomBiasOutput));
			}
			
			List<IntermediateLayer> hiddenLayers = new ArrayList<IntermediateLayer>(length-2);
			for(int i=1; i<=length-2; i++){
				IntermediateLayer res = new IntermediateLayer();
				for(int k=1; k<=constructorTab[i]; k++){
					res.getLayer().add(new IntermediateNeuron(activationFunction, randomBias));
				}
				hiddenLayers.add(res);
			}
			
			this.inputLayer.setLayer(inputLayer);
			this.hiddenLayers = hiddenLayers;
			this.outputLayer.setLayer(outputLayer);
		}
	}
		
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}
	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}
	
	public LearningAlgorithm getLearningAlgorithm() {
		return learningAlgorithm;
	}
	public void setLearningAlgorithm(LearningAlgorithm learningAlgorithm) {
		this.learningAlgorithm = learningAlgorithm;
	}
	
	public int[] getConstructorTab() {
		return constructorTab;
	}
	
	public List<InputNeuron> getInputlayer() {
		return this.inputLayer.getLayer();
	}
	
	public List<IntermediateLayer> getHiddenlayers() {
		return this.hiddenLayers;
	}
	
	public List<OutputNeuron> getOutputlayer() {
		return this.outputLayer.getLayer();
	}

	/*returns the nbLayer th layer layerORlist ?*/
	public List<> getLayer(int nbLayer) {
		if(nbLayer==0){return this.inputLayer.getLayer();}
		if(nbLayer==this.constructorTab.length-1){return this.outputLayer.getLayer();}
		else{return this.hiddenLayers.get(nbLayer-1).getLayer();}
	}
	/*or List<?> with .getLayer in here*/
	
	/*puts diff to 0*/
	public void reboot(){
		for (int i = 0; i <= this.getConstructorTab().length - 2; i++) {
			for (int j = 0; j <= this.getConstructorTab()[i] - 1; j++) {
				for(Synapse s : this.getLayer(i).get(j).getOutputsynapses()){
					s.setWeightdiff(0);
				}
			}
		}
		for (int i = this.getConstructorTab().length - 1; i >= 1; i--) {
			for (int j = 0; j <= this.getConstructorTab()[i] - 1; j++) {
				((Neuron) this.getLayer(i).get(j)).setNeurondiff(0);
				((Neuron) this.getLayer(i).get(j)).setBiasDiff(0);
			}
		}
		
		for (int j = 0; j <= this.getConstructorTab()[0] - 1; j++) {
			((Neuron) this.getLayer(0).get(j)).setNeurondiff(0);
		}
		
	}

	abstract public void linkNetwork();

}