
public class FeedForward extends NeuralNetwork {

	/* constructor */
	public FeedForward(int[] constructorTab) throws InvalidNetworkConstruction {
		super(constructorTab);
	}

	public FeedForward(int[] constructorTab,
			ActivationFunction activationFunction,
			LearningAlgorithm learningAlgorithm)
			throws InvalidNetworkConstruction {
		super(constructorTab, activationFunction, learningAlgorithm);
	}

	/* links the network */
	public void linkNetwork() {
		for (int i = 0; i <= this.getConstructorTab().length - 2; i++) {
			for (int j = 0; j <= this.getConstructorTab()[i] - 1; j++) {
				for (int k = 0; k <= this.getConstructorTab()[i + 1] - 1; k++) {
					Synapse a = new Synapse(this.getLayer(i).get(j), this
							.getLayer(i + 1).get(k));
					((Neuron) this.getLayer(i).get(j)).getOutputsynapses().add(
							a);
					((Neuron) this.getLayer(i + 1).get(k)).getInputsynapses()
							.add(a);
				}
			}
		}
	}

}
