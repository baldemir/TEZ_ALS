package sorting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import model.Aircraft;
import model.AircraftExt;
import model.ImportAircrafts;
import model.Solution;

public class GenerateInitialPopulation {
	private ImportAircrafts ia;
	private ArrayList<Aircraft> airList;
	private ArrayList<Solution> population;
	private Evaluation eval;
	private Solution bestSolution;
	
	public GenerateInitialPopulation(int number, String filename){
		ia = new ImportAircrafts(filename);
		population = new ArrayList<Solution>();
		eval = new Evaluation();
		bestSolution = new Solution();
		airList = ia.getAircraftList();
		createRandomPopulation(number);
	}
	
	public GenerateInitialPopulation(int number, ArrayList<Aircraft> airList){
		population = new ArrayList<Solution>();
		eval = new Evaluation();
		bestSolution = new Solution();
		this.airList = airList;
		createRandomPopulationExt(number);
	}
	
	public void createRandomPopulation(int number){
		Solution sol;
		Aircraft temp, new_aircraft;
		Iterator<Aircraft> it;
		int window, landing;
		Random rand = new Random();
		
		for(int i=0; i<number; i++){
			it = airList.iterator();
			sol = new Solution();
			while(it.hasNext()){
				temp = it.next();
				window = temp.getLatestLandingTime()-temp.getEarliestLandindgTime();
				landing = temp.getEarliestLandindgTime()+rand.nextInt(window);
				if(temp.getApperanceTime()!=-1)
					new_aircraft = Aircraft.createNewAircraftAPP(temp);
				else
					new_aircraft = Aircraft.createNewAircraft(temp);
					
				new_aircraft.setScheduledLandingTime(landing);
				sol.getList().add(new_aircraft);
			}
			sol.setFitness(eval.fintnessNonLinear(sol));
			sol.setUnfitness(eval.unfitness(sol));
			//Finds the initial best solution
			if(sol.getUnfitness()==0 && sol.getFitness()>bestSolution.getFitness())
				bestSolution = sol;
			population.add(sol);
		}		
	}

	public void createRandomPopulationExt(int number){
		Solution sol;
		AircraftExt temp, new_aircraft;
		Iterator<Aircraft> it;
		int window, landing;
		Random rand = new Random();
		
		for(int i=0; i<number; i++){
			it = airList.iterator();
			sol = new Solution();
			while(it.hasNext()){
				temp = (AircraftExt)it.next();
				window = temp.getLatestLandingTime()-temp.getEarliestLandindgTime();
				landing = temp.getEarliestLandindgTime()+rand.nextInt(window);
				
				new_aircraft = AircraftExt.createNewAircraftExt(temp);
					
				new_aircraft.setScheduledLandingTime(landing);
				sol.getList().add(new_aircraft);
			}
			sol.setFitness(eval.fintnessLinearExt(sol));
			sol.setUnfitness(eval.unfitnessExt(sol));
			//Finds the initial best solution
			if(sol.getUnfitness()==0 && Math.abs(sol.getFitness())<Math.abs(bestSolution.getFitness()))
				bestSolution = sol;
			population.add(sol);
		}		
	}
	
	public ArrayList<Solution> getPopulation() {
		return population;
	}

	public Solution getBestSolution() {
		return bestSolution;
	}
	
	
}
