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
			this.getNeuralNetwork().getInputlayer().get(i).setInput(input[i]);
		}

		/* forward propagates the input by activating each neuron */
		for (int i = 0; i <= this.getNeuralNetwork().getConstructorTab().length - 1; i++) {
			for (int j = 0; j <= this.getNeuralNetwork().getConstructorTab()[i] - 1; j++) {
				((Neuron) this.getNeuralNetwork().getLayer(i).get(j)).activate();
			}
		}

	}

	/*
	 * back propagates the error, neuronDiff and synapses's weightDiff are
	 * changed
	 */
	public void calculateNeuronAndWeightDiffs(double[] realOutput) {
		
		// Calculate neuron and biasDiff for the output layer
		for (int k = 0; k <= this.getNeuralNetwork().getOutputlayer().size() - 1; k++) {
			double neuronDiff = - this.getNeuralNetwork().getOutputlayer().get(k)
					.getActivationFunction().applyDerivative(
							this.getNeuralNetwork().getOutputlayer().get(k)
									.getActivation())
					* (realOutput[k]-this.getNeuralNetwork().getOutputlayer().get(k).getActivation());
			/*updates the neuronDiff*/
			this.getNeuralNetwork().getOutputlayer().get(k).setNeurondiff(neuronDiff);
			/*updates the biasDiff*/
			this.getNeuralNetwork().getOutputlayer().get(k).setBiasDiff(this.getNeuralNetwork().getOutputlayer().get(k).getBiasDiff() + neuronDiff);
			
		}
		
		
		//Update weightDiff between the last hidden layer and the output layer
		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers()
				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1).size() - 1; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getOutputlayer().size() - 1; i++) {
				double newDeltaWeight = this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.get(k).getOutputSynapses().get(i).getOutputNeuron()
						.getNeurondiff()
						* this.getNeuralNetwork()
								.getHiddenlayers()
								.get(this.getNeuralNetwork().getHiddenlayers()
										.size() - 1).get(k).getActivation();
				
				double oldDeltaWeight = this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.get(k).getOutputSynapses().get(i).getWeightDiff();
				
				this.getNeuralNetwork().getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.get(k).getOutputSynapses().get(i).setWeightDiff(oldDeltaWeight
						+ newDeltaWeight);
			}
		}
		
		
		// Calculate neuron and bias diff for each neuron of the hidden layers
		
		/* for each hidden layer */
		for (int k = this.getNeuralNetwork().getHiddenlayers().size() - 1; k >= 0; k--) {
			/* for each neuron in this layer */
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
					.size() - 1; i++) {
				double s = 0;
				/* for each output synapse of this neuron (we take k + 1 + 1 because there is the input layer) */
				for (int j = 0; j <= this.getNeuralNetwork().getLayer(k + 1 + 1).size() - 1; j++) {
					/* weighted sum of the delta neurons */
					s += this.getNeuralNetwork().getHiddenlayers().get(k).get(i)
							.getOutputSynapses().get(j).getOutputNeuron()
							.getNeurondiff()
							* this.getNeuralNetwork().getHiddenlayers().get(k)
									.get(i).getOutputSynapses().get(j).getWeight();
				}
				double neuronDiff = this.getNeuralNetwork()
						.getHiddenlayers()
						.get(k)
						.get(i)
						.getActivationFunction()
						.applyDerivative(
								this.getNeuralNetwork().getHiddenlayers().get(k)
										.get(i).getActivation())
						* s;
				/* updates the neuronDiff*/
				this.getNeuralNetwork().getHiddenlayers().get(k).get(i)
						.setNeurondiff(neuronDiff);
				
				/*updates the biasDiff*/
				this.getNeuralNetwork().getHiddenlayers().get(k).get(i).setBiasDiff(this.getNeuralNetwork().getHiddenlayers().get(k).get(i).getBiasDiff() + neuronDiff);
			}
		}
		
		
		// Update weightDiff between two neurons in hiddenLayers
		
		/* for each layer (length - 1 to respect the indexation, - 2 because 
		 * between last hidden and output already done*/
		for (int k = this.getNeuralNetwork().getConstructorTab().length - 1 - 2; k >= 0; k--) {
			/* for each neuron in this layer k */
			for (int i = 0; i <= this.getNeuralNetwork().getLayer(k).size() - 1; i++) {
				/* for each output synapse of this neuron (layer k+1) */
				for (int j = 0; j <= this.getNeuralNetwork().getLayer(k + 1).size() - 1; j++) {
					
					double oldDeltaWeight = ((Neuron) this.getNeuralNetwork()
							.getLayer(k).get(i)).getOutputSynapses().get(j)
							.getWeightDiff();

					double newDeltaWeight = ((Neuron) this.getNeuralNetwork()
							.getLayer(k).get(i)).getActivation()
							* ((Neuron) this.getNeuralNetwork().getLayer(k + 1)
									.get(j)).getNeurondiff();
					
					((Neuron) this.getNeuralNetwork().getLayer(k).get(i))
							.getOutputSynapses().get(j).setWeightDiff(oldDeltaWeight + newDeltaWeight);
					
				}
			}
		}
		
		
	}
	

//	/* launches the training */
//	public void train(double[][] inputs, double[][] outputs) {
//		double errorperepoch = 0;
//		/*
//		 * for each input, calculates the neurons's diff and synapases's weight
//		 * diff
//		 */
//		for (int i = 0; i <= inputs.length - 1; i++) {
//			this.calculateActivations(inputs[i]);
//			this.calculateNeuronAndWeightDiffs(outputs[i]);
//			double norme2 = 0;
//			for (int j = 0; j <= outputs[0].length - 1; j++) {
//				norme2 = norme2
//						+ (this.getNeuralNetwork().getOutputlayer().get(j)
//								.getActivation() - outputs[i][j])
//						* (this.getNeuralNetwork().getOutputlayer().get(j)
//								.getActivation() - outputs[i][j]) / 2
//						/ this.epochSize;
//			}
//			errorperepoch = errorperepoch + norme2;
//		}
//		this.trainError.add(errorperepoch);
//		this.testError.add(errorperepoch);
//        
//		/* changes the neurons's value and the synapses's weight */
//		for (int i = 0; i <= this.getNeuralNetwork().getInputlayer().size() - 1; i++) {
//			for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers().get(0)
//					.getLayer().size() - 1; j++) {
//				/*
//				 * weight = weight + learningrate*weight diff // input/hidden
//				 * layers
//				 */
//				this.getNeuralNetwork().getInputlayer().get(i).getOutputSynapses().get(j)
//						.setWeight(this.getNeuralNetwork().getInputlayer().get(i)
//								.getOutputSynapses().get(j).getWeight()
//								+ this.learningRate
//								* this.getNeuralNetwork().getInputlayer().get(i)
//										.getOutputSynapses().get(j).getWeightDiff());
//			}
//		}
//		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers().size() - 2; k++) {
//			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
//					.getLayer().size() - 1; i++) {
//				for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers()
//						.get(k + 1).getLayer().size() - 1; j++) {
//					/*
//					 * weight = weight + learningrate*weight diff // hidden
//					 * layers
//					 */
//					this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
//							.getOutputSynapses().get(j)
//							.setWeight(this.getNeuralNetwork().getHiddenlayers()
//									.get(k).getLayer().get(i).getOutputSynapses().get(j)
//									.getWeight()
//									+ this.learningRate
//									* this.getNeuralNetwork().getHiddenlayers()
//											.get(k).getLayer().get(i).getOutputSynapses().get(j)
//											.getWeightDiff());
//				}
//			}
//		}
//		
//		for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers()
//				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1).getLayer().size() - 1; i++) {
//			for (int j = 0; j <= this.getNeuralNetwork().getOutputlayer().size() - 1; j++) {
//				/*
//				 * weight = weight + learningrate*weight diff // hidden
//				 * layers/output, can be integrated in the previous case but
//				 * doesn't matter for now
//				 */
//				this.getNeuralNetwork().getHiddenlayers()
//						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
//						.getLayer().get(i).getOutputSynapses().get(j)
//						.setWeight(this.getNeuralNetwork()
//								.getHiddenlayers()
//								.get(this.getNeuralNetwork().getHiddenlayers()
//										.size() - 1).getLayer().get(i).getOutputSynapses().get(j)
//								.getWeight()
//								+ this.learningRate
//								* this.getNeuralNetwork()
//										.getHiddenlayers()
//										.get(this.getNeuralNetwork()
//												.getHiddenlayers().size() - 1)
//										.getLayer().get(i).getOutputSynapses().get(j)
//										.getWeightDiff());
//			}
//		}
//		//System.out.println(this.neuralNetwork.getHiddenlayers().get(0).get(8).getOutputsynapses().get(3).getWeight());
//	}
//
//	public void globaltraining(double[][] inputsdata, double[][] outputsdata) {
//		List<double[][]> inputsepoch = splitIntoEpochs(inputsdata);
//		List<double[][]> outputsepoch = splitIntoEpochs(outputsdata);
//		for (int i = 0; i <= inputsepoch.size() - 1; i++) {
//			this.train(inputsepoch.get(i), outputsepoch.get(i));
//			this.outputdata.data.add(new Output(this.epochSize,
//					this.learningRate, this.trainError.get(i), this.testError
//							.get(i)));
//		}
//	}
//
//	/*test + training*/
//	
//	/* launches the training (inputs = one epoch) */
//	public void train(double[][] inputsTraining, double[][] outputsTraining,
//			double[][] inputsTest, double[][] outputsTest) {
//		double errorPerEpochTraining = 0;
//		double errorPerEpochTest = 0;
//		/*
//		 * for each input, calculates the neurons's diff and synapases's weight
//		 * diff
//		 */
//		for (int i = 0; i <= inputsTraining.length - 1; i++) {
//			this.calculateActivations(inputsTraining[i]);
//			this.calculateNeuronAndWeightDiffs(inputsTraining[i]);
//			// double norme2 = 0;
//			// for(int j = 0; j<=outputsTraining.length - 1; j++){
//			// norme2 += (this.neuralNetwork.getOutputlayer()[j].getActivation()
//			// -
//			// outputsTraining[i][j])*(this.neuralNetwork.getOutputlayer()[j].getActivation()
//			// - outputsTraining[i][j])/2/inputsTraining.length;
//			// }
//			// errorPerEpochTraining += norme2;
//		}
//		// this.trainerror.add(errorPerEpochTraining);
//
//		/* changes the neurons's value and the synapses's weight */
//		for (int i = 0; i <= this.getNeuralNetwork().getInputlayer().size() - 1; i++) {
//			for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers().get(0)
//					.getLayer().size() - 1; j++) {
//				/*
//				 * weight = weight + learningrate*weight diff // input/hidden
//				 * layers
//				 */
//				this.getNeuralNetwork().getInputlayer().get(i).getOutputSynapses().get(j)
//						.setWeight(this.getNeuralNetwork().getInputlayer().get(i)
//								.getOutputSynapses().get(j).getWeight()
//								+ this.learningRate
//								* this.getNeuralNetwork().getInputlayer().get(i)
//										.getOutputSynapses().get(j).getWeightDiff());
//			}
//		}
//		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers().size() - 2; k++) {
//			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers().get(k)
//					.getLayer().size() - 1; i++) {
//				for (int j = 0; j <= this.getNeuralNetwork().getHiddenlayers()
//						.get(k + 1).getLayer().size() - 1; j++) {
//					/*
//					 * weight = weight + learningrate*weight diff // hidden
//					 * layers
//					 */
//					this.getNeuralNetwork().getHiddenlayers().get(k).getLayer().get(i)
//							.getOutputSynapses().get(j)
//							.setWeight(this.getNeuralNetwork().getHiddenlayers()
//									.get(k).getLayer().get(i).getOutputSynapses().get(j)
//									.getWeight()
//									+ this.learningRate
//									* this.getNeuralNetwork().getHiddenlayers()
//											.get(k).getLayer().get(i).getOutputSynapses().get(j)
//											.getWeightDiff());
//				}
//			}
//		}
//		for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers()
//				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1).getLayer().size() - 1; i++) {
//			for (int j = 0; j <= this.getNeuralNetwork().getOutputlayer().size() - 1; j++) {
//				/*
//				 * weight = weight + learningrate*weight diff // hidden
//				 * layers/output, can be integrated in the previous case but
//				 * doesn't matter for now
//				 */
//				this.getNeuralNetwork().getHiddenlayers()
//						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
//						.getLayer().get(i).getOutputSynapses().get(j)
//						.setWeight(this.getNeuralNetwork()
//								.getHiddenlayers()
//								.get(this.getNeuralNetwork().getHiddenlayers()
//										.size() - 1).getLayer().get(i).getOutputSynapses().get(j)
//								.getWeight()
//								+ this.learningRate
//								* this.getNeuralNetwork()
//										.getHiddenlayers()
//										.get(this.getNeuralNetwork()
//												.getHiddenlayers().size() - 1)
//										.getLayer().get(i).getOutputSynapses().get(j)
//										.getWeightDiff());
//			}
//		}
//
//		/* Training error */
//		for (int i = 0; i <= inputsTraining.length - 1; i++) {
//			this.calculateActivations(inputsTraining[i]);
//			double norme2 = 0;
//			for (int j = 0; j <= outputsTraining.length - 1; j++) {
//				norme2 += (this.getNeuralNetwork().getOutputlayer().get(j)
//						.getActivation() - outputsTraining[i][j])
//						* (this.getNeuralNetwork().getOutputlayer().get(j)
//								.getActivation() - outputsTraining[i][j])
//						/ 2
//						/ inputsTraining.length;
//			}
//			errorPerEpochTraining += norme2;
//		}
//		this.trainError.add(errorPerEpochTraining);
//
//		/* Test error */
//		for (int i = 0; i <= inputsTest.length - 1; i++) {
//			this.calculateActivations(inputsTest[i]);
//			double norme2 = 0;
//			for (int j = 0; j <= outputsTest.length - 1; j++) {
//				norme2 += (this.getNeuralNetwork().getOutputlayer().get(j)
//						.getActivation() - outputsTest[i][j])
//						* (this.getNeuralNetwork().getOutputlayer().get(j)
//								.getActivation() - outputsTest[i][j])
//						/ 2
//						/ inputsTest.length;
//			}
//			errorPerEpochTest += norme2;
//		}
//		this.testError.add(errorPerEpochTest);
//
//	}
//
//	/* launches the training (inputs = all the epochs) */
//	public void globaltraining(double[][] inputsTraining,
//			double[][] outputsTraining, double[][] inputsTest,
//			double[][] outputsTest) {
//
//		List<double[][]> inputsTrainingEpochs = splitIntoEpochs(inputsTraining);
//		List<double[][]> outputsTrainingEpochs = splitIntoEpochs(outputsTraining);
//		/* problem, not the same epoch sizes ? */
//		List<double[][]> inputsTestEpochs = splitIntoEpochs(inputsTest);
//		List<double[][]> outputsTestEpochs = splitIntoEpochs(outputsTest);
//
//		for (int i = 0; i <= inputsTrainingEpochs.size() - 1; i++) {
//			this.train(inputsTrainingEpochs.get(i),
//					outputsTrainingEpochs.get(i), inputsTestEpochs.get(i),
//					outputsTestEpochs.get(i));
//			this.outputdata.data.add(new Output(this.epochSize,
//					this.learningRate, this.trainError.get(i), this.testError
//							.get(i)));
//		}
//	}
}
