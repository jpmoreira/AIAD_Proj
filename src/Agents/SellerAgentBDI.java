package Agents;
import java.util.ArrayList;

import General.Bid;
import General.Demand;
import General.Proposal;
import General.QLearner;
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
import jadex.micro.annotation.AgentKilled;
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
	@Argument(name="Base Price", clazz=Integer.class, defaultvalue="1"),
	@Argument(name="Product", clazz=String.class, defaultvalue="\"Banana\"")
	})
@ProvidedServices(@ProvidedService(type = SellingService.class,implementation=@Implementation(SimpleSellingService.class)))
public class SellerAgentBDI  {

	

	//______________ Constants __________________

	final static double stockLoadMinimum=0.1;
	final static double stockLoadMaximum=0.9;
	
	final static double stockLoadTargetMinimum=0.3;
	final static double stockLoadTargetMaximum=0.7;
	
	final static int windowSize=5;//the window size for the Q learning algorithm
	final static int softMaxTime=10;
	
	
	//______________ Other Variable _______________
	
	public long startTime=System.currentTimeMillis();
	public ArrayList<Integer> cycleProfits=new ArrayList<Integer>();
	public String product;
	public QLearner q;
	public int lastActionTaken;
	
	
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
	
	@Belief
	public int totalEarnedSoFar=0;
	
	@Belief
	public int earnedAt100=0;
	
	@Belief
	public int earnedAt200=0;
	
	@Belief
	public int earnedAt300=0;
	
	@Belief
	public int earnedAt400=0;
	
	@Belief
	public int earnedAt500=0;
	
	@Belief
	public int earnedAt600=0;
	
	@Belief
	public int earnedAt700=0;
	
	@Belief
	public int earnedAt800=0;
	
	@Belief
	public int earnedAt900=0;
	
	@Belief
	public int earnedAt1000=0;
	
	
	@Belief(dynamic=true)
	public boolean windowChanged=requests%windowSize==0;
	
	//__________________ Proposal Handling _____________________________
	
	public synchronized Proposal proposalForDemand(Demand d){
		
		
		if(requests==100)earnedAt100=earned;
		else if(requests==200)earnedAt200=totalEarnedSoFar;
		else if(requests==300)earnedAt300=totalEarnedSoFar;
		else if(requests==400)earnedAt400=totalEarnedSoFar;
		else if(requests==500)earnedAt500=totalEarnedSoFar;
		else if(requests==600)earnedAt600=totalEarnedSoFar;
		else if(requests==700)earnedAt700=totalEarnedSoFar;
		else if(requests==800)earnedAt800=totalEarnedSoFar;
		else if(requests==900)earnedAt900=totalEarnedSoFar;
		else if(requests==1000)earnedAt1000=totalEarnedSoFar;
		requests++;
		
		//System.out.println("" + this + " - Quantity "+ nrProducts + " with price " + price+ " for demand "+d);
		/*
		System.out.println(d==null);
		System.out.println("Buyer: " + d.getIssuer() + " Product: " + d.getProduct());
		System.out.println(d.getProduct()==null);
		System.out.println(!d.getProduct().equals(product));
		System.out.println(d.getQuantity()>nrProducts);
		*/
		if(d==null ||d.getProduct()==null || !d.getProduct().equals(product) || d.getQuantity()>nrProducts)return null;
		
		
		//System.out.println("giving proposal");
		
		return new Proposal(this,d.getQuantity());
	}

	public synchronized void executeBid(Bid bid) {
		
		
		//System.out.println("Seller executing Proposal");
		
		
		if(!(bid instanceof Proposal))return;
		
		if(!bid.getProduct().equals(product))return;
		
		if(bid.getQuantity()>nrProducts)return;

		if(!bid.getIssuer().equals(this))return;
		
		//System.out.println("Quantity before Proposal "+nrProducts + " with price " + price);
		nrProducts-=bid.getQuantity();
		earned+=bid.getQuantity()*bid.getPrice();
		//System.out.println("Seller executed Proposal now with "+nrProducts+" products");
		
	}
	
	//__________________ Agent Life Cycle _____________________________
	
	@AgentBody
	public synchronized void agentBody() {
		
		stockCapacity = (int) agent.getArgument("Stock Capacity");
		production = (int) agent.getArgument("Production");//set production to the max production
		maxProduction=(int) agent.getArgument("Production");
		basePrice = (int) agent.getArgument("Base Price");
		product = (String) agent.getArgument("Product");
		q=new QLearner(2*basePrice,20*basePrice,basePrice,0.5,0.0,softMaxTime);
		agent.dispatchTopLevelGoal(new HandleStockQuantityGoal());
		price=q.currentPrice;
		lastActionTaken=0;
	}
	
	
	@AgentKilled
	public synchronized void agentKill(){
		
		
		for(int i=0;i<q.qMatrix.length;i++){
			
			System.out.println("Q["+i+"]="+q.qMatrix[i]);
			
		}
		
	}
	
	
	//_____________________ Plans ___________________________
	
	//_____________________ Plans _____________________________
	
	@Plan(trigger=@Trigger(factchangeds="time"))
	synchronized void updateStuff(){
		nrProducts+=production;
		
		if(nrProducts>stockCapacity)nrProducts=stockCapacity;
		
		
		
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
		

		windowChanged=false;
		
		System.out.println("Practicing price= "+price+" with t="+q.t);
		
		q.iterate(earned, lastActionTaken);
		
		price=q.currentPrice;
		
		lastActionTaken=q.action();
		
		price+=lastActionTaken;
		
		
		totalEarnedSoFar+=earned;
		
		earned=0;
		
		
		
		
		
		
	}
	
	//_____________________ Goals _____________________________
	
	@Goal(excludemode=ExcludeMode.Never)
	public class HandleStockQuantityGoal {
		
		
		
		@GoalMaintainCondition(beliefs="stockLoad")
		synchronized protected boolean maintain() {
			return stockLoad<=stockLoadMaximum && stockLoad>=stockLoadMinimum;
		}

		@GoalTargetCondition(beliefs="stockLoad")
		synchronized protected boolean target() {
			return stockLoad>=0.30 && stockLoad<=0.70;
			
		}

	}

}
