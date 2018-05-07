package sorting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import model.Aircraft;
import model.ImportAircrafts;
import model.SeperationConstraints;
import model.Solution;

public class Testing4 {

	/*
	//This method recalculates the appropriate part of the previous solution and
	// inserts the new scheduelled times into that solution.
	public static void recalculate(Aircraft air, Solution sol, ArrayList inAir){
	
	}
	*/
	public static void reorder(Aircraft air, Solution sol, int i){
		ArrayList<Aircraft> list, list2;
		SeperationConstraints sc = new SeperationConstraints();
		Aircraft air1, air2;		
		
		list = (ArrayList<Aircraft>)sol.getList().clone();
		list.remove(air);
		
		int gap = 0, index = 0, sep, first = 0;
		
		//Remove any aircrafts that are about to land at scheduled time
		//in the next 5 minuttes
		while(index<list.size()-1){
			if(list.get(index++).getScheduledLandingTime()>i+300){
				first=index;
				break;
			}
		}

		//Search for how many planes that needs to be rearranged
		//for a gap that is 1.5 times the maximum seperation
		//desitance between two aircraft is availiable.
		while(gap<270 && index<list.size()-1){
			air1 = list.get(index);
			air2 = list.get(index+1);
			sep = sc.getSeperation(air1.getType(), air2.getType());
			gap += (air2.getScheduledLandingTime()-air1.getScheduledLandingTime())-sep;
			index++;
		}
		
		//Create a new list with the aircrafts that need to be rescheduled
		list2 = new ArrayList<Aircraft>(list.subList(first, list.size()));
		
		//Find the aircraft that lands before the aircrafts that are
		//about to be rearranged, and calculate the new earliest
		//landing time for the delayed aircraft
		Aircraft airTemp = list.get(first-1);
		int dist = airTemp.getScheduledLandingTime()
					+sc.getSeperation(airTemp.getType(), air.getType());
		
		//Assuming that the aircraft was late from takeoff and therefore has
		//the same amount of fuel left as if it had landed on time
		int window = air.getLatestLandingTime()-air.getEarliestLandindgTime();
		
		air.setEarliestLandindgTime(Math.max(air.getApperanceTime()+300, dist));
		air.setLatestLandingTime(i+300+window);
		list2.add(air);
		
		Iterator it = list2.iterator();
		while(it.hasNext()){
			Aircraft temp = (Aircraft) it.next();
			int cons = sc.getSeperation(temp.getType(), list.get(index).getType());
			if(temp.getLatestLandingTime()>list.get(index).getScheduledLandingTime()-cons)
				temp.setLatestLandingTime(list.get(index).getScheduledLandingTime()-cons);
		}
		
		//Rearrange the aircraft after there earliest landing time
		Boolean swapped = true;
		while(swapped==true){
			swapped=false;
			for(int k=0; k<list2.size()-1;k++){
				Aircraft a = list2.get(k);
				Aircraft b = list2.get(k+1);
				if(a.getEarliestLandindgTime() > b.getEarliestLandindgTime()){
					list2.set(k, b);
					list2.set(k+1, a);
					swapped=true;
				}
			}
		}
		
		//Create a new solution
		GenerateInitialPopulation gip = new GenerateInitialPopulation(100, list2);
		PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(),"target");
		
		int j =0;
		
		Solution sol2;
		Solution best = gip.getBestSolution();
		
		while(j<20000){	
			sol2 = ph.createChild();
			ph.populationReplacement(sol2);
			if(sol2.getUnfitness()==0){
				if(sol.getFitness()>best.getFitness()){
					best = sol2;
					j=0;
				}
			}
			j++;
		}	
	
		if(best.getUnfitness()!=0)
			System.out.println("No feasable solutions");
		else{
			System.out.println("Best solution: "+best.getFitness());
			System.out.println(best);
		}
	}
	
	
	public static void recalculate(Aircraft air, Solution sol, ArrayList inAir){
		Evaluation eval = new Evaluation();
		SeperationConstraints sc = new SeperationConstraints();
		air.setScheduledLandingTime(air.getApperanceTime()+300);
		if(eval.unfitness(sol)==0){
			return;
		}
		else{
			int index = sol.getList().indexOf(air);
			Aircraft air2 = sol.getList().get(index+1);
			int sep = sc.getSeperation(air2.getType(), air.getType());
			air.setScheduledLandingTime(air2.getScheduledLandingTime()+sep);
			inAir.remove(air);
			inAir.add(index+1, air);
			Solution sol2 = new Solution();
			sol2.setList(inAir);
			if(eval.unfitness(sol2)==0){
				return;
			}
		}
		System.out.println("Not possible");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution sol = new Solution();
		Evaluation eval = new Evaluation();
		ImportAircrafts ia = new ImportAircrafts("data3_2.txt");
		
		ArrayList<Aircraft> al = ia.getAircraftList();
		ArrayList<Aircraft> landed = new ArrayList<Aircraft>();
		ArrayList<Aircraft> inAir = new ArrayList<Aircraft>();
		
		al.get(0).setScheduledLandingTime(400);
		al.get(1).setScheduledLandingTime(481);
		al.get(2).setScheduledLandingTime(582);
		al.get(3).setScheduledLandingTime(664);
		al.get(4).setScheduledLandingTime(795);
		al.get(5).setScheduledLandingTime(863);
		al.get(6).setScheduledLandingTime(931);
		al.get(7).setScheduledLandingTime(1170);
		al.get(8).setScheduledLandingTime(1246);
		al.get(9).setScheduledLandingTime(1384);
		al.get(10).setScheduledLandingTime(1900);
		
		
		sol.setList(al);
		//System.out.println(sol);
		//System.out.println(eval.fintnessNonLinear(sol));
		//System.out.println(eval.unfitness(sol));
		int i=0;
		inAir = (ArrayList<Aircraft>)sol.getList().clone();
		
		while(i++<2000){
			Iterator it = sol.getList().iterator();
			//Go through all the aircrafts that are scheduled
			while(it.hasNext()){
				Aircraft air = (Aircraft)it.next();
				//Test if the aircraft is appering on the radar
				if(air.getApperanceTime()==i){
					//Test if the aircraft is late
					if(air.getScheduledLandingTime()-300<i){
						reorder(air,sol, i);
						recalculate(air, sol, inAir);
						
					}
				}
				//Test if the plane was on time and has landet
				if(air.getScheduledLandingTime()==i&& air.getApperanceTime()<=i-300){
					air.setHasLanded(true);
					landed.add(air);
					inAir.remove(air);
				}
			}
			sol.setList((ArrayList<Aircraft>)inAir.clone());
		}
		//System.out.println("Still in air");
		//System.out.println(sol);
		
		//System.out.println("Landed");
		//Solution sol2 = new Solution();
		//sol2.setList(landed);
		//System.out.println(sol2);

	}

}

