
package DefaultPackage;

import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final Queue<OrderItem> orders = new LinkedList<>();

    public synchronized void addOrder(OrderItem order) {
        orders.add(order);
        notifyAll(); // Notify waiter that a new order is available
    }

    public synchronized OrderItem getNextOrder() {
        while (orders.isEmpty()) {
            try {
                wait(); // Wait until an order is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return orders.poll();
    }

    public boolean hasOrders() {
        return !orders.isEmpty();
    }
}
