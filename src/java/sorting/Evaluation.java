package sorting;

import java.util.ArrayList;
import java.util.Iterator;

import model.Aircraft;
import model.AircraftExt;
import model.SeperationConstraints;
import model.Solution;

public class Evaluation {
	private SeperationConstraints sep;
	
	public Evaluation(){
		sep = new SeperationConstraints();
	}
	
	//Calculate the fitness, udsing a linear model-function
	public int fintnessLinear(Solution sol){
		int sum=0;
		Iterator<Aircraft> it = sol.getList().iterator();
		Aircraft air;
		
		while(it.hasNext()){
			air = it.next();
			sum += air.getTargetLandingTime()-air.getScheduledLandingTime();
		}		
		return sum;
	}
	
	//Calculate the fitness with target landing time as objective
	public int fintnessLinearExt(Solution sol){
		int sum=0, temp;
		Iterator<Aircraft> it = sol.getList().iterator();
		AircraftExt air;
//		while(it.hasNext()){
//			air = (AircraftExt)it.next();
//			temp = air.getScheduledLandingTime()-air.getTargetLandingTime();
//			if(temp<0)
//				sum += Math.abs(temp*air.getPenB());
//			else
//				sum += temp*air.getPenA();
                        
//		}
                sum = sol.getList().get(sol.getList().size()-1).getScheduledLandingTime();
                
		return sum;
	}
	
	//Calculate the fitness, udsing a squared model-function
	public int fintnessNonLinear(Solution sol){
		int sum=0;
		Iterator<Aircraft> it = sol.getList().iterator();
		Aircraft air;
		int temp;
		
		while(it.hasNext()){
			air = it.next();
			temp = air.getScheduledLandingTime()-air.getTargetLandingTime();
			if(temp>=0)
				sum -= (temp*temp);
			else
				sum += (temp*temp);
		}		
		return sum;
	}
	
	//Calculate the unfitness value
	public int unfitness(Solution sol){
		int sum = 0;
		int i = 0;
		int s,x2,x1;
		Aircraft air1, air2;
		ArrayList<Aircraft> list = sol.getList();
		
		while(i<list.size()-1){
			air1 = list.get(i);
			air2 = list.get(i+1);
			
			s = sep.getSeperation(air1.getType(), air2.getType());
			x1 = air1.getScheduledLandingTime();
			x2 = air2.getScheduledLandingTime();
			
			sum += Math.max(0, s-(x2-x1));
			i++;
		}		
		return sum;
	}
	
	public int unfitnessExt(Solution sol){
		int sum = 0;
		int i = 1;
		int s,x2,x1;
		AircraftExt air1, air2;
		ArrayList<Aircraft> list = sol.getList();
		
		while(i<list.size()){
			air1 = (AircraftExt)list.get(i-1);
			air2 = (AircraftExt)list.get(i);
			
			s = air1.getSeperation(air2.getNumber());
			x1 = air1.getScheduledLandingTime();
			x2 = air2.getScheduledLandingTime();
			
			sum += Math.max(0, s-(x2-x1));
			i++;
		}		
		return sum;
	}
}
