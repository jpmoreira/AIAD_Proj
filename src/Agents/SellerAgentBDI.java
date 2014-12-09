package Agents;
import java.util.ArrayList;

import General.Bid;
import General.Demand;
import General.Proposal;
import General.QLearner;
import General.QLearner.Action;
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
import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;


@Agent
@Description("A seller agent")
@Arguments({
	@Argument(name="Stock Capacity", clazz=Integer.class, defaultvalue="100"),
	@Argument(name="Production", clazz=Integer.class, defaultvalue="5"),
	@Argument(name="Base Price", clazz=Integer.class, defaultvalue="10"),
	@Argument(name="Product", clazz=String.class, defaultvalue="Banana")
	})
@ProvidedServices(@ProvidedService(type = SellingService.class,implementation=@Implementation(SimpleSellingService.class)))
public class SellerAgentBDI  {

	

	//______________ Constants __________________

	final static double stockLoadMinimum=0.1;
	final static double stockLoadMaximum=0.9;
	
	final static double stockLoadTargetMinimum=0.3;
	final static double stockLoadTargetMaximum=0.7;
	
	final static int windowSize=5;//the window size for the Q learning algorithm
	final static int softMaxTime=1000;
	
	
	//______________ Other Variable _______________
	
	public long startTime=System.currentTimeMillis();
	public ArrayList<Integer> cycleProfits=new ArrayList<Integer>();
	public String product;
	public QLearner q;
	public Action lastActionTaken;
	
	
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
	
	@Belief
	public int requests=0;
	
	@Belief
	public int earned=0;
	
	@Belief(dynamic=true)
	public boolean windowChanged=requests%windowSize==0;
	
	//__________________ Proposal Handling _____________________________
	
	public synchronized Proposal proposalForDemand(Demand d){
		
		requests++;
		
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
		earned+=bid.getQuantity()*bid.getPrice();
		System.out.println("Seller executed Proposal now with "+nrProducts+" products");
		
	}
	
	//__________________ Agent Body _____________________________
	
	@AgentBody
	public synchronized void agentBody() {
		
		stockCapacity = (int) agent.getArgument("Stock Capacity");
		production = (int) agent.getArgument("Production");//set production to the max production
		maxProduction=(int) agent.getArgument("Production");
		basePrice = (int) agent.getArgument("Base Price");
		product = (String) agent.getArgument("Product");
		q=new QLearner(2*basePrice,100*basePrice,basePrice,0.5,0.5,softMaxTime);
		agent.dispatchTopLevelGoal(new HandleStockQuantityGoal());
		price=q.currentPrice;
		lastActionTaken=Action.Mantain;
	}
	
	//_____________________ Plans _____________________________
	
	@Plan(trigger=@Trigger(factchangeds="time"))
	synchronized void updateStuff(){
		nrProducts+=production;
		
		if(nrProducts>stockCapacity)nrProducts=stockCapacity;
		
		float stockRatio=((float)nrProducts)/stockCapacity;
		
		
		/*
		if(stockRatio>0.8){
			price=(int) (0.75*basePrice);
		}
		else if(stockRatio<0.2){
			price=(int) (1.10*basePrice);
		}
		else price=basePrice;
	
		*/
		
		
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
		//System.out.println("Production set to "+production+" to stockLoad="+stockLoad+" (production="+production+")");

		
	}
	
	@Plan(trigger=@Trigger(factchangeds="windowChanged"))
	synchronized void updatePricingInfo(){
		
		if(!windowChanged)return;
		
		System.out.println("Window changed");
		windowChanged=false;
		
		q.iterate(earned, lastActionTaken);
		
		price=q.currentPrice;
		
		lastActionTaken=q.action();
		
		price+=Action.Increase.value;
		
		
		earned=0;
		
		
		
		
		
		
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
