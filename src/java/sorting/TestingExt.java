package sorting;

import java.util.ArrayList;

import model.Aircraft;
import model.ImportAircraftExt;
import model.Solution;

public class TestingExt {
	private ImportAircraftExt ia;
	private ArrayList<Aircraft> list; 
	
	public TestingExt(String filename){
		//ph = new PopulationHeuristic();
		ia = new ImportAircraftExt(filename);
		list = ia.getAircraftList();
	}
	
	
	
	public static void main(String[] args){
		TestingExt te = new TestingExt("airland1.txt");
		Solution best = new Solution();
		Solution bestGip = new Solution();
		Solution temp = new Solution();
		int k=0, test=100;;
		
		while(k++<10){			
			// TODO Auto-generated method stub
			GenerateInitialPopulation gip = new GenerateInitialPopulation(100, te.list);
			PopulationHeuristic ph = new PopulationHeuristic(gip.getPopulation(),"target");
			
			int j =0;
	
			Solution sol;
			bestGip = gip.getBestSolution();
			
			if(bestGip.getFitness()<best.getFitness())
				best = bestGip;
			//System.out.println("Best initial fitness: "+best.getFitness());
			//System.out.println("best initial unfitness: "+best.getUnfitness());
			
			while(j<5000){	
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
		
		if(best.getUnfitness()!=0 && temp.getUnfitness()!=0){
			System.out.println("No feasable solutions");
		}
		else if(temp.getUnfitness()==0 && best.getUnfitness()!=0){
			System.out.println("Feasable solution: "+temp.getFitness());
			System.out.println(temp.toString());
		}
		else{
			System.out.println("Best solution: "+best.getFitness());
			System.out.println(best.toString());
		}
	}
}
