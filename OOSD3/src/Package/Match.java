
package Package;

public class Match {
	private String matchDateTime;
	private String homeTeam;
	private String awayTeam;
	private int homeScore;
	private int awayScore;
	private int ticketsSold;

	public Match(String matchDateTime, String homeTeam, String awayTeam, int homeScore, int awayScore,
			int ticketsSold) {
		if (homeTeam.equalsIgnoreCase(awayTeam)) {
			throw new IllegalArgumentException("Home and away teams cannot be the same.");
		}
		this.matchDateTime = matchDateTime;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.ticketsSold = ticketsSold;
	}

	public String getMatchDateTime() {
		return matchDateTime;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public int getTicketsSold() {
		return ticketsSold;
	}

	public String toString() {
		return String.format("%s - %s vs %s, Score: %d-%d, Tickets Sold: %d", matchDateTime, homeTeam, awayTeam,
				homeScore, awayScore, ticketsSold);
	}
}
