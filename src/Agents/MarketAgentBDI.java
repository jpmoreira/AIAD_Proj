package Agents;
import java.util.Timer;
import java.util.TimerTask;

import General.Bid;
import General.SealedProposal;
import Products.Product;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;


@Agent
@Description("A generic agent")
public abstract class MarketAgentBDI {
	
	
	protected Product product;
	
	
	protected int price;
	
	
	@Belief(updaterate=1000)
	protected long time=System.currentTimeMillis();
	
	
	
	
	synchronized public void setProduct(Product product) {
		this.product = product;
	}

	@Belief
	synchronized public void setPrice(int price) {
		this.price = price;
	}

	@Belief
	synchronized public int getPrice() {
		return price;
	}

	@AgentBody
	public synchronized void agentBody(){
		
		
	}

	synchronized public Product getProduct() {
		return product;
	}

	
	
	abstract public void executeBid(Bid sp);
}
