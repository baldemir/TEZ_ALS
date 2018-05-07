package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Solution {
	private ArrayList<Aircraft> list;
	private int unfitness;
	private int fitness;
	
	public Solution(){
		unfitness = Integer.MAX_VALUE;
		//fitness = Integer.MIN_VALUE;
		fitness = Integer.MAX_VALUE;
		list = new ArrayList<Aircraft>();
	}
	
	public void sort(String objective){
		//	Sort the aircraftList using bubble sort
		Boolean swapped = true;
		while(swapped==true){
			swapped=false;
			for(int i=0; i<list.size()-1;i++){
				Aircraft a = list.get(i);
				Aircraft b = list.get(i+1);
				if(objective.equals("earliest")){
					if(a.getEarliestLandindgTime() > b.getEarliestLandindgTime()){
						list.set(i, b);
						list.set(i+1, a);
						swapped=true;
					}
				}else if(objective.equals("target")){
					if(a.getTargetLandingTime() > b.getTargetLandingTime()){
						list.set(i, b);
						list.set(i+1, a);
						swapped=true;
					}	
					else if(objective.equals("scheduled")){
						if(a.getScheduledLandingTime() > b.getScheduledLandingTime()){
							list.set(i, b);
							list.set(i+1, a);
							swapped=true;
						}	
					}
				}
			}
		}
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public ArrayList<Aircraft> getList() {
		return list;
	}

	public void setList(ArrayList<Aircraft> list) {
		this.list = list;
	}

	public int getUnfitness() {
		return unfitness;
	}

	public void setUnfitness(int unfitness) {
		this.unfitness = unfitness;
	}
	
	public String toString(){
		String temp = "";
		
		Iterator<Aircraft> it = list.iterator();
		while(it.hasNext()){
			temp += it.next().toString();
		}
		
		return temp;
	}
}
