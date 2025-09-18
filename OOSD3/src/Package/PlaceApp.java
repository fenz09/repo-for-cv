
package Package;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class PlaceApp {

	private JFrame frame;
	private JTextField nameField, addressField, capacityField;
	private JFormattedTextField phoneField;
	private JTable table;
	private DefaultTableModel tableModel;
	private ArrayList<Place> places;
	private JLabel totalScoresLabel;

	public PlaceApp() {
		places = new ArrayList<>();
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Place Management");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		frame.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel placeInfoPanel = new JPanel(new BorderLayout());
		placeInfoPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		JPanel inputPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel nameLabel = new JLabel("Name:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		inputPanel.add(nameLabel, gbc);

		nameField = new JTextField(20);
		gbc.gridx = 1;
		inputPanel.add(nameField, gbc);

		JLabel addressLabel = new JLabel("Address:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		inputPanel.add(addressLabel, gbc);

		addressField = new JTextField(20);
		gbc.gridx = 1;
		inputPanel.add(addressField, gbc);

		JLabel phoneLabel = new JLabel("Phone:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		inputPanel.add(phoneLabel, gbc);

		phoneField = new JFormattedTextField();
		phoneField.setColumns(20);
		gbc.gridx = 1;
		inputPanel.add(phoneField, gbc);

		JLabel capacityLabel = new JLabel("Capacity:");
		gbc.gridx = 0;
		gbc.gridy = 3;
		inputPanel.add(capacityLabel, gbc);

		capacityField = new JTextField(20);
		gbc.gridx = 1;
		inputPanel.add(capacityField, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		JButton addButton = new JButton("Add Place");
		addButton.addActionListener(new AddButtonListener());
		buttonPanel.add(addButton);

		JButton updateButton = new JButton("Update Place");
		updateButton.addActionListener(new UpdateButtonListener());
		buttonPanel.add(updateButton);

		gbc.gridx = 1;
		gbc.gridy = 4;
		inputPanel.add(buttonPanel, gbc);

		placeInfoPanel.add(inputPanel, BorderLayout.NORTH);

		tableModel = new DefaultTableModel(new Object[] { "Name", "Address", "Phone", "Capacity" }, 0);
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(e -> populateFields());

		JScrollPane tableScrollPane = new JScrollPane(table);
		placeInfoPanel.add(tableScrollPane, BorderLayout.CENTER);

		tabbedPane.addTab("Place Information", placeInfoPanel);
		setupMatchTab(tabbedPane);
		setupStatisticsTab(tabbedPane);

		frame.add(tabbedPane, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private void setupMatchTab(JTabbedPane tabbedPane) {
		JPanel matchPanel = new JPanel(new BorderLayout());

		
		JPanel inputPanel = new JPanel(new GridLayout(6, 2));
		inputPanel.add(new JLabel("Date (dd/MM/yyyy HH:mm):"));
		JFormattedTextField dateField = new JFormattedTextField();
		inputPanel.add(dateField);

		inputPanel.add(new JLabel("Home Team:"));
		JComboBox<String> homeTeamBox = new JComboBox<>(
				new String[] { "Bohemians", "Cork City", "Shamrock Rovers", "Shelbourne", "Sligo Rovers" });
		inputPanel.add(homeTeamBox);

		inputPanel.add(new JLabel("Away Team:"));
		JComboBox<String> awayTeamBox = new JComboBox<>(
				new String[] { "Bohemians", "Cork City", "Shamrock Rovers", "Shelbourne", "Sligo Rovers" });
		inputPanel.add(awayTeamBox);

		inputPanel.add(new JLabel("Home Score:"));
		JTextField homeScoreField = new JTextField("0");
		inputPanel.add(homeScoreField);

		inputPanel.add(new JLabel("Away Score:"));
		JTextField awayScoreField = new JTextField("0");
		inputPanel.add(awayScoreField);

		inputPanel.add(new JLabel("Tickets Sold:"));
		JTextField ticketsSoldField = new JTextField("0");
		inputPanel.add(ticketsSoldField);

		matchPanel.add(inputPanel, BorderLayout.NORTH);

		// Center panel for table
		JTable matchTable = new JTable(
				new DefaultTableModel(new Object[] { "Date", "Home", "Away", "Score", "Tickets" }, 0));
		JScrollPane scrollPane = new JScrollPane(matchTable);
		matchPanel.add(scrollPane, BorderLayout.CENTER);

		// South panel for actions
		JPanel actionPanel = new JPanel(new FlowLayout());
		JButton addMatchButton = new JButton("Add Match");
		addMatchButton.addActionListener(e -> {
			try {
				String dateTime = dateField.getText();
				String homeTeam = homeTeamBox.getSelectedItem().toString();
				String awayTeam = awayTeamBox.getSelectedItem().toString();
				int homeScore = Integer.parseInt(homeScoreField.getText());
				int awayScore = Integer.parseInt(awayScoreField.getText());
				int ticketsSold = Integer.parseInt(ticketsSoldField.getText());

				int selectedPlaceIndex = table.getSelectedRow();
				if (selectedPlaceIndex == -1) {
					JOptionPane.showMessageDialog(frame, "No place selected!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Place selectedPlace = places.get(selectedPlaceIndex);
				if (selectedPlace == null) {
					JOptionPane.showMessageDialog(frame, "Selected place not found!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Match match = new Match(dateTime, homeTeam, awayTeam, homeScore, awayScore, ticketsSold);
				selectedPlace.addMatch(match);

				DefaultTableModel model = (DefaultTableModel) matchTable.getModel();
				model.addRow(new Object[] { dateTime, homeTeam, awayTeam, homeScore + "-" + awayScore, ticketsSold });
				updateTotalScores();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error adding match: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		actionPanel.add(addMatchButton);

		matchPanel.add(actionPanel, BorderLayout.SOUTH);

		tabbedPane.addTab("Match Information", matchPanel);
	}

	private void setupStatisticsTab(JTabbedPane tabbedPane) {
		JPanel statisticsPanel = new JPanel(new BorderLayout());

		totalScoresLabel = new JLabel("Total Scores: 0");
		statisticsPanel.add(totalScoresLabel, BorderLayout.NORTH);

		tabbedPane.addTab("Statistics", statisticsPanel);
	}

	private void updateTotalScores() {
		int selectedPlaceIndex = table.getSelectedRow();
		if (selectedPlaceIndex != -1) {
			Place selectedPlace = places.get(selectedPlaceIndex);
			int totalScores = selectedPlace.getMatches().stream()
					.mapToInt(match -> match.getHomeScore() + match.getAwayScore()).sum();
			totalScoresLabel.setText("Total Scores: " + totalScores);
		}
	}

	private void populateFields() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			nameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
			addressField.setText(tableModel.getValueAt(selectedRow, 1).toString());
			phoneField.setText(tableModel.getValueAt(selectedRow, 2).toString());
			capacityField.setText(tableModel.getValueAt(selectedRow, 3).toString());
			updateTotalScores();
		}
	}

	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = nameField.getText().trim();
			String address = addressField.getText().trim();
			String phone = phoneField.getText().trim();
			String capacityStr = capacityField.getText().trim();

			if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || capacityStr.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int capacity = Integer.parseInt(capacityStr);
				Place place = new Place(name, address, phone, capacity);
				places.add(place);
				tableModel.addRow(new Object[] { name, address, phone, capacity });
				clearFields();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Capacity must be an integer!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		private void clearFields() {
			nameField.setText("");
			addressField.setText("");
			phoneField.setValue(null);
			capacityField.setText("");
		}
	}

	private class UpdateButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(frame, "No place selected for update!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String name = nameField.getText().trim();
			String address = addressField.getText().trim();
			String phone = phoneField.getText().trim();
			String capacityStr = capacityField.getText().trim();

			if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || capacityStr.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int capacity = Integer.parseInt(capacityStr);
				Place place = places.get(selectedRow);
				place.setName(name);
				place.setAddress(address);
				place.setPhoneNumber(phone);
				place.setCapacity(capacity);

				tableModel.setValueAt(name, selectedRow, 0);
				tableModel.setValueAt(address, selectedRow, 1);
				tableModel.setValueAt(phone, selectedRow, 2);
				tableModel.setValueAt(capacity, selectedRow, 3);
				updateTotalScores();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Capacity must be an integer!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(PlaceApp::new);
	}
}
