
public class testXOR {

	public static void main(String[] args) throws InvalidNetworkConstruction {
		// TODO Auto-generated method stub
		int[] constructorTab =new int[]{2,2,1};
		FeedForward ff = new FeedForward(constructorTab, false);

		//No need to reuse a constructor, it is already done when ff is constructed
		//BackProp bp = new BackProp(ff);
		LearningAlgorithm bp = ff.getLearningAlgorithm();
		
		ff.linkNetwork();
		
		ff.getInputlayer().get(0).getOutputSynapses().get(0).setWeight(0.8);
		ff.getInputlayer().get(0).getOutputSynapses().get(1).setWeight(-0.3);
		ff.getInputlayer().get(1).getOutputSynapses().get(0).setWeight(0.5);
		ff.getInputlayer().get(1).getOutputSynapses().get(1).setWeight(0.2);
		
		ff.getHiddenlayers().get(0).get(0).getOutputSynapses().get(0).setWeight(-0.5);
		ff.getHiddenlayers().get(0).get(1).getOutputSynapses().get(0).setWeight(0.6);
		
		double[] a = {1, 1};
		double[] b = {1, 0};
		double[] c = {0, 1};
		double[] d = {0, 0};
		
		double[] oa = {0};
		double[] ob = {1};
		double[] oc = {1};
		double[] od = {0};
		
		double[][] inputdata = new double[][]{a, b, c, d};
		double[][] outputdata = new double[][]{oa, ob, oc, od};
		
		bp.calculateActivations(inputdata[0]);
		
		System.out.println("out_h1 : " + ff.getHiddenlayers().get(0).get(0).getActivation());
		System.out.println("out_h2 : " + ff.getHiddenlayers().get(0).get(1).getActivation());
		System.out.println("out_o1 : " + ff.getOutputlayer().get(0).getActivation());
		
		bp.calculateNeuronAndWeightDiffs(outputdata[0]);
		
		System.out.println("neuronDiff : " + ff.getOutputlayer().get(0).getNeuronDiff());
		System.out.println("derivée : " + ff.getOutputlayer().get(0).getActivationFunction().applyDerivative(ff.getOutputlayer().get(0).getActivation()));
		
		System.out.println("delta w5 : " + ff.getHiddenlayers().get(0).get(0).getOutputSynapses().get(0).getWeightDiff());
		System.out.println("delta w6 : " + ff.getHiddenlayers().get(0).get(1).getOutputSynapses().get(0).getWeightDiff());
		System.out.println("delte w1 : " + ff.getInputlayer().get(0).getOutputSynapses().get(0).getWeightDiff());
		System.out.println("delte w2 : " + ff.getInputlayer().get(0).getOutputSynapses().get(1).getWeightDiff());
		System.out.println("delte w3 : " + ff.getInputlayer().get(1).getOutputSynapses().get(0).getWeightDiff());
		System.out.println("delte w4 : " + ff.getInputlayer().get(1).getOutputSynapses().get(1).getWeightDiff());
	}

}
