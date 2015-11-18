
public class Synapse {
	
	private double weight;
	private double weightDiff;
	private Neuron inputNeuron;
	private Neuron outputNeuron;
	
	public Synapse(Neuron inputNeuron, Neuron outputNeuron) {
		this.inputNeuron = inputNeuron;
		this.outputNeuron = outputNeuron;
	}
	public double getWeight() {
		return this.weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getWeightDiff() {
		return this.weightDiff;
	}
	public void setWeightDiff(double weightDiff) {
		this.weightDiff = weightDiff;
	}
	public Neuron getInputNeuron() {
		return this.inputNeuron;
	}
	public void setInputNeuron(Neuron inputNeuron) {
		this.inputNeuron = inputNeuron;
	}
	public Neuron getOutputNeuron() {
		return this.outputNeuron;
	}
	public void setOutputNeuron(Neuron outputNeuron) {
		this.outputNeuron = outputNeuron;
	}

}
