import java.io.IOException;

public class TestMNIST {

	public static void main(String[] args) throws IOException,
			InvalidNetworkConstruction {
		
		int[] constructorTab = new int[] {784, 90, 10 };
		FeedForward ff = new FeedForward(constructorTab);
		BackProp bp = new BackProp(ff);
		ff.setLearningAlgorithm(bp);
		ff.linkNetwork();
		
		MnistManager trainingDataManager = new MnistManager(
				"C:/Users/Léo/workspace/dl/MNISTData/train-images.idx3-ubyte",
				"C:/Users/Léo/workspace/dl/MNISTData/train-labels.idx1-ubyte");
		
		double[][] inputsDataTraining = new double[42000][28*28];
		double[][] outputsDataTraining = new double[42000][10];
		
		for (int i=0; i<42000; i++) {
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
		
		bp.globaltraining(inputsDataTraining, outputsDataTraining, inputsDataTest, outputsDataTest);
		
		ToCsv csv = new ToCsv(bp);
		
		csv.writeCsvFile("C:/Users/Léo/Desktop/");

	}

}
