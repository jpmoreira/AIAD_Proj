package Agents;
import java.util.ArrayList;

import General.Bid;
import General.Demand;
import General.Proposal;
import General.Utilities;
import Products.Banana;
import Products.Product;
import Services.SellingService;
import Services.SimpleSellingService;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Goal.ExcludeMode;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalTargetCondition;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;


@Agent
@Description("A seller agent")

@ProvidedServices(@ProvidedService(type = SellingService.class,implementation=@Implementation(SimpleSellingService.class)))
public class SellerAgentBDI  {

	
	//______________ Constants __________________
	
	final static double stockLoadMinimum=0.1;
	final static double stockLoadMaximum=0.9;
	
	final static double stockLoadTargetMinimum=0.3;
	final static double stockLoadTargetMaximum=0.7;
	
	//______________ Other Variable _______________
	
	public Product product;
	public long startTime=System.currentTimeMillis();
	public ArrayList<Integer> cycleProfits=new ArrayList<Integer>();
	
	//_______________ Agent ______________________
	
	@Agent
	protected BDIAgent agent;
	
//___________________ Beliefs _______________________
	
	@Belief
	int production;
	
	@Belief
	int maxProduction;
	
	@Belief
	public int price;
	
	@Belief
	public int basePrice=10;
	
	@Belief(updaterate=1000)
	public long time=System.currentTimeMillis();
	
	
	@Belief
	public Integer overallProfit=0;
	
	@Belief
	int stockCapacity=100;
	
	@Belief
	int nrProducts;
	
	@Belief(dynamic=true)
	public double stockLoad=nrProducts/(double)stockCapacity;
	
	//__________________ Proposal Handling _____________________________
	
	public synchronized Proposal proposalForDemand(Demand d){
		
		System.out.println("" + this + " - Quantity "+ nrProducts + " with price " + price);
		if(d==null ||d.getProduct()==null || !d.getProduct().equals(product) || d.getQuantity()>nrProducts)return null;
		
		
		return new Proposal(this,d.getQuantity());
		
		
	}

	public void executeBid(Bid bid) {
		
		System.out.println("Seller executing Proposal");
		
		
		if(!(bid instanceof Proposal))return;
		
		if(!bid.getProduct().equals(product))return;
		
		if(bid.getQuantity()>nrProducts)return;

		if(!bid.getIssuer().equals(this))return;
		
		System.out.println("Quantity before Proposal "+nrProducts + " with price " + price);
		nrProducts-=bid.getQuantity();
		System.out.println("Seller executed Proposal now with "+nrProducts+" products");
		
	}
	
	//__________________ Agent Body _____________________________
	
	@AgentBody
	public synchronized void agentBody() {
		
		product=new Banana();
		production=Utilities.randInt(1, 20);
		
		//int meanProfit= (int) agent.dispatchTopLevelGoal(new MaximizeProfitGoal(cycleProfits,overallProfit)).get();
	
		agent.dispatchTopLevelGoal(new HandleStockQuantityGoal());
	}
	
	//_____________________ Plans _____________________________
	
	@Plan(trigger=@Trigger(factchangeds="time"))
	synchronized void updateStuff(){
		nrProducts+=production;
		
		if(nrProducts>stockCapacity)nrProducts=stockCapacity;
		
		float stockRatio=((float)nrProducts)/stockCapacity;
		
		
		if(stockRatio>0.8){
			price=(int) (0.75*basePrice);
		}
		else if(stockRatio<0.2){
			price=(int) (1.10*basePrice);
		}
		else price=basePrice;
	
		
		
	}

	@Plan(trigger=@Trigger(goals=HandleStockQuantityGoal.class))
	synchronized void adaptProductionToStock(){
		
		if(stockLoad>=stockLoadTargetMaximum){
			
			double incl=-5./3.*maxProduction;
			double b=5./3.*maxProduction;
			
			production=(int)(incl*stockLoad+b);
			
		}
		else if(stockLoad<=stockLoadTargetMinimum){
			
			double incl=-5./3.*maxProduction;
			double b=maxProduction;
			
			production=(int)(incl*stockLoad+b);
			
		}
		
		System.out.println("Production set to "+production+" to stockLoad="+stockLoad+" (production="+production+")");
		
	}


	//_____________________ Goals _____________________________
	
	@Goal(excludemode=ExcludeMode.Never)
	public class HandleStockQuantityGoal {
		
		
		
		@GoalMaintainCondition(beliefs="stockLoad")
		protected boolean maintain() {
			return stockLoad<=stockLoadMaximum && stockLoad>=stockLoadMinimum;
		}

		@GoalTargetCondition(beliefs="stockLoad")
		protected boolean target() {
			return stockLoad>=0.30 && stockLoad<=0.70;
			
		}

	}

}
