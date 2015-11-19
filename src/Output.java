public class Output {
	public double quadraticErrorTraining;
	public double percentageErrorTraining;
	public double quadraticErrorTest;
	public double percentageErrorTest;

	public Output(double quadraticErrorTraining,
			double percentageErrorTraining, double quadraticErrorTest,
			double percentageErrorTest) {
		this.quadraticErrorTraining = quadraticErrorTraining;
		this.percentageErrorTraining = percentageErrorTraining;
		this.quadraticErrorTest = quadraticErrorTest;
		this.percentageErrorTest = percentageErrorTest;
	}
}
