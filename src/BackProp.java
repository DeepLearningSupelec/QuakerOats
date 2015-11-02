import java.util.ArrayList;
import java.util.List;

public class BackProp extends LearningAlgorithm {

	/* constructor */
	public BackProp(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	public BackProp(NeuralNetwork neuralNetwork, double learningRate,
			int epochSize) {
		super(neuralNetwork, learningRate, epochSize);
	}

	/* forward propagates the input, neuron's activation are changed */
	public void calculateActivations(double[] input) {

		/* present an input to the network */
		for (int i = 0; i <= input.length - 1; i++) {
			this.getNeuralNetwork().getInputlayer().getLayer().get(i).setInput(input[i]);
		}

		/* forward propagates the input by activating each neuron */
		for (int i = 0; i <= this.getNeuralNetwork().getInputlayer().getLayer().size() - 1; i++) {
			this.getNeuralNetwork().getInputlayer().getLayer().get(i).activate();
		}
		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers().size() - 1; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
					.getLayer().size() - 1; i++) {
				this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i).activate();
			}
		}
		for (int i = 0; i <= this.getNeuralNetwork().getOutputlayer().getLayer().size() - 1; i++) {
			this.getNeuralNetwork().getOutputlayer().getLayer().get(i).activate();
		}

	}

	/*
	 * back propagates the error, neuron's diff and synapses's weight diff are
	 * changed
	 */
	public void calculateNeuronAndWeightDiffs(double[] ouput) {

		// Calculate neuron diff for the output layer
		for (int k = 0; k <= this.getNeuralNetwork().getOutputlayer().getLayer().size() - 1; k++) {
			double delta = this.getNeuralNetwork().getOutputlayer().getLayer().get(k)
					.getActivationfunction().applyDerivative(
							this.getNeuralNetwork().getOutputlayer().getLayer().get(k)
									.getActivation())
					* (ouput[k]-this.getNeuralNetwork().getOutputlayer().getLayer().get(k).getActivation());
			this.getNeuralNetwork().getOutputlayer().getLayer().get(k).setNeurondiff(delta);
			/*updates the bias*/
			this.getNeuralNetwork().getOutputlayer().getLayer().get(k).setBiasDiff(this.getNeuralNetwork().getOutputlayer().getLayer().get(k).getBiasDiff() + delta);
			
		}

		// Update weight diff between the last hidden layer and the output layer
		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers()
				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1).getLayer().size() - 1; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getOutputlayer().getLayer().size() - 1; i++) {
				double deltaweight = this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.getLayer().get(k).getOutputsynapses().get(i).getOutputneuron()
						.getNeurondiff()
						* this.getNeuralNetwork()
								.getHiddenlayers()
								.get(this.getNeuralNetwork().getHiddenlayers()
										.size() - 1).getLayer().get(k).getActivation();
				double a = this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.getLayer().get(k).getOutputsynapses().get(i).getWeightdiff();
				this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.getLayer().get(k).getOutputsynapses().get(i).setWeightdiff(a
						+ deltaweight);
				//System.out.println(a+deltaweight);
			}
		}
		// Calculate neurondiff for each Neuron of the hiddenlayers

		/* for each layer */
		for (int k = this.getNeuralNetwork().getHiddenlayers().size() - 2; k >= 0; k--) {
			/* for each neuron in this layer */
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
					.getLayer().size() - 1; i++) {
				double s = 0;
				/* for each output synapse of this neuron */
				for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers()
						.get(k + 1).getLayer().size() - 1; j++) {
					s += this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
							.getOutputsynapses().get(j).getOutputneuron()
							.getNeurondiff()
							* this.getNeuralNetwork().getHiddenlayers().get(k)
									.getLayer().get(i).getOutputsynapses().get(j).getWeight();
				}
				double delta = this.getNeuralNetwork()
						.getHiddenlayers()
						.get(k)
						.getLayer().get(i)
						.getActivationfunction()
						.applyDerivative(
								this.getNeuralNetwork().getHiddenlayers().get(k)
										.getLayer().get(i).getActivation())
						* s;
				//System.out.println(delta);
				this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
						.setNeurondiff(delta);
				/*updates the bias*/
				this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i).setBiasDiff(this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i).getBiasDiff() + delta);
			}
		}
		// Neurondiff for first hiddenlayer
		for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(0)
				.getLayer().size() - 1; i++) {
			double s = 0;
			/* for each output synapse of this neuron */
			for (int j = 0; j <= this.getNeuralNetwork().getOutputlayer().getLayer().size() - 1; j++) {
				s += this.getNeuralNetwork().getHiddenlayers().get(0).getLayer().get(i)
						.getOutputsynapses().get(j).getOutputneuron()
						.getNeurondiff()
						* this.getNeuralNetwork().getHiddenlayers().get(0)
								.getLayer().get(i).getOutputsynapses().get(j).getWeight();
			}
			double delta = this.getNeuralNetwork()
					.getHiddenlayers()
					.get(0)
					.getLayer().get(i)
					.getActivationfunction()
					.applyDerivative(
							this.getNeuralNetwork().getHiddenlayers().get(0)
									.getLayer().get(i).getActivation())
					* s;
			//System.out.println(delta);
			this.getNeuralNetwork().getHiddenlayers().get(0).getLayer().get(i)
					.setNeurondiff(delta);
			/*updates the bias*/
			this.getNeuralNetwork().getHiddenlayers().get(0).getLayer().get(i).setBiasDiff(this.getNeuralNetwork().getHiddenlayers().get(0).getLayer().get(i).getBiasDiff() + delta);
		}
	
		
		// Update weightdiff between two neurons in hiddenlayers.
		for (int k = this.getNeuralNetwork().getHiddenlayers().size() - 2; k >= 0; k--) {
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
					.getLayer().size() - 1; i++) {
				for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers()
						.get(k + 1).getLayer().size() - 1; j++) {
					double delta = this.getNeuralNetwork().getHiddenlayers().get(k)
							.getLayer().get(i).getOutputsynapses().get(j).getWeightdiff()
							+ this.getNeuralNetwork().getHiddenlayers().get(k)
									.getLayer().get(i).getActivation()
							* this.getNeuralNetwork().getHiddenlayers().get(k)
									.getLayer().get(i).getOutputsynapses().get(j)
									.getOutputneuron().getNeurondiff();
					this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
							.getOutputsynapses().get(j).setWeightdiff(delta);
					//System.out.println(this.neuralNetwork.getHiddenlayers().get(k)
							//.get(i).getActivation());
				}
			}
		}
		// Update weightdiff between the input layer and the first hiddenlayer.
		for (int k = 0; k <= this.getNeuralNetwork().getInputlayer().getLayer().size() - 1; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(0)
					.getLayer().size() - 1; i++) {
				double delta = this.getNeuralNetwork().getInputlayer().getLayer().get(k)
						.getOutputsynapses().get(i).getOutputneuron()
						.getNeurondiff()
						* this.getNeuralNetwork().getInputlayer().getLayer().get(k).getActivation()
						+ this.getNeuralNetwork().getInputlayer().getLayer().get(k)
								.getOutputsynapses().get(i).getWeightdiff();
				//System.out.println(this.neuralNetwork.getInputlayer()[k]
					//	.getOutputsynapses().get(i).getOutputneuron()
					//	.getNeurondiff());
				//System.out.println(this.neuralNetwork.getHiddenlayers().get(0).get(i).getNeurondiff());
				this.getNeuralNetwork().getInputlayer().getLayer().get(k).getOutputsynapses().get(i)
						.setWeightdiff(delta);
				//System.out.println(this.neuralNetwork.getInputlayer()[k]
						//.getOutputsynapses().get(i).getWeightdiff());
			}
		}

	}

	/* launches the training */
	public void train(double[][] inputs, double[][] outputs) {
		double errorperepoch = 0;
		/*
		 * for each input, calculates the neurons's diff and synapases's weight
		 * diff
		 */
		for (int i = 0; i <= inputs.length - 1; i++) {
			this.calculateActivations(inputs[i]);
			this.calculateNeuronAndWeightDiffs(outputs[i]);
			double norme2 = 0;
			for (int j = 0; j <= outputs[0].length - 1; j++) {
				norme2 = norme2
						+ (this.getNeuralNetwork().getOutputlayer().getLayer().get(j)
								.getActivation() - outputs[i][j])
						* (this.getNeuralNetwork().getOutputlayer().getLayer().get(j)
								.getActivation() - outputs[i][j]) / 2
						/ this.epochSize;
			}
			errorperepoch = errorperepoch + norme2;
		}
		this.trainerror.add(errorperepoch);
		this.testerror.add(errorperepoch);
        
		/* changes the neurons's value and the synapses's weight */
		for (int i = 0; i <= this.getNeuralNetwork().getInputlayer().getLayer().size() - 1; i++) {
			for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers().get(0)
					.getLayer().size() - 1; j++) {
				/*
				 * weight = weight + learningrate*weight diff // input/hidden
				 * layers
				 */
				this.getNeuralNetwork().getInputlayer().getLayer().get(i).getOutputsynapses().get(j)
						.setWeight(this.getNeuralNetwork().getInputlayer().getLayer().get(i)
								.getOutputsynapses().get(j).getWeight()
								+ this.learningRate
								* this.getNeuralNetwork().getInputlayer().getLayer().get(i)
										.getOutputsynapses().get(j).getWeightdiff());
			}
		}
		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers().size() - 2; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
					.getLayer().size() - 1; i++) {
				for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers()
						.get(k + 1).getLayer().size() - 1; j++) {
					/*
					 * weight = weight + learningrate*weight diff // hidden
					 * layers
					 */
					this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
							.getOutputsynapses().get(j)
							.setWeight(this.getNeuralNetwork().getHiddenlayers()
									.get(k).getLayer().get(i).getOutputsynapses().get(j)
									.getWeight()
									+ this.learningRate
									* this.getNeuralNetwork().getHiddenlayers()
											.get(k).getLayer().get(i).getOutputsynapses().get(j)
											.getWeightdiff());
				}
			}
		}
		
		for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers()
				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1).getLayer().size() - 1; i++) {
			for (int j = 0; j <= this.getNeuralNetwork().getOutputlayer().getLayer().size() - 1; j++) {
				/*
				 * weight = weight + learningrate*weight diff // hidden
				 * layers/output, can be integrated in the previous case but
				 * doesn't matter for now
				 */
				this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.getLayer().get(i).getOutputsynapses().get(j)
						.setWeight(this.getNeuralNetwork()
								.getHiddenlayers()
								.get(this.getNeuralNetwork().getHiddenlayers()
										.size() - 1).getLayer().get(i).getOutputsynapses().get(j)
								.getWeight()
								+ this.learningRate
								* this.getNeuralNetwork()
										.getHiddenlayers()
										.get(this.getNeuralNetwork()
												.getHiddenlayers().size() - 1)
										.getLayer().get(i).getOutputsynapses().get(j)
										.getWeightdiff());
			}
		}
		//System.out.println(this.neuralNetwork.getHiddenlayers().get(0).get(8).getOutputsynapses().get(3).getWeight());
	}

	public void globaltraining(double[][] inputsdata, double[][] outputsdata) {
		List<double[][]> inputsepoch = splitIntoEpochs(inputsdata);
		List<double[][]> outputsepoch = splitIntoEpochs(outputsdata);
		for (int i = 0; i <= inputsepoch.size() - 1; i++) {
			this.train(inputsepoch.get(i), outputsepoch.get(i));
			this.outputdata.data.add(new Output(this.epochSize,
					this.learningRate, this.trainerror.get(i), this.testerror
							.get(i)));
		}
	}

	/* launches the training (inputs = one epoch) */
	/* test during the training to study the effect of diverse parameters */
	public void train(double[][] inputsTraining, double[][] outputsTraining,
			double[][] inputsTest, double[][] outputsTest) {
		double errorPerEpochTraining = 0;
		double errorPerEpochTest = 0;
		/*
		 * for each input, calculates the neurons's diff and synapases's weight
		 * diff
		 */
		for (int i = 0; i <= inputsTraining.length - 1; i++) {
			this.calculateActivations(inputsTraining[i]);
			this.calculateNeuronAndWeightDiffs(inputsTraining[i]);
			// double norme2 = 0;
			// for(int j = 0; j<=outputsTraining.length - 1; j++){
			// norme2 += (this.neuralNetwork.getOutputlayer()[j].getActivation()
			// -
			// outputsTraining[i][j])*(this.neuralNetwork.getOutputlayer()[j].getActivation()
			// - outputsTraining[i][j])/2/inputsTraining.length;
			// }
			// errorPerEpochTraining += norme2;
		}
		// this.trainerror.add(errorPerEpochTraining);

		/* changes the neurons's value and the synapses's weight */
		for (int i = 0; i <= this.getNeuralNetwork().getInputlayer().getLayer().size() - 1; i++) {
			for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers().get(0)
					.getLayer().size() - 1; j++) {
				/*
				 * weight = weight + learningrate*weight diff // input/hidden
				 * layers
				 */
				this.getNeuralNetwork().getInputlayer().getLayer().get(i).getOutputsynapses().get(j)
						.setWeight(this.getNeuralNetwork().getInputlayer().getLayer().get(i)
								.getOutputsynapses().get(j).getWeight()
								+ this.learningRate
								* this.getNeuralNetwork().getInputlayer().getLayer().get(i)
										.getOutputsynapses().get(j).getWeightdiff());
			}
		}
		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers().size() - 2; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
					.getLayer().size() - 1; i++) {
				for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers()
						.get(k + 1).getLayer().size() - 1; j++) {
					/*
					 * weight = weight + learningrate*weight diff // hidden
					 * layers
					 */
					this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
							.getOutputsynapses().get(j)
							.setWeight(this.getNeuralNetwork().getHiddenlayers()
									.get(k).getLayer().get(i).getOutputsynapses().get(j)
									.getWeight()
									+ this.learningRate
									* this.getNeuralNetwork().getHiddenlayers()
											.get(k).getLayer().get(i).getOutputsynapses().get(j)
											.getWeightdiff());
				}
			}
		}
		for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers()
				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1).getLayer().size() - 1; i++) {
			for (int j = 0; j <= this.getNeuralNetwork().getOutputlayer().getLayer().size() - 1; j++) {
				/*
				 * weight = weight + learningrate*weight diff // hidden
				 * layers/output, can be integrated in the previous case but
				 * doesn't matter for now
				 */
				this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.getLayer().get(i).getOutputsynapses().get(j)
						.setWeight(this.getNeuralNetwork()
								.getHiddenlayers()
								.get(this.getNeuralNetwork().getHiddenlayers()
										.size() - 1).getLayer().get(i).getOutputsynapses().get(j)
								.getWeight()
								+ this.learningRate
								* this.getNeuralNetwork()
										.getHiddenlayers()
										.get(this.getNeuralNetwork()
												.getHiddenlayers().size() - 1)
										.getLayer().get(i).getOutputsynapses().get(j)
										.getWeightdiff());
			}
		}

		/* Training error */
		for (int i = 0; i <= inputsTraining.length - 1; i++) {
			this.calculateActivations(inputsTraining[i]);
			double norme2 = 0;
			for (int j = 0; j <= outputsTraining.length - 1; j++) {
				norme2 += (this.getNeuralNetwork().getOutputlayer().getLayer().get(j)
						.getActivation() - outputsTraining[i][j])
						* (this.getNeuralNetwork().getOutputlayer().getLayer().get(j)
								.getActivation() - outputsTraining[i][j])
						/ 2
						/ inputsTraining.length;
			}
			errorPerEpochTraining += norme2;
		}
		this.trainerror.add(errorPerEpochTraining);

		/* Test error */
		for (int i = 0; i <= inputsTest.length - 1; i++) {
			this.calculateActivations(inputsTest[i]);
			double norme2 = 0;
			for (int j = 0; j <= outputsTest.length - 1; j++) {
				norme2 += (this.getNeuralNetwork().getOutputlayer().getLayer().get(j)
						.getActivation() - outputsTest[i][j])
						* (this.getNeuralNetwork().getOutputlayer().getLayer().get(j)
								.getActivation() - outputsTest[i][j])
						/ 2
						/ inputsTest.length;
			}
			errorPerEpochTest += norme2;
		}
		this.testerror.add(errorPerEpochTest);

	}

	/* launches the training (inputs = all the epochs) */
	/* test during the training to study the effect of diverse parameters */
	public void globaltraining(double[][] inputsTraining,
			double[][] outputsTraining, double[][] inputsTest,
			double[][] outputsTest) {

		List<double[][]> inputsTrainingEpochs = splitIntoEpochs(inputsTraining);
		List<double[][]> outputsTrainingEpochs = splitIntoEpochs(outputsTraining);
		/* problem, not the same epoch sizes ? */
		List<double[][]> inputsTestEpochs = splitIntoEpochs(inputsTest);
		List<double[][]> outputsTestEpochs = splitIntoEpochs(outputsTest);

		for (int i = 0; i <= inputsTrainingEpochs.size() - 1; i++) {
			this.train(inputsTrainingEpochs.get(i),
					outputsTrainingEpochs.get(i), inputsTestEpochs.get(i),
					outputsTestEpochs.get(i));
			this.outputdata.data.add(new Output(this.epochSize,
					this.learningRate, this.trainerror.get(i), this.testerror
							.get(i)));
		}
	}
}
