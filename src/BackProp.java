import java.util.List;

public class BackProp extends LearningAlgorithm {

	/* constructor */
	public BackProp(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	public BackProp(NeuralNetwork neuralNetwork, double learningRate,
			int epochSize, int numberOfEpochBetweenEachMeasure) {
		super(neuralNetwork, learningRate, epochSize,
				numberOfEpochBetweenEachMeasure);
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
				((Neuron) this.getNeuralNetwork().getLayer(i).get(j))
						.activate();
			}
		}

	}

	/*
	 * back propagates the error, neuronDiff and synapses's weightDiff are
	 * changed
	 */
	public void calculateNeuronAndWeightDiffs(double[] realOutput) {

		// Calculate neuronDiff and biasDiff for the output layer
		for (int k = 0; k <= this.getNeuralNetwork().getOutputlayer().size() - 1; k++) {
			double neuronDiff = -this
					.getNeuralNetwork()
					.getOutputlayer()
					.get(k)
					.getActivationFunction()
					.applyDerivative(
							this.getNeuralNetwork().getOutputlayer().get(k)
									.getActivation())
					* (realOutput[k] - this.getNeuralNetwork().getOutputlayer()
							.get(k).getActivation());
			/* updates the neuronDiff */
			this.getNeuralNetwork().getOutputlayer().get(k)
					.setNeuronDiff(neuronDiff);
			/* updates the biasDiff */
			this.getNeuralNetwork()
					.getOutputlayer()
					.get(k)
					.setBiasDiff(
							this.getNeuralNetwork().getOutputlayer().get(k)
									.getBiasDiff()
									+ neuronDiff);

		}

		// Update weightDiff between the last hidden layer and the output layer
		for (int k = 0; k <= this.getNeuralNetwork().getHiddenlayers()
				.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
				.size() - 1; k++) {
			for (int i = 0; i <= this.getNeuralNetwork().getOutputlayer()
					.size() - 1; i++) {
				double newDeltaWeight = this
						.getNeuralNetwork()
						.getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.get(k).getOutputSynapses().get(i).getOutputNeuron()
						.getNeuronDiff()
						* this.getNeuralNetwork()
								.getHiddenlayers()
								.get(this.getNeuralNetwork().getHiddenlayers()
										.size() - 1).get(k).getActivation();

				double oldDeltaWeight = this
						.getNeuralNetwork()
						.getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.get(k).getOutputSynapses().get(i).getWeightDiff();

				this.getNeuralNetwork()
						.getHiddenlayers()
						.get(this.getNeuralNetwork().getHiddenlayers().size() - 1)
						.get(k).getOutputSynapses().get(i)
						.setWeightDiff(oldDeltaWeight + newDeltaWeight);
			}
		}

		// Calculate neuronDiff and biasDiff for each neuron of the hidden
		// layers

		/* for each hidden layer */
		for (int k = this.getNeuralNetwork().getHiddenlayers().size() - 1; k >= 0; k--) {
			/* for each neuron in this layer */
			for (int i = 0; i <= this.getNeuralNetwork().getHiddenlayers()
					.get(k).size() - 1; i++) {
				double s = 0;
				/*
				 * for each output synapse of this neuron (we take k + 1 + 1
				 * because there is the input layer)
				 */
				for (int j = 0; j <= this.getNeuralNetwork()
						.getLayer(k + 1 + 1).size() - 1; j++) {
					/* weighted sum of the delta neurons */
					s += this.getNeuralNetwork().getHiddenlayers().get(k)
							.get(i).getOutputSynapses().get(j)
							.getOutputNeuron().getNeuronDiff()
							* this.getNeuralNetwork().getHiddenlayers().get(k)
									.get(i).getOutputSynapses().get(j)
									.getWeight();
				}
				double neuronDiff = this
						.getNeuralNetwork()
						.getHiddenlayers()
						.get(k)
						.get(i)
						.getActivationFunction()
						.applyDerivative(
								this.getNeuralNetwork().getHiddenlayers()
										.get(k).get(i).getActivation())
						* s;
				/* updates the neuronDiff */
				this.getNeuralNetwork().getHiddenlayers().get(k).get(i)
						.setNeuronDiff(neuronDiff);

				/* updates the biasDiff */
				this.getNeuralNetwork()
						.getHiddenlayers()
						.get(k)
						.get(i)
						.setBiasDiff(
								this.getNeuralNetwork().getHiddenlayers()
										.get(k).get(i).getBiasDiff()
										+ neuronDiff);
			}
		}

		// Update weightDiff between Input and all the hiddenLayers

		/*
		 * for each layer (- 2 because between last hidden and output already
		 * done)
		 */
		for (int k = this.getNeuralNetwork().getConstructorTab().length - 1 - 2; k >= 0; k--) {
			/* for each neuron in this layer k */
			for (int i = 0; i <= this.getNeuralNetwork().getLayer(k).size() - 1; i++) {
				/* for each output synapse of this neuron (layer k+1) */
				for (int j = 0; j <= this.getNeuralNetwork().getLayer(k + 1)
						.size() - 1; j++) {

					double oldDeltaWeight = ((Neuron) this.getNeuralNetwork()
							.getLayer(k).get(i)).getOutputSynapses().get(j)
							.getWeightDiff();

					double newDeltaWeight = ((Neuron) this.getNeuralNetwork()
							.getLayer(k).get(i)).getActivation()
							* ((Neuron) this.getNeuralNetwork().getLayer(k + 1)
									.get(j)).getNeuronDiff();

					((Neuron) this.getNeuralNetwork().getLayer(k).get(i))
							.getOutputSynapses().get(j)
							.setWeightDiff(oldDeltaWeight + newDeltaWeight);

				}
			}
		}

	}
	

	/* launches the training on an epoch*/
	public void train(double[][] inputs, double[][] outputs) {
		
		/* for each input, calculates the neuronsDiff and synapases's weightDiff */
		for (int i = 0; i <= inputs.length - 1; i++) {
			this.calculateActivations(inputs[i]);
			this.calculateNeuronAndWeightDiffs(outputs[i]);
		}
        
		/* when it's done for all inputs, changes the synapses's weight */
		/* for each layer from input layer to last hidden layer (for each layer of synapses) */
		for(int i=0; i<=this.getNeuralNetwork().getConstructorTab().length-2; i++){
			/* for each neuron in this layer */
			for(int j=0; j<=this.getNeuralNetwork().getLayer(i).size()-1; j++){
				/*for each outputSynapse of this neuron */
				for(int k=0; k<=this.getNeuralNetwork().getLayer(i+1).size()-1; k++){
					Synapse s = ((Neuron)this.getNeuralNetwork().getLayer(i).get(j)).getOutputSynapses().get(k);
					s.setWeight(s.getWeight()-this.learningRate*(s.getWeightDiff()));
				}
			}
		}
		
		/* reboot the system*/
		this.getNeuralNetwork().reboot();
		
	}
		
	/* launches the training on all the given data */
	public void globaltraining(double[][] inputsData, double[][] outputsData) {
		List<double[][]> inputsEpoch = splitIntoEpochs(inputsData);
		List<double[][]> outputsEpoch = splitIntoEpochs(outputsData);
		for (int i = 0; i <= inputsEpoch.size() - 1; i++) {
			this.train(inputsEpoch.get(i), outputsEpoch.get(i));
		}
	}
	
	/* launches the training on all the given data and gives statistical data to analyze perfomances*/
	public void globaltraining(double[][] inputsDataTraining, double[][] outputsDataTraining, double[][] inputsDataTest, double[][] outputsDataTest) {
		List<double[][]> inputsEpoch = splitIntoEpochs(inputsDataTraining);
		List<double[][]> outputsEpoch = splitIntoEpochs(outputsDataTraining);
		
		/* point zero, sans apprentissage */
		if(true){ //just to remove variable duplicated erro message
			double quadraticErrorTraining = 0;
			double percentageErrorTraining = 1;
			double nbOfErrorTraining = 0;
			for(int j=0; j<=inputsDataTraining.length - 1; j++){
				this.calculateActivations(inputsDataTraining[j]);
				int maxOutputIndex = 0;
				int realOutputIndex = 0;
				
				for(int k=0; k<=outputsDataTraining[0].length - 1; k++){
					double calculatedOutputK = this.getNeuralNetwork().getOutputlayer().get(k).getActivation();
					double calculatedOutputMax = this.getNeuralNetwork().getOutputlayer().get(maxOutputIndex).getActivation();
					
					/* quadratic error*/
					quadraticErrorTraining += 0.5*( outputsDataTraining[j][k] - calculatedOutputK )*( outputsDataTraining[j][k] - calculatedOutputK );
					/* percentage error*/
					if( calculatedOutputK > calculatedOutputMax ){maxOutputIndex = k;}
					if( outputsDataTraining[j][k]==1){realOutputIndex = k;}
				}
				/* if predicted well*/
				//if(maxOutputIndex == realOutputIndex){percentageErrorTraining -= 1/inputsDataTraining.length;}
				if(maxOutputIndex == realOutputIndex){nbOfErrorTraining += 1;}
			}
			percentageErrorTraining = 1 - nbOfErrorTraining/inputsDataTraining.length;
			
			/*statistics on test set*/
			double quadraticErrorTest = 0;
			double percentageErrorTest = 1;
			double nbOfErrorTest = 0;
			for(int j=0; j<=inputsDataTest.length - 1; j++){
				this.calculateActivations(inputsDataTest[j]);
				int maxOutputIndex = 0;
				int realOutputIndex = 0;
				
				for(int k=0; k<=outputsDataTest[0].length - 1; k++){
					double calculatedOutputK = this.getNeuralNetwork().getOutputlayer().get(k).getActivation();
					double calculatedOutputMax = this.getNeuralNetwork().getOutputlayer().get(maxOutputIndex).getActivation();
					
					/* quadratic error*/
					quadraticErrorTest += 0.5*( outputsDataTest[j][k] - calculatedOutputK )*( outputsDataTest[j][k] - calculatedOutputK );
					/* percentage error*/
					if( calculatedOutputK > calculatedOutputMax ){maxOutputIndex = k;}
					if( outputsDataTest[j][k]==1){realOutputIndex = k;}
				}
				/* if predicted well*/
				//if(maxOutputIndex == realOutputIndex){percentageErrorTest -= 1/inputsDataTest.length;}
				if(maxOutputIndex == realOutputIndex){nbOfErrorTest += 1;}
			}
			percentageErrorTest = 1 - nbOfErrorTest/inputsDataTraining.length;
			
			this.outputData.getData().add(new Output(quadraticErrorTraining, percentageErrorTraining, quadraticErrorTest, percentageErrorTest));
		}
		
		/* rest */
		int cpt = 0;
		for (int i = 0; i <= inputsEpoch.size() - 1; i++) {
			this.train(inputsEpoch.get(i), outputsEpoch.get(i));
			cpt += 1;
			if(cpt>=this.numberOfEpochBetweenEachMeasure){
				/*statistics on training set*/
				double quadraticErrorTraining = 0;
				double percentageErrorTraining = 1;
				double nbOfErrorTraining = 0;
				for(int j=0; j<=inputsDataTraining.length - 1; j++){
					this.calculateActivations(inputsDataTraining[j]);
					int maxOutputIndex = 0;
					int realOutputIndex = 0;
					
					for(int k=0; k<=outputsDataTraining[0].length - 1; k++){
						double calculatedOutputK = this.getNeuralNetwork().getOutputlayer().get(k).getActivation();
						double calculatedOutputMax = this.getNeuralNetwork().getOutputlayer().get(maxOutputIndex).getActivation();
						
						/* quadratic error*/
						quadraticErrorTraining += 0.5*( outputsDataTraining[j][k] - calculatedOutputK )*( outputsDataTraining[j][k] - calculatedOutputK );
						/* percentage error*/
						if( calculatedOutputK > calculatedOutputMax ){maxOutputIndex = k;}
						if( outputsDataTraining[j][k]==1){realOutputIndex = k;}
					}
					/* if predicted well*/
					//if(maxOutputIndex == realOutputIndex){percentageErrorTraining -= 1/inputsDataTraining.length;}
					if(maxOutputIndex == realOutputIndex){nbOfErrorTraining += 1;}
				}
				percentageErrorTraining = 1 - nbOfErrorTraining/inputsDataTraining.length;
				
				/*statistics on test set*/
				double quadraticErrorTest = 0;
				double percentageErrorTest = 1;
				double nbOfErrorTest = 0;
				for(int j=0; j<=inputsDataTest.length - 1; j++){
					this.calculateActivations(inputsDataTest[j]);
					int maxOutputIndex = 0;
					int realOutputIndex = 0;
					
					for(int k=0; k<=outputsDataTest[0].length - 1; k++){
						double calculatedOutputK = this.getNeuralNetwork().getOutputlayer().get(k).getActivation();
						double calculatedOutputMax = this.getNeuralNetwork().getOutputlayer().get(maxOutputIndex).getActivation();
						
						/* quadratic error*/
						quadraticErrorTest += 0.5*( outputsDataTest[j][k] - calculatedOutputK )*( outputsDataTest[j][k] - calculatedOutputK );
						/* percentage error*/
						if( calculatedOutputK > calculatedOutputMax ){maxOutputIndex = k;}
						if( outputsDataTest[j][k]==1){realOutputIndex = k;}
					}
					/* if predicted well*/
					//if(maxOutputIndex == realOutputIndex){percentageErrorTest -= 1/inputsDataTest.length;}
					if(maxOutputIndex == realOutputIndex){nbOfErrorTest += 1;}
				}
				percentageErrorTest = 1 - nbOfErrorTest/inputsDataTest.length;
				
				this.outputData.getData().add(new Output(quadraticErrorTraining, percentageErrorTraining, quadraticErrorTest, percentageErrorTest));
				
				cpt = 0;
			}
		}
	}

}
