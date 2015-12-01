import java.io.IOException;

public class TestMNIST {

	public static void main(String[] args) throws IOException,
			InvalidNetworkConstruction {
		
		int[] constructorTab = new int[] {784, 90, 10 };
		FeedForward ff = new FeedForward(constructorTab, 0.1, 1, 1000);
		
		//No need to reuse a constructor, it is already done when ff is constructed
		//BackProp bp = new BackProp(ff);
		LearningAlgorithm bp = ff.getLearningAlgorithm();
		
		ff.linkNetwork();
		
		MnistManager trainingDataManager = new MnistManager(
				"C:/Users/Léo/workspace/dl/MNISTData/train-images.idx3-ubyte",
				"C:/Users/Léo/workspace/dl/MNISTData/train-labels.idx1-ubyte");
		
		double[][] inputsDataTraining = new double[60000][28*28];
		double[][] outputsDataTraining = new double[60000][10];
		
		for (int i=0; i<60000; i++) {
			trainingDataManager.setCurrent(i+1);
			int[][] a = trainingDataManager.readImage();
			for (int j = 0; j <= 27; j++) {
				for (int k = 0; k <= 27; k++) {
					inputsDataTraining[i][28*j + k] = a[j][k];
				}
			}
			int b = trainingDataManager.readLabel();
			outputsDataTraining[i][b] = 1;
		}
		
		//files 5 times
		
		double[][] inputsDataTraining5 = new double[300000][28*28];
		double[][] outputsDataTraining5 = new double[300000][10];

		for(int i=0; i<4; i++){
			for(int j=0; j<60000; j++){
				inputsDataTraining5[i*60000+j]=inputsDataTraining[j];
				outputsDataTraining5[i*60000+j]=outputsDataTraining[j];
			}
		}
		
		MnistManager testDataManager = new MnistManager(
				"C:/Users/Léo/workspace/dl/MNISTData/t10k-images.idx3-ubyte",
				"C:/Users/Léo/workspace/dl/MNISTData/t10k-labels.idx1-ubyte");
		
		double[][] inputsDataTest = new double[10000][28*28];
		double[][] outputsDataTest = new double[10000][10];
		
		for (int i=0; i<10000; i++) {
			testDataManager.setCurrent(i+1);
			int[][] a = testDataManager.readImage();
			for (int j = 0; j <= 27; j++) {
				for (int k = 0; k <= 27; k++) {
					inputsDataTest[i][28*j + k] = a[j][k];
				}
			}
			int b = testDataManager.readLabel();
			outputsDataTest[i][b] = 1;
		}
		
		//files 5 times
		
		double[][] inputsDataTest5 = new double[300000][28*28];
		double[][] outputsDataTest5 = new double[300000][10];

		for(int i=0; i<4; i++){
			for(int j=0; j<60000; j++){
				inputsDataTest5[i*60000+j]=inputsDataTest[j];
				outputsDataTest5[i*60000+j]=outputsDataTest[j];
			}
		}
		
		bp.globaltraining(inputsDataTraining5, outputsDataTraining5, inputsDataTest5, outputsDataTest5);
		
		ToCsv csv = new ToCsv(bp);
		
		csv.writeCsvFile("C:/Users/Léo/Desktop/");

	}

}
