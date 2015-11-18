
public class testXOR {

	public static void main(String[] args) throws InvalidNetworkConstruction {
		// TODO Auto-generated method stub
		int[] constructorTab =new int[]{2,2,1};
		FeedForward ff = new FeedForward(constructorTab);
		System.out.println(ff.getLayer(0));
		BackProp bp = new BackProp(ff);
		ff.setLearningAlgorithm(bp);
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
		
		System.out.println(ff.getHiddenlayers().get(0).get(0).getActivation());
		System.out.println(ff.getHiddenlayers().get(0).get(1).getActivation());
	}

}
