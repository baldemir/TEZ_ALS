package sorting;

import java.util.ArrayList;

import model.Aircraft;
import model.SeperationConstraints;

public class FCFS {
	private SeperationConstraints sc;
	
	public FCFS(){
		sc = new SeperationConstraints();
	}
	
	public ArrayList<Aircraft> sort(ArrayList<Aircraft> list){
		ArrayList<Aircraft> temp_list = new ArrayList<Aircraft>();
		Aircraft temp_aircraft;
		
		while (list.size()>0){
			temp_aircraft = list.get(0);
			for (int i=1; i<list.size(); i++){
				if (list.get(i).getTargetLandingTime()<temp_aircraft.getTargetLandingTime())
					temp_aircraft = list.get(i);
			}
			temp_list.add(temp_aircraft);
			list.remove(temp_aircraft);
		}
		return makeFeasible(temp_list);
	}
	
	public ArrayList<Aircraft> makeFeasible(ArrayList<Aircraft> list){
		int temp_time;
		
		for (int i=0; i<list.size()-1; i++){
			temp_time = list.get(i).getScheduledLandingTime()+
						sc.getSeperation(list.get(i).getType(),
								list.get(i+1).getType());
			System.out.println(temp_time);
			
			if(list.get(i+1).getScheduledLandingTime()<temp_time){
				if(temp_time<list.get(i+1).getLatestLandingTime())
					list.get(i+1).setScheduledLandingTime(temp_time);
				else
					System.out.println("No feasable solution found");
			}
		}		
		return list;
	}
}

