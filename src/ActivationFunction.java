public abstract class ActivationFunction {
	
	public double valueFunction;
	public double valueDerivative;
	public ActivationFunction() {}
	abstract public double applyDerivative(Double x);
	abstract public double apply(Double x);

}
