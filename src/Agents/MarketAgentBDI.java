package Agents;
import java.util.Timer;
import java.util.TimerTask;

import Products.Product;
import jadex.bdiv3.annotation.Belief;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;


@Agent
@Description("A generic agent")
public class MarketAgentBDI {
	
	
	protected Product product;
	
	
	protected int price;
	
	
	protected Timer t=new Timer();
	
	protected TimerTask periodicTask=null;
	
	
	
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
		
		
		if(periodicTask!=null)t.scheduleAtFixedRate(periodicTask, 0, 1000);
		System.out.println("Running market agent");
	}

	synchronized public Product getProduct() {
		return product;
	}

}
