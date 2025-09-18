
package DefaultPackage;

public class Chef extends Thread {
    private final Restaurant restaurant;

    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        while (true) {
            OrderItem order = restaurant.getNextOrder();
            if (order == null) break;

            System.out.println("Chef is preparing: " + order.getDishName() + " for table " + order.getTableNumber());
            try {
                Thread.sleep(2000); // Simulate meal preparation time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (this) {
                System.out.println("Chef has prepared: " + order.getDishName());
                notify(); // Notify waiter that the meal is ready
            }
        }
    }
}
 // This class represents the Chef in the restaurant.