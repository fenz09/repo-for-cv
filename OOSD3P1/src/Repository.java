
// Repository class to store an integer value
public class Repository {
	private int value;
	private boolean newValue = false;

	// Synchronized method to get the value
	public synchronized int getValue() throws InterruptedException {
		while (!newValue) {
			wait(); // Wait until a new value is set
		}
		newValue = false;
		notifyAll(); // Notify other threads that the value has been read
		return value;
	}

	// Synchronized method to set the value
	public synchronized void setValue(int value) throws InterruptedException {
		while (newValue) {
			wait(); // Wait until the current value is read
		}
		this.value = value;
		newValue = true;
		notifyAll(); // Notify other threads that a new value is set
	}
}
