
package DefaultPackage;

public class Waiter extends Thread {
    private final Restaurant restaurant;
    private final Chef chef;

    public Waiter(Restaurant restaurant, Chef chef) {
        this.restaurant = restaurant;
        this.chef = chef;
    }

    @Override
    public void run() {
        while (restaurant.hasOrders()) {
            OrderItem order = restaurant.getNextOrder();
            if (order == null) break;

            System.out.println("Waiter picked up order: " + order.getDishName() + " for table " + order.getTableNumber());
            chef.start(); // Start chef thread to prepare the meal

            synchronized (chef) {
                try {
                    chef.wait(); // Wait for the chef to notify when the meal is ready
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Waiter served: " + order.getDishName() + " to table " + order.getTableNumber());
        }
    }
}
