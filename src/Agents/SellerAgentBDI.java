package Agents;
import General.Demand;
import General.Proposal;
import Products.Product;
import Services.SellingService;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;


@Agent
@Description("A seller agent")

@ProvidedServices(@ProvidedService(type = SellingService.class,implementation=@Implementation(SellingService.class)))
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



	@AgentBody
	public synchronized void agentBody() {
		System.out.println("Executing Seller");
	}
	
	
	
	public synchronized Proposal proposalForDemand(Demand d){
		
		if(d==null || !d.getProduct().equals(product) || d.getQuantity()>nrProducts)return null;
		
		
		return new Proposal(this);
		
		
	}
}
