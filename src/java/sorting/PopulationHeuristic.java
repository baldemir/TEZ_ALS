package sorting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import model.Solution;

public class PopulationHeuristic {
	private ArrayList<Solution> population;
	private Random rand;
	private Evaluation eval;
	private String objective;
	
	public PopulationHeuristic(ArrayList<Solution> population, String objective){
		rand = new Random();
		eval = new Evaluation();
		this.population = population;
		this.objective = objective;
	}
	
	public Solution getRandomParent(){
		//returns a random parent
		return population.get(rand.nextInt(population.size()));
	}
	
	public Solution selectParent(Solution one, Solution two){
		//Return the parent with best fitness value
		if (one.getFitness()>two.getFitness())
			return one;
		else
			return two;			
	}
	
	public Solution createChild(){
		//Selects 4 different parants randomly
		Solution one = getRandomParent();
		Solution two = getRandomParent();
		Solution three = getRandomParent();
		Solution four = getRandomParent();
		
		//Binary tournament, to find the best 2 parents
		Solution parentOne = selectParent(one, two);
		Solution parentTwo = selectParent(three, four);
		
		//Crossover of the 2 parents
		Solution temp = uniformCrossover(parentOne, parentTwo);
		
		//Sort the child according to the objective
		temp.sort(objective);
		
		//Calculate the fitness value
		//int fitness = eval.fintnessLinear(temp);
		//int fitness = eval.fintnessNonLinear(temp);
		int fitness = eval.fintnessLinearExt(temp);
		
		//Calculate the unfitness value
		//int unfitness = eval.unfitness(temp);
		int unfitness = eval.unfitnessExt(temp);
		
		//Sets the fitness and unfitness value of the child
		temp.setFitness(fitness);
		temp.setUnfitness(unfitness);
		
		return temp;
	}
	
	public Solution uniformCrossover(Solution one, Solution two){
		int i = 0;
		Solution temp = new Solution();
		boolean index;
		
		while(i<one.getList().size()){
			index = rand.nextBoolean();
			if(index)
				temp.getList().add(one.getList().get(i));
			else
				temp.getList().add(two.getList().get(i));
			i++;
		}
		return temp;
	}
	
	public void populationReplacement(Solution child){
		ArrayList<Solution> g1 = new ArrayList<Solution>();
		ArrayList<Solution> g2 = new ArrayList<Solution>();
		ArrayList<Solution> g3 = new ArrayList<Solution>();
		ArrayList<Solution> g4 = new ArrayList<Solution>();
		
		Solution temp;
		
		Iterator<Solution> it = population.iterator();
		
		//Sorting the current solutions into groups
		while(it.hasNext()){
			temp = it.next();
			if(temp.getFitness()<child.getFitness()){
				if(temp.getUnfitness()<child.getUnfitness())
					g3.add(temp);
				else
					g1.add(temp);
			}
			else{
				if(temp.getUnfitness()<child.getUnfitness())
					g4.add(temp);
				else
					g2.add(temp);
			}
		}
		
		//remove a solution from the population randomly from the
		//worst group if possible, othervise form the second worst etc.
		if(g1.size()>0)
			population.remove(g1.get(rand.nextInt(g1.size())));
		else if(g2.size()>0)
			population.remove(g2.get(rand.nextInt(g2.size())));
		else if(g3.size()>0)
			population.remove(g3.get(rand.nextInt(g3.size())));
		else
			population.remove(g4.get(rand.nextInt(g4.size())));
		
		//add the child to the population
		population.add(child);
		
	}
	
	public void populationReplacementExt(Solution child){
		ArrayList<Solution> g1 = new ArrayList<Solution>();
		ArrayList<Solution> g2 = new ArrayList<Solution>();
		ArrayList<Solution> g3 = new ArrayList<Solution>();
		ArrayList<Solution> g4 = new ArrayList<Solution>();
		
		Solution temp;
		
		Iterator<Solution> it = population.iterator();
		
		//Sorting the current solutions into groups
		while(it.hasNext()){
			temp = it.next();
			if(Math.abs(temp.getFitness())>Math.abs(child.getFitness())){
				if(temp.getUnfitness()<child.getUnfitness())
					g3.add(temp);
				else
					g1.add(temp);
			}
			else{
				if(temp.getUnfitness()<child.getUnfitness())
					g4.add(temp);
				else
					g2.add(temp);
			}
		}
		
		//remove a solution from the population randomly from the
		//worst group if possible, othervise form the second worst etc.
		if(g1.size()>0)
			population.remove(g1.get(rand.nextInt(g1.size())));
		else if(g2.size()>0)
			population.remove(g2.get(rand.nextInt(g2.size())));
		else if(g3.size()>0)
			population.remove(g3.get(rand.nextInt(g3.size())));
		else
			population.remove(g4.get(rand.nextInt(g4.size())));
		
		//add the child to the population
		population.add(child);
		
	}
	
	public int totalFitness(ArrayList<Solution> pop){
		int sum = 0;
		Iterator<Solution> it = pop.iterator();
		while(it.hasNext()){
			sum += it.next().getFitness();
		}
		return sum;
	}
	
	public int totalUnfitness(ArrayList<Solution> pop){
		int sum = 0;
		Iterator<Solution> it = pop.iterator();
		while(it.hasNext()){
			sum += it.next().getUnfitness();
		}
		return sum;
	}


	public ArrayList<Solution> getPopulation() {
		return population;
	}
}
