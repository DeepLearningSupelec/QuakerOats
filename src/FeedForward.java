
public class FeedForward extends NeuralNetwork {

	public FeedForward(int[] constructorTab) throws InvalidNetworkConstruction {
		super(constructorTab);
	}
	
	public FeedForward(int[] constructorTab, Boolean bias) throws InvalidNetworkConstruction {
		super(constructorTab, bias);
	}
	
	public FeedForward(int[] constructorTab, double learningRate,
			int epochSize, int numberOfEpochBetweenEachMeasure, Boolean bias) throws InvalidNetworkConstruction {
		super(constructorTab, learningRate, epochSize, numberOfEpochBetweenEachMeasure, bias);
	}

	/* links the network */
	public void linkNetwork() {
		for (int i = 0; i <= this.getConstructorTab().length - 2; i++) {
			for (int j = 0; j <= this.getConstructorTab()[i] - 1; j++) {
				for (int k = 0; k <= this.getConstructorTab()[i+1] - 1; k++) {
					Synapse s = new Synapse((Neuron) this.getLayer(i).get(j), (Neuron) this
							.getLayer(i+1).get(k));
					s.setWeight(2*(Math.random()-0.5)*2.38/Math.sqrt(this.getConstructorTab()[1]));
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
				((Neuron) this.getLayer(i).get(j)).setNeuronDiff(0);
				((Neuron) this.getLayer(i).get(j)).setBiasDiff(0);
			}
		}
		for (int j = 0; j <= this.getConstructorTab()[0] - 1; j++) {
			((Neuron) this.getLayer(0).get(j)).setNeuronDiff(0);
		}
		
	}

}
