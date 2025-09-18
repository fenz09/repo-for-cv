
// Publisher class to print the value from the Repository
public class Publisher implements Runnable {
	private Repository repository;

	public Publisher(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("Current value: " + repository.getValue()); // Print the value from the repository
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
