package sorting;

import java.util.ArrayList;
import java.util.Iterator;

import model.Aircraft;
import model.ImportAircrafts;
import model.Solution;

public class Testing {
	//private PopulationHeuristic ph;
	private ImportAircrafts ia;
	private ArrayList<Aircraft> list; 
	
	public Testing(String filename){
		//ph = new PopulationHeuristic();
		ia = new ImportAircrafts(filename);
		list = ia.getAircraftList();
	}
	
	public ArrayList<Solution> createSolutions(){
		ArrayList<Solution> result = new ArrayList<Solution>();
		
		Aircraft temp;
		
		ArrayList<Aircraft> alist;
		
		int number, number_temp;
		
		for(int i=0; i<9; i++){
			alist = new ArrayList<Aircraft>();
			Iterator<Aircraft> it  = list.iterator();
			Solution sol = new Solution();
			
			switch(i){
				case 0:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number = temp.getEarliestLandindgTime();
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);	
					break;
				case 1:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number = temp.getTargetLandingTime();
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);	
					break;
				case 2:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number = temp.getLatestLandingTime();
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);	
					break;
				case 3:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number_temp = temp.getTargetLandingTime()-temp.getEarliestLandindgTime();
						number = temp.getEarliestLandindgTime()+(number_temp/2);
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);	
					break;
				case 4:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number_temp = temp.getLatestLandingTime()-temp.getTargetLandingTime(); 
						number = temp.getTargetLandingTime()+(number_temp/2);
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);
					break;
				case 5:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number_temp = temp.getTargetLandingTime()-temp.getEarliestLandindgTime();
						number = temp.getEarliestLandindgTime()+(number_temp/4);
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);
					break;
				case 6:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number_temp = temp.getTargetLandingTime()-temp.getEarliestLandindgTime();
						number = temp.getTargetLandingTime()-(number_temp/4);
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);	
					break;
				case 7:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number_temp = temp.getLatestLandingTime()-temp.getTargetLandingTime();
						number = temp.getTargetLandingTime()+(number_temp/4);
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);
					break;
				case 8:
					while(it.hasNext()){
						temp = Aircraft.createNewAircraft(it.next());
						number_temp = temp.getLatestLandingTime()-temp.getTargetLandingTime();
						number = temp.getLatestLandingTime()-(number_temp/4);
						temp.setScheduledLandingTime(number);
						alist.add(temp);
					}
					sol.setList(alist);
					result.add(sol);	
					break;
			}
		}		
		
		return result;
	}
	
	
	public static void main(String args[]){
		Testing t = new Testing("/Users/ulakbim/Downloads/Aircraftlanding/data.txt");
		ArrayList<Solution> sol = t.createSolutions();
		Evaluation eval = new Evaluation();
		Iterator<Solution> it = sol.iterator();
		while(it.hasNext()){
			Solution temp = it.next();
			temp.setFitness(eval.fintnessLinear(temp));
			temp.setUnfitness(eval.unfitness(temp));
			System.out.println("Fitness "+temp.getFitness());
			System.out.println("unFitness "+temp.getUnfitness());
			System.out.println();
		}
		
		PopulationHeuristic ph = new PopulationHeuristic(sol,"target");
		Solution child = ph.createChild();
		System.out.println("Child "+child.getFitness());
		ph.populationReplacement(child);
	}
}
