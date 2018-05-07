package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ImportAircraftExt {
	private ArrayList<Aircraft> aircraftList;
	private int freeze, planes;
	
	public ImportAircraftExt(String filename){
		aircraftList = extractAircrafts(readFile(filename));
	}
	
	public ArrayList readFile(String filename){
		ArrayList<String> incomingAircrafts = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String temp = br.readLine();
			while (temp != null){
				incomingAircrafts.add(temp);
				temp = br.readLine();
			}
			br.close();
			fr.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return incomingAircrafts;
	}
	
	public ArrayList<Aircraft> extractAircrafts(ArrayList incomingAircrafts){
		aircraftList = new ArrayList<Aircraft>();
		int number=0, elt=0, tlt=0, llt=0, app=0;
		double penB, penA;
		
		StringTokenizer temp;
		freeze=0;
		planes=0;
		
		
		
		Iterator it = incomingAircrafts.iterator();
		if(it.hasNext()){
			temp = new StringTokenizer((String)it.next());
			planes = new Integer(temp.nextToken()).intValue();
			freeze = new Integer(temp.nextToken()).intValue();			
		}
		
		
		while(it.hasNext()){
			temp = new StringTokenizer((String)it.next());
			int[] seperation = new int[planes];
			AircraftExt aircraft;
			
			app = new Integer(temp.nextToken()).intValue();
			elt = new Integer(temp.nextToken()).intValue();
			tlt = new Integer(temp.nextToken()).intValue();
			llt = new Integer(temp.nextToken()).intValue();
			penB = new Double(temp.nextToken()).doubleValue();
			penA = new Double(temp.nextToken()).doubleValue();
			aircraft = new AircraftExt(number, app, elt, tlt, llt, penB, penA);
			number++;

			//extract the seperation constraints
			if(it.hasNext()){
				temp = new StringTokenizer((String)it.next());
				int i=0;
				while(temp.hasMoreTokens()){
					seperation[i++] = new Integer(temp.nextToken()).intValue();
				}
				aircraft.setSeperation(seperation);
			}
			aircraftList.add(aircraft);
		}
		//Sort the aircraftList using bubble sort
		Boolean swapped = true;
		while(swapped==true){
			swapped=false;
			for(int i=0; i<aircraftList.size()-1;i++){
				Aircraft a = aircraftList.get(i);
				Aircraft b = aircraftList.get(i+1);
				if(a.getEarliestLandindgTime() > b.getEarliestLandindgTime()){
					aircraftList.set(i, b);
					aircraftList.set(i+1, a);
					swapped=true;
				}
			}
		}
			
		return aircraftList;
	}

	public ArrayList<Aircraft> getAircraftList() {
		return aircraftList;
	}

	public int getFreeze() {
		return freeze;
	}

	public int getPlanes() {
		return planes;
	}
}
