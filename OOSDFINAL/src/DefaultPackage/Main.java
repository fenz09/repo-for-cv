
package DefaultPackage;

public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        // Add some orders
        restaurant.addOrder(new OrderItem("Burger", 1));
        restaurant.addOrder(new OrderItem("Pizza", 2));
        restaurant.addOrder(new OrderItem("Pasta", 3));

        Chef chef = new Chef(restaurant);
        Waiter waiter = new Waiter(restaurant, chef);

        waiter.start(); // Start the waiter thread
    }
}
