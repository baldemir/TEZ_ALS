package sorting;

import java.util.ArrayList;
import java.util.Iterator;

import model.Aircraft;
import model.AircraftExt;
import model.ImportAircraftExt;
import model.Solution;

public class Test3 {
	private ImportAircraftExt ia;
	private ArrayList<Aircraft> list; 
	private Solution best;
	private int populationSize, iterations;
	
	public Test3(String filename, int populationSize, int iterations){
		this.populationSize = populationSize;
		this.iterations = iterations;		
		best = new Solution();
		ia = new ImportAircraftExt(filename);
		list = ia.getAircraftList();
		firstIteration();
	}
	
	public void firstIteration(){
		Solution bestGip = new Solution();
		Solution temp = new Solution();		
		
		int k=0;
		while(k++<10){			
			GenerateInitialPopulation gip = new GenerateInitialPopulation(populationSize, list);
			PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(),"scheduled");
			
			int j =0;
	
			Solution sol;
			bestGip = gip.getBestSolution();
			
			if(bestGip.getFitness()<best.getFitness())
				best = bestGip;

			while(j<iterations){	
				sol = ph.createChild();
				ph.populationReplacementExt(sol);
				if(sol.getUnfitness()==0){
					temp = sol;
					if(sol.getFitness()<best.getFitness()){
						best = sol;
						j=0;
					}
				}
				j++;
			}	
		}
		if(best.getUnfitness()==0)
			tightening(best);
		else
			tightening(temp);
	}
	
	public void tightening(Solution sol){
		Iterator<Aircraft> it = sol.getList().iterator();
		
		while(it.hasNext()){
			AircraftExt air = (AircraftExt)it.next();
			if(air.getScheduledLandingTime()>air.getTargetLandingTime()){
				air.setLatestLandingTime(air.getScheduledLandingTime());
			}
			else if(air.getScheduledLandingTime()<air.getTargetLandingTime()){
				air.setEarliestLandindgTime(air.getScheduledLandingTime());
			}
		}
		iterate(sol);
	}
	
	public void iterate(Solution solution){
		Solution bestGip = new Solution();
		Solution temp = new Solution();	
		int k=0;
		
		while(k++<10){			
			GenerateInitialPopulation gip = new GenerateInitialPopulation(populationSize, solution.getList());
			PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(),"scheduled");
			
			int j =0;
	
			Solution sol;
			bestGip = gip.getBestSolution();
			
			if(bestGip.getFitness()<best.getFitness() && bestGip.getUnfitness()==0)
				best = bestGip;

			while(j<iterations){	
				sol = ph.createChild();
				ph.populationReplacementExt(sol);
				if(sol.getUnfitness()==0){
					temp = sol;
					if(sol.getFitness()<best.getFitness()){
						best = sol;
						j=0;
					}
				}
				j++;
			}	
		}
		if(best.getFitness()<solution.getFitness() && best.getUnfitness()==0){
			tightening(best);
		}
		else if(temp.getFitness()<solution.getFitness()){
			tightening(temp);
		}
		else{
			System.out.println("Best solution: "+best.getFitness());
			System.out.println(best.toString());
		}
	}
	
	
	public static void main(String[] args){
		Test3 te = new Test3("/Users/ulakbim/Downloads/Aircraftlanding/airland1.txt", 100, 5000);
	}
}