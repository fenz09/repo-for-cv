
package DefaultPackage;

public class OrderItem {
    private String dishName;
    private int tableNumber;

    public OrderItem(String dishName, int tableNumber) {
        this.dishName = dishName;
        this.tableNumber = tableNumber;
    }

    public String getDishName() {
        return dishName;
    }

    public int getTableNumber() {
        return tableNumber;
    }
}
