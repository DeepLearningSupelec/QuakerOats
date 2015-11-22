import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToCsv {
	private LearningAlgorithm learningAlgorithm;

	public ToCsv(LearningAlgorithm learningalgorithm) {
		this.learningAlgorithm = learningalgorithm;
	}

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "quadraticErrorTraining,percentageErrorTraining,quadraticErrorTest,percentageErrorTest";

	public void writeCsvFile(String filePath) {
		FileWriter fileWriter = null;
		try {
			String title = "LRate-" + this.learningAlgorithm.learningRate
					+ "-ESize-" + this.learningAlgorithm.epochSize
					+ "-EBetweenStat-"
					+ this.learningAlgorithm.numberOfEpochBetweenEachMeasure;
			fileWriter = new FileWriter(filePath + title + ".csv");

			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new output object list to the CSV file
			for (Output output : this.learningAlgorithm.outputData) {
				fileWriter
						.append(String.valueOf(output.quadraticErrorTraining));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String
						.valueOf(output.percentageErrorTraining));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(output.quadraticErrorTest));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(output.percentageErrorTest));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out
						.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();

			}

		}

	}

}
