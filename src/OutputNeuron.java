import java.util.ArrayList;


public class OutputNeuron extends Neuron{
	
	OutputNeuron(ActivationFunction activationfunction){
		super(activationfunction);
	}
	OutputNeuron(ActivationFunction activationfunction, double bias){
		super(activationfunction, bias);
	}

	public ArrayList<Synapse> getOutputsynapses(){
		return null;
	}
	
	public void activate(){
		double s = 0;
		for(int i = 0; i <= this.getInputsynapses().size()-1; i++){
			s += this.getInputsynapses().get(i).getWeight()
				*this.getInputsynapses().get(i).getInputneuron().getActivation();
		}
		s += this.getBias();
		this.setActivation(this.getActivationfunction().apply(s));
	
	}
	
}
