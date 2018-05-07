package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ImportAircrafts {
	private ArrayList<Aircraft> aircraftList;
	
	public ImportAircrafts(String filename){
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
		int number=0, elt=0, tlt=0, llt=0, type=0, app=0;
		
		Iterator it = incomingAircrafts.iterator();
		while(it.hasNext()){
			StringTokenizer temp = new StringTokenizer((String)it.next());
			Aircraft aircraft;
			if (temp.countTokens()==4){
				number++;
				elt = new Integer(temp.nextToken()).intValue();
				tlt = new Integer(temp.nextToken()).intValue();
				llt = new Integer(temp.nextToken()).intValue();
				type = new Integer(temp.nextToken()).intValue();
				aircraft = new Aircraft(number, elt, tlt, llt, type);
				aircraftList.add(aircraft);
			}
			else if (temp.countTokens()==5){
				number++;
				app = new Integer(temp.nextToken()).intValue();
				elt = new Integer(temp.nextToken()).intValue();
				tlt = new Integer(temp.nextToken()).intValue();
				llt = new Integer(temp.nextToken()).intValue();
				type = new Integer(temp.nextToken()).intValue();
				aircraft = new Aircraft(number, app, elt, tlt, llt, type);
				aircraftList.add(aircraft);
			}			
			
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
}
