
// Main class to create instances of Repository, Counter, and Publisher and start the threads
public class Main {
	public static void main(String[] args) {
		Repository repository = new Repository();
		Counter counter = new Counter(repository);
		Publisher publisher = new Publisher(repository);

		Thread counterThread = new Thread(counter);
		Thread publisherThread = new Thread(publisher);

		counterThread.start();
		publisherThread.start();
	}
}
