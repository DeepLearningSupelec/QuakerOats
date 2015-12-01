public class Sigmoid extends ActivationFunction {
	
	public Sigmoid() {
		super();
	}
	public double applyDerivative(Double x) {
		//return this.valueDerivative = Math.exp(-x)/((1+Math.exp(-x))*(1+Math.exp(-x)));
		return this.valueDerivative = 1/(1+Math.exp(-x))*(1-1/(1+Math.exp(-x)));
	}
	public double apply(Double x){
		return this.valueFunction = 1/(1+Math.exp(-x));
	}
	
}

