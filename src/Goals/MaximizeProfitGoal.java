package Goals;

import java.util.ArrayList;

import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalResult;

@Goal
public class MaximizeProfitGoal {
	
	
	@GoalParameter
	final static int cycleLenght=10;
	
	@GoalParameter
	ArrayList<Integer> profits;
	
	@GoalResult
	Integer meanProfit;
	
	
	public MaximizeProfitGoal(ArrayList<Integer> profits,Integer profit) {
	
		this.meanProfit=profit;
		this.profits=profits;
	}
	
	
	

}
