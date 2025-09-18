
package DefaultPackage;

import javax.swing.*; // For Swing components
import java.awt.*; // For layout managers and AWT components
import java.awt.event.*; // For event handling

public class RestaurantGUI {
    private Restaurant restaurant;
    private Chef chef;
    private Waiter waiter;

    public RestaurantGUI() {
        restaurant = new Restaurant();
        chef = new Chef(restaurant);
        waiter = new Waiter(restaurant, chef);

        // Create the main frame
        JFrame frame = new JFrame("Restaurant Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Input panel for adding orders
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel dishLabel = new JLabel("Dish Name:");
        JTextField dishField = new JTextField();
        JLabel tableLabel = new JLabel("Table Number:");
        JTextField tableField = new JTextField();
        JButton addButton = new JButton("Add Order");

        inputPanel.add(dishLabel);
        inputPanel.add(dishField);
        inputPanel.add(tableLabel);
        inputPanel.add(tableField);
        inputPanel.add(new JLabel()); // Empty cell
        inputPanel.add(addButton);

        // Output area for displaying prepared orders
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add order button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dishName = dishField.getText();
                String tableNumberText = tableField.getText();

                if (dishName.isEmpty() || tableNumberText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both dish name and table number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int tableNumber = Integer.parseInt(tableNumberText);
                    OrderItem order = new OrderItem(dishName, tableNumber);
                    restaurant.addOrder(order);
                    outputArea.append("Order added: " + dishName + " for table " + tableNumber + "\n");
                    dishField.setText("");
                    tableField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Table number must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Start processing orders
        JButton processButton = new JButton("Process Orders");
        frame.add(processButton, BorderLayout.SOUTH);

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waiter.start();
                processButton.setEnabled(false); // Disable button after starting processing
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new RestaurantGUI();
    }
}
