package sorting;

import model.Solution;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GenerateInitialPopulation gip = new GenerateInitialPopulation(100, "/Users/ulakbim/Downloads/Aircraftlanding/data.txt");
		PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(),"target");

		int j =0;

		Solution sol;
			
		while(j<1000){	
			sol = ph.createChild();
			ph.populationReplacement(sol);
			if(sol.getUnfitness()==0){
				break;
			}
			j++;
		}	

		System.out.println(j);
	}
}
