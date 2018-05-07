package sorting;

import java.util.ArrayList;
import java.util.Iterator;

import model.Aircraft;
import model.ImportAircrafts;

public class Management {
	private static ArrayList<Aircraft> totalList;
	private ArrayList<Aircraft> inRangeList;
	
	public Management(String filename){
		totalList = new ImportAircrafts(filename).getAircraftList();
	}
	
	/**
	 * createInRangeList creates a list of which aircrafts that are in range 
	 * of the control tower at a given time.
	 * @param totalList, the list of all aircrafts landing at the airport
	 * @return an updated list over the aircrafts in range of the control tower
	 */
	public ArrayList<Aircraft> createInRangeList(ArrayList<Aircraft> totalList, int time){
		inRangeList = new ArrayList<Aircraft>();
		
		Iterator<Aircraft> it = totalList.iterator();
		
		//Finds the aircrafts that has not landet
		//and are inside the range of  the control
		//tower (300 nautical miles)
		while(it.hasNext()){
			Aircraft temp_aircraft = it.next();
			if (temp_aircraft.getTargetLandingTime()>=time
					&& temp_aircraft.getEarliestLandindgTime()<=time+675)
				inRangeList.add(temp_aircraft);
		}
		return inRangeList;
	}
	
	
	public static void main(String[] args){
		// Testing prupose
		Management mgr = new Management("data.txt");
		ArrayList<Aircraft> air = mgr.createInRangeList(totalList,0);
		FCFS fcfs = new FCFS();
		air = fcfs.sort(air);
	}
}

