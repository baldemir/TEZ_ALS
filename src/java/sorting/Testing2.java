package sorting;

import model.Solution;

public class Testing2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Solution best = new Solution();
		int k=0;
		int test = 100;
		
		while(test!=0 && k++<300){			
			// TODO Auto-generated method stub
			GenerateInitialPopulation gip = new GenerateInitialPopulation(100, "airland1_test2.txt");
			PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(),"target");
			
			//System.out.println("Initial fitness: "+ph.totalFitness(gip.getPopulation())/100);
			//System.out.println("Initial unfitness: "+ph.totalUnfitness(gip.getPopulation())/100);
			int j =0;
	
			Solution sol;
			best = gip.getBestSolution();
			//System.out.println("Best initial fitness: "+best.getFitness());
			//System.out.println("best initial unfitness: "+best.getUnfitness());
			
			while(j<20000){	
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
			test = best.getUnfitness();
		}

		//System.out.println("Final fitness: "+ph.totalFitness(ph.getPopulation())/100);
		//System.out.println("Final unfitness: "+ph.totalUnfitness(ph.getPopulation())/100);
		//System.out.println("j: "+j);
		
		if(best.getUnfitness()!=0)
			System.out.println("No feasable solutions");
		else{
			System.out.println("Best solution: "+best.getFitness());
			System.out.println(best.toString());
		}
	}

}
