 package sorting;

import model.Solution;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Solution best = new Solution();
		Solution test = new Solution();
		
		GenerateInitialPopulation gip = new GenerateInitialPopulation(100, "/Users/ulakbim/Downloads/Aircraftlanding/data.txt");
		PopulationHeuristic ph;

		int j =0, k=0;
		
	
		Solution sol;
			
		while(k++<10){			
			ph = new PopulationHeuristic(gip.getPopulation(),"scheduled");

			j =0;	
			if(gip.getBestSolution().getUnfitness()==0)
				best = gip.getBestSolution();
			else
				best = new Solution();
			
			while(j<5000){	
				sol = ph.createChild();
				ph.populationReplacement(sol);
				if(sol.getUnfitness()==0){
					if(sol.getFitness()>best.getFitness()){
						best = sol;
						j=0;
					}
				}
				j++;
			}	
			if(best.getFitness()>test.getFitness())
				test = best;
		}

		if(test.getUnfitness()!=0)
			System.out.println("No feasable solutions");
		else{
			System.out.println("Best solution: "+test.getFitness());
			//System.out.println("k: "+k);
			System.out.println(test.toString());
		}
	}
}