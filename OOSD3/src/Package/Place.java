
package Package;

import java.util.ArrayList;

public class Place {
	private String name;
	private String address;
	private String phoneNumber;
	private int capacity;
	private ArrayList<Match> matches;

	public Place(String name, String address, String phoneNumber, int capacity) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.capacity = capacity;
		this.matches = new ArrayList<>();
	}

	public void addMatch(Match match) {
		if (match.getTicketsSold() > capacity) {
			throw new IllegalArgumentException("Tickets sold exceed stadium capacity.");
		}
		matches.add(match);
	}

	public ArrayList<Match> getMatches() {
		return matches;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
