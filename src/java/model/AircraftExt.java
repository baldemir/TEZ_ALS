package model;

public class AircraftExt extends Aircraft{
	private double penB, penA;
	private int[] seperation;
	
	public AircraftExt(int number, int app, int elt, int tlt, int llt, double penB, double penA){
		super(number, app, elt, tlt, llt, -1);
		this.penB = penB;
		this.penA = penA;
		
	}

	public double getPenA() {
		return penA;
	}

	public double getPenB() {
		return penB;
	}
	
	public int getSeperation(int i){
		return seperation[i];
	}

	public int[] getSeperation() {
		return seperation;
	}

	public void setSeperation(int[] seperation) {
		this.seperation = seperation;
	}
	
	public static AircraftExt createNewAircraftExt(AircraftExt a){
		int number = a.getNumber();
		int app = a.getApperanceTime();
		int elt = a.getEarliestLandindgTime();
		int tlt = a.getTargetLandingTime();
		int llt = a.getLatestLandingTime();
		double penA = a.getPenA();
		double penB = a.getPenB();
		
		AircraftExt air = new AircraftExt(number, app,elt, tlt, llt, penB, penA);
		air.setSeperation(a.getSeperation());
		
		return air;
	}

}
