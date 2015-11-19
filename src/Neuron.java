import java.util.ArrayList;

public abstract class Neuron {
	
	private double activation;
	private double neuronDiff;
	private double bias;
	private double biasDiff;
	private ActivationFunction activationFunction;
	private ArrayList<Synapse> inputSynapses;
	private ArrayList<Synapse> outputSynapses;
	
	
	public Neuron(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
		this.inputSynapses = new ArrayList<Synapse>();
		this.outputSynapses = new ArrayList<Synapse>();
	}
	public Neuron(ActivationFunction activationFunction, double bias) {
		this.activationFunction = activationFunction;
		this.inputSynapses = new ArrayList<Synapse>();
		this.outputSynapses = new ArrayList<Synapse>();
		this.bias = bias;
	}
	
	abstract public void activate();
	
	public ActivationFunction getActivationFunction() {
		return this.activationFunction;
	}
	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}
	public ArrayList<Synapse> getInputSynapses() {
		return this.inputSynapses;
	}
	public void setInputSynapses(ArrayList<Synapse> inputSynapses) {
		this.inputSynapses = inputSynapses;
	}
	public ArrayList<Synapse> getOutputSynapses() {
		return this.outputSynapses;
	}
	public void setOutputSynapses(ArrayList<Synapse> outputSynapses) {
		this.outputSynapses = outputSynapses;
	}
	public double getActivation(){
		return this.activation;
	}
	public void setActivation(double activation){
		this.activation = activation;
	}
	public double getNeuronDiff(){
		return this.neuronDiff;
	}
	public void setNeuronDiff(double neurondiff){
		this.neuronDiff = neurondiff;
	}
	public double getBias() {
		return bias;
	}
	public void setBias(double bias) {
		this.bias = bias;
	}
	public double getBiasDiff() {
		return biasDiff;
	}
	public void setBiasDiff(double biasDiff) {
		this.biasDiff = biasDiff;
	}

}
