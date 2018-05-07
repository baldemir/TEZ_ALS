package sorting;

import java.util.ArrayList;
import java.util.Iterator;

import model.Aircraft;
import model.ImportAircrafts;
import model.Solution;

public class Testing3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution sol = new Solution();
		Evaluation eval = new Evaluation();
		ImportAircrafts ia = new ImportAircrafts("data.txt");
		
		ArrayList<Aircraft> al = ia.getAircraftList();
		al.get(0).setScheduledLandingTime(0);
		al.get(1).setScheduledLandingTime(68);
		al.get(2).setScheduledLandingTime(158);
		al.get(3).setScheduledLandingTime(225);
		al.get(4).setScheduledLandingTime(338);
		
		sol.setList(al);

		System.out.println(eval.fintnessNonLinear(sol));
		System.out.println(eval.unfitness(sol));

	}

}
