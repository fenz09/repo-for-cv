
// Counter class to increment the value in the Repository
public class Counter implements Runnable {
	private Repository repository;

	public Counter(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void run() {
		int count = 0;
		try {
			while (true) {
				repository.setValue(count); // Set the value in the repository
				count++;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
