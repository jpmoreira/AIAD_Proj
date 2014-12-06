package Goals;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalParameter;
import jadex.bdiv3.annotation.GoalTargetCondition;
import jadex.micro.annotation.Agent;
import Agents.*;

@Goal
public class HandleStockQuantityGoal {
	
	
	
	@GoalParameter
	double percentageOfStock;
	
	@GoalMaintainCondition(beliefs="stockLoad")
	protected boolean maintain() {
		return stockLoad<=90 && stockLoad>=10;
	}

	@GoalTargetCondition(beliefs="stockLoad")
	protected boolean target() {
		return stockLoad>=30 && stockLoad<=70;
		
	}

}
