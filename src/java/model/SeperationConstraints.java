package model;

public class SeperationConstraints {
	private int[][] lookup= new int[5][5];
	
	public SeperationConstraints(){
		//Distances after a light or small aircraft
		for (int i=0;i<5;i++){
			lookup[0][i]=3;
			lookup[1][i]=3;
		}
		
		//Distances after a lower-medium aircraft
		lookup[2][0] = 6;
		lookup[2][1] = 5;
		lookup[2][2] = 3;
		lookup[2][3] = 3;
		lookup[2][4] = 3;
		
		//Distances after a upper-medium aircraft
		lookup[3][0] = 7;
		lookup[3][1] = 6;
		lookup[3][2] = 4;
		lookup[3][3] = 3;
		lookup[3][4] = 3;
		
		//Distances after a heavy aircraft
		lookup[4][0] = 8;
		lookup[4][1] = 7;
		lookup[4][2] = 6;
		lookup[4][3] = 5;
		lookup[4][4] = 4;
	}
	
	/**
	 * getSeperation returns the seperation constrain in 
	 * seconds when a aircraft-type
	 *  i is followed by aircraft-type j.
	 * 
	 * @param aircraft type i
	 * @param aircraft type j
	 * @return seperation constraints in seconds
	 */
	public int getSeperation(int i, int j){		
		int temp = lookup[i-1][j-1];
		return temp*3600/160;
		//return temp*60/160;
	}
}
