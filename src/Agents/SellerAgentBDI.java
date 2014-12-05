package Agents;
import java.util.ArrayList;

import General.Bid;
import General.Demand;
import General.Proposal;
import General.Utilities;
//import Goals.MaximizeProfitGoal;
//import Products.Banana;
//import Products.Product;
import Services.SellingService;
import Services.SimpleSellingService;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
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

	
	int stockCapacity;
	
	int nrProducts;
	
	int production;
	
	@Agent
	protected BDIAgent agent;

	
	public String product;
	
	@Belief
	public int price;
	
	@Belief
	public int basePrice=10;
	
	public long startTime=System.currentTimeMillis();
	
	
	@Belief(updaterate=1000)
	public long time=System.currentTimeMillis();
	
	
	
	
	synchronized public void setProduct(String product) {
		this.product = product;
	}

	public int getPrice(){
		return price;
	}


	synchronized public String getProduct() {
		return product;
	}

	
	
	public int getStockCapacity() {
		return stockCapacity;
	}



	public void setStockCapacity(int stockCapacity) {
		this.stockCapacity = stockCapacity;
	}



	public int getProduction() {
		return production;
	}



	public void setProduction(int production) {
		this.production = production;
	}



	//for testing purposes
	public void setNrProducts(int nrProducts) {
		this.nrProducts = nrProducts;
	}



	public int getNrProducts() {
		return nrProducts;
	}


	

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
	
	
 
	
	@AgentBody
	public synchronized void agentBody() {
		
		//product=new Banana();
		
		stockCapacity = (int) agent.getArgument("Stock Capacity");
		production = (int) agent.getArgument("Production");
		basePrice = (int) agent.getArgument("Base Price");
		product = (String) agent.getArgument("Product");
		
		//int meanProfit= (int) agent.dispatchTopLevelGoal(new MaximizeProfitGoal()).get();
		
	}
	
	
	
	public synchronized Proposal proposalForDemand(Demand d){
		
		System.out.println("" + this + " - Quantity "+ nrProducts + " with price " + price);
		if(d==null ||d.getProduct()==null || !d.getProduct().equals(product) || d.getQuantity()>nrProducts) return null;
		
		
		return new Proposal(this,d.getQuantity());
		
		
	}



	public void executeBid(Bid bid) {
		
		System.out.println("Seller executing Proposal");
		
		
		if(!(bid instanceof Proposal))return;
		
		if(!bid.getProduct().equals(getProduct()))return;
		
		if(bid.getQuantity()>getNrProducts())return;

		if(!bid.getIssuer().equals(this))return;
		
		System.out.println("Quantity before Proposal "+nrProducts + " with price " + price);
		nrProducts-=bid.getQuantity();
		System.out.println("Seller executed Proposal now with "+nrProducts+" products");
		
	}
}
