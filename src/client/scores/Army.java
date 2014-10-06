package client.scores;

public class Army {

	private int unitsLost;
	private int unitsCaptured;
	private float armyValue;
	
	private static Army army;
	
	public static void init() {
		army = new Army();
	}
	
	public static Army get() {
		return army;
	}
	
	public Army() {
		unitsLost = 0;
		unitsCaptured = 0;
		armyValue = 0;
	}
	
	public void loseUnit() {
		unitsLost++;
	}
	
	public void captureUnit() {
		unitsCaptured++;
	}
	
	public void tallyArmyValue(int score) {
		armyValue = score;
	}
	
	public String getSummary() {
		float modifier = (unitsCaptured - unitsLost);
		if(modifier < 1)
			modifier = 1;
		else
			modifier = (modifier * 0.1f) + 1;
		
		return (modifier * armyValue) + ";" + unitsCaptured + ";" + unitsLost + ";" + armyValue;
	}
	
	public int getUnitsLost() {
		return unitsLost;
	}
	
	public int getUnitsCaptured() {
		return unitsCaptured;
	}
	
	public float getArmyValue() {
		return armyValue;
	}
	
}
