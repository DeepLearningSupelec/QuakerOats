import java.util.ArrayList;
import java.util.List;

public abstract class NeuralNetwork {

	private int[] constructorTab;
	private ActivationFunction activationFunction;
//	private InputLayer inputLayer;
//	private List<IntermediateLayer> hiddenLayers;
//	private OutputLayer outputLayer;
	private ArrayList<InputNeuron> inputLayer;
	private List<ArrayList<IntermediateNeuron>> hiddenLayers;
	private ArrayList<OutputNeuron> outputLayer;
	private LearningAlgorithm learningAlgorithm;
	
	/*If the user doesn't give an activation function*/
//	public NeuralNetwork(int[] constructorTab) throws InvalidNetworkConstruction{
//		/*exception if the constructor array length is too small*/
//		if(constructorTab.length<3){
//			throw new InvalidNetworkConstruction();
//		}
//		else{
//			this.constructorTab = constructorTab;
//			this.learningAlgorithm = new BackProp(this);
//			int length = constructorTab.length;
//			/*only 1 intermediate layer for now, it's ok to give the "same" random bias, see later*/
//			double randomBias = 2*(Math.random()-0.5)*2.4/constructorTab[1];
//			double randomBiasOutput = 2*(Math.random()-0.5)*2.4/constructorTab[length-1];
//			
//			ActivationFunction activationFunction = new Sigmoid();
//			
//			List<InputNeuron> inputLayer = new ArrayList<InputNeuron>(constructorTab[0]);
//			for(int i=0; i<=inputLayer.size()-1; i++){
//				inputLayer.add(new InputNeuron(activationFunction));
//			}
//			
//			List<OutputNeuron> outputLayer = new ArrayList<OutputNeuron>(constructorTab[length-1]);
//			for(int i=0; i<=outputLayer.size()-1; i++){
//				outputLayer.add(new OutputNeuron(activationFunction, randomBiasOutput));
//			}
//			
//			List<IntermediateLayer> hiddenLayers = new ArrayList<IntermediateLayer>(length-2);
//			for(int i=1; i<=length-2; i++){
//				IntermediateLayer iemeLayer = new IntermediateLayer(constructorTab[i]);
//				for(int k=1; k<=constructorTab[i]; k++){
//					iemeLayer.getLayer().add(new IntermediateNeuron(activationFunction, randomBias));
//				}
//				hiddenLayers.add(iemeLayer);
//			}
//			this.inputLayer = new InputLayer(constructorTab[0]);
//			this.outputLayer = new OutputLayer(constructorTab[length-1]);
//			
//			this.inputLayer.setLayer(inputLayer);
//			this.hiddenLayers = hiddenLayers;
//			this.outputLayer.setLayer(outputLayer);
//		}
//	}
//	/*If the user specifies an activation function and the learning algorithm*/
//	public NeuralNetwork(int[] constructorTab, ActivationFunction activationFunction, LearningAlgorithm learningAlgorithm) throws InvalidNetworkConstruction{
//		if(constructorTab.length<3){
//			throw new InvalidNetworkConstruction();
//		}
//		else{
//			this.setActivationFunction(activationFunction);
//			this.setLearningAlgorithm(learningAlgorithm);
//			this.getLearningAlgorithm().setNeuralNetwork(this);
//			this.constructorTab = constructorTab;
//			int length = constructorTab.length;
//			/*only 1 intermediate layer for now, it's ok to give the "same" random bias, see later*/
//			double randomBias = 2*(Math.random()-0.5)*2.4/constructorTab[1];
//			double randomBiasOutput = 2*(Math.random()-0.5)*2.4/constructorTab[length-1];
//			
//			List<InputNeuron> inputLayer = new ArrayList<InputNeuron>(constructorTab[0]);
//			for(int i=0; i<=inputLayer.size()-1; i++){
//				inputLayer.add(new InputNeuron(activationFunction));
//			}
//			
//			List<OutputNeuron> outputLayer = new ArrayList<OutputNeuron>(constructorTab[length-1]);
//			for(int i=0; i<=outputLayer.size()-1; i++){
//				outputLayer.add(new OutputNeuron(activationFunction, randomBiasOutput));
//			}
//			
//			List<IntermediateLayer> hiddenLayers = new ArrayList<IntermediateLayer>(length-2);
//			for(int i=1; i<=length-2; i++){
//				IntermediateLayer iemeLayer = new IntermediateLayer(constructorTab[i]);
//				for(int k=1; k<=constructorTab[i]; k++){
//					iemeLayer.getLayer().add(new IntermediateNeuron(activationFunction, randomBias));
//				}
//				hiddenLayers.add(iemeLayer);
//			}
//			this.inputLayer = new InputLayer(constructorTab[0]);
//			this.outputLayer = new OutputLayer(constructorTab[length-1]);
//			
//			this.inputLayer.setLayer(inputLayer);
//			this.hiddenLayers = hiddenLayers;
//			this.outputLayer.setLayer(outputLayer);
//		}
//	}
	
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
			
			ArrayList<InputNeuron> inputLayer = new ArrayList<InputNeuron>(constructorTab[0]);
			for(int i=0; i<=constructorTab[0]-1; i++){
				inputLayer.add(new InputNeuron(activationFunction));
			}
			
			ArrayList<OutputNeuron> outputLayer = new ArrayList<OutputNeuron>(constructorTab[length-1]);
			for(int i=0; i<=constructorTab[length-1]-1; i++){
				outputLayer.add(new OutputNeuron(activationFunction, randomBiasOutput));
			}
			
			List<ArrayList<IntermediateNeuron>> hiddenLayers = new ArrayList<ArrayList<IntermediateNeuron>>(length-2);
			for(int i=1; i<=length-2; i++){
				ArrayList<IntermediateNeuron> iemeLayer = new ArrayList<IntermediateNeuron>(constructorTab[i]);
				for(int k=1; k<=constructorTab[i]; k++){
					iemeLayer.add(new IntermediateNeuron(activationFunction, randomBias));
				}
				hiddenLayers.add(iemeLayer);
			}
			
			this.inputLayer = inputLayer;
			this.hiddenLayers = hiddenLayers;
			this.outputLayer = outputLayer;
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
		return this.inputLayer;
	}
	
	public List<ArrayList<IntermediateNeuron>> getHiddenlayers() {
		return this.hiddenLayers;
	}
	
	public List<OutputNeuron> getOutputlayer() {
		return this.outputLayer;
	}

	/*returns the nbLayer layer layerORlist ?*/
	public ArrayList<?> getLayer(int nbLayer) {
		if(nbLayer==0){return this.inputLayer;}
		if(nbLayer==this.constructorTab.length-1){return this.outputLayer;}
		else{return this.hiddenLayers.get(nbLayer-1);}
	}
	
	/*links the neurons, synapses stuff*/
	abstract public void linkNetwork();
	
	/*puts diff to 0*/
	abstract public void reboot();

}