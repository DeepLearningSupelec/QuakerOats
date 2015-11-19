
public class FeedForward extends NeuralNetwork {

	/* constructor */
	public FeedForward(int[] constructorTab) throws InvalidNetworkConstruction {
		super(constructorTab);
	}
	
	public FeedForward(int[] constructorTab, double bias) throws InvalidNetworkConstruction {
		super(constructorTab, bias);
	}

//	public FeedForward(int[] constructorTab,
//			ActivationFunction activationFunction,
//			LearningAlgorithm learningAlgorithm)
//			throws InvalidNetworkConstruction {
//		super(constructorTab, activationFunction, learningAlgorithm);
//	}

	/* links the network */
	public void linkNetwork() {
		for (int i = 0; i <= this.getConstructorTab().length - 2; i++) {
			for (int j = 0; j <= this.getConstructorTab()[i] - 1; j++) {
				for (int k = 0; k <= this.getConstructorTab()[i+1] - 1; k++) {
					Synapse s = new Synapse((Neuron) this.getLayer(i).get(j), (Neuron) this
							.getLayer(i+1).get(k));
					((Neuron) this.getLayer(i).get(j)).getOutputSynapses().add(
							s);
					((Neuron) this.getLayer(i + 1).get(k)).getInputSynapses()
							.add(s);
				}
			}
		}
	}
	
	public void reboot(){
		for (int i = 0; i <= this.getConstructorTab().length - 2; i++) {
			for (int j = 0; j <= this.getConstructorTab()[i] - 1; j++) {
				for(Synapse s : ((Neuron) this.getLayer(i).get(j)).getOutputSynapses()){
					s.setWeightDiff(0);
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

}
