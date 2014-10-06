package server.scores;

public class Scores {

	private float[] playerTotals = new float[4];
	private int[] captured = new int[4];
	private int[] lost = new int[4];
	private float[] value = new float[4];
	
	private static Scores scores;
	
	public static void init() {
		scores = new Scores();
	}
	
	public static Scores get() {
		return scores;
	}
	
	public Scores() {
		playerTotals = new float[]{0f, 0f, 0f, 0f};
		captured = new int[]{0, 0, 0, 0};
		lost = new int[]{0, 0, 0, 0};
		value = new float[]{0f, 0f, 0f, 0f};
	}
	
	public float[] getTotals() {
		return playerTotals;
	}
	
	public void addScore(float score, int captured, int lost, float value, int player) {
		this.playerTotals[player] = score;
		this.captured[player] = captured;
		this.lost[player] = lost;
		this.value[player] = value;
	}
	
	public String toString() {
		String data = playerTotals[0] + "," + captured[0] + " " + lost[0] + " " + value[0];
		for(int x = 1; x<playerTotals.length; x++)
			data += "-" + playerTotals[x] + "," + captured[x] + " " + lost[x] + " " + value[x];
		
		return data;
	}
	
}
