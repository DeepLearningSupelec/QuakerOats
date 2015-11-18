import java.util.ArrayList;


public class OutputNeuron extends Neuron{
	
	OutputNeuron(ActivationFunction activationFunction){
		super(activationFunction);
	}
	OutputNeuron(ActivationFunction activationFunction, double bias){
		super(activationFunction, bias);
	}

	public ArrayList<Synapse> getOutputSynapses(){
		return null;
	}
	
	public void activate(){
		double s = 0;
		for(int i = 0; i <= this.getInputSynapses().size()-1; i++){
			s += this.getInputSynapses().get(i).getWeight()
				*this.getInputSynapses().get(i).getInputNeuron().getActivation();
		}
		s += this.getBias();
		this.setActivation(this.getActivationFunction().apply(s));
	
	}
	
}
