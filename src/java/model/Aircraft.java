package model;

public class Aircraft {
	private int number;
	private int apperanceTime;
	private int earliestLandindgTime;
	private int targetLandingTime;
	private int latestLandingTime;
	private int scheduledLandingTime;
	private int type;
	private boolean hasLanded;
	
	public Aircraft(int number, int elt, int tlt, int llt, int type){
		this.number = number;
		earliestLandindgTime = elt;
		targetLandingTime = tlt;
		latestLandingTime = llt;
		scheduledLandingTime = targetLandingTime;
		this.type = type;
		hasLanded = false;
		apperanceTime = -1;
	}
	
	public Aircraft(int number, int app, int elt, int tlt, int llt, int type){
		this.number = number;
		apperanceTime = app;
		earliestLandindgTime = elt;
		targetLandingTime = tlt;
		latestLandingTime = llt;
		scheduledLandingTime = targetLandingTime;
		this.type = type;
	}

	public int getNumber() {
		return number;
	}
	
	public int getEarliestLandindgTime() {
		return earliestLandindgTime;
	}

	public void setEarliestLandindgTime(int earliestLandindgTime) {
		this.earliestLandindgTime = earliestLandindgTime;
	}

	public int getLatestLandingTime() {
		return latestLandingTime;
	}

	public int getTargetLandingTime() {
		return targetLandingTime;
	}

	public int getType() {
		return type;
	}

	public int getScheduledLandingTime() {
		return scheduledLandingTime;
	}

	public void setScheduledLandingTime(int scheduledLandingTime) {
		this.scheduledLandingTime = scheduledLandingTime;
	}
	
	public static Aircraft createNewAircraft(Aircraft a){
		int number = a.getNumber();
		int elt = a.getEarliestLandindgTime();
		int tlt = a.getTargetLandingTime();
		int llt = a.getLatestLandingTime();
		int type = a.getType();
		
		Aircraft air = new Aircraft(number, elt, tlt, llt, type);
		
		return air;
	}
	
	public static Aircraft createNewAircraftAPP(Aircraft a){
		int number = a.getNumber();
		int app = a.getApperanceTime();
		int elt = a.getEarliestLandindgTime();
		int tlt = a.getTargetLandingTime();
		int llt = a.getLatestLandingTime();
		int type = a.getType();
		
		Aircraft air = new Aircraft(number, app,elt, tlt, llt, type);
		
		return air;
	}
	
	public String toString(){
		String temp= number + ": x: "+ scheduledLandingTime;
		temp += ": elt: " + earliestLandindgTime;
		//temp += " tlt: "+ targetLandingTime;
		//temp += " llt: "+ latestLandingTime;
		temp += " app: "+ apperanceTime +"\n";
		return temp;
	}

	public int getApperanceTime() {
		return apperanceTime;
	}

	public void setApperanceTime(int apperanceTime) {
		this.apperanceTime = apperanceTime;
	}

	public boolean isHasLanded() {
		return hasLanded;
	}

	public void setHasLanded(boolean hasLanded) {
		this.hasLanded = hasLanded;
	}

	public void setLatestLandingTime(int latestLandingTime) {
		this.latestLandingTime = latestLandingTime;
	}
	
}
