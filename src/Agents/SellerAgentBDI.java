package Agents;
import java.util.TimerTask;

import General.Bid;
import General.Demand;
import General.Proposal;
import General.Utilities;
import Services.SellingService;
import Services.SimpleSellingService;
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
public class SellerAgentBDI extends MarketAgentBDI {

	
	int stockCapacity;
	
	int nrProducts;
	
	int production;

	
	
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
		System.out.println("Updating product quantity to "+nrProducts);
	}
	
	@AgentBody
	public synchronized void agentBody() {
		System.out.println("Executing Seller");
		
		
		production=Utilities.randInt(1, 20);
		
		System.out.println("Production set to "+production);
		
		super.agentBody();
	}
	
	
	
	public synchronized Proposal proposalForDemand(Demand d){
		
		if(d==null ||d.getProduct()==null || !d.getProduct().equals(product) || d.getQuantity()>nrProducts)return null;
		
		
		return new Proposal(this,d.getQuantity());
		
		
	}



	@Override
	public void executeBid(Bid bid) {
		
		System.out.println("Seller executing Proposal");
		
		if(!bid.getClass().equals(Proposal.class))return;
		if(!bid.getProduct().equals(getProduct()))return;
		if(bid.getQuantity()>getNrProducts())return;
		if(!bid.getIssuer().equals(this))return;
		
		nrProducts-=bid.getQuantity();
		System.out.println("Seller executed Proposal now with "+nrProducts+" products");
		
	}
}
