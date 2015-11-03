import java.util.ArrayList;


public class InputNeuron extends Neuron{
	
	private double input;
	
	public InputNeuron(ActivationFunction activationfunction){
		super(activationfunction);
	}
	public double getInput(){
		return input;
	}
	public void setInput(double input){
	     this.input = input;
	}
	public ArrayList<Synapse> getInputsynapses(){
		return null;
	}
	
	public double getBias(){
		return 0;
	}
	
	public double getBiasDiff(){
		return 0;
	}
		
	/*input neurons just transmit the value, we don't apply the activation function, no bias*/
	public void activate(){
		this.setActivation(this.getActivationfunction().apply(input));
	}
	
}

