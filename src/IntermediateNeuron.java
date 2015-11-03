
public class IntermediateNeuron extends Neuron{
	
	IntermediateNeuron(ActivationFunction activationfunction){
		super(activationfunction);
	}
	IntermediateNeuron(ActivationFunction activationfunction, double bias){
		super(activationfunction, bias);
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
