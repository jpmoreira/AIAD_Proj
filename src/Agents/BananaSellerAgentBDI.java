package Agents;

import Products.Banana;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;

@Agent
@Description("A banana seller agent")
public class BananaSellerAgentBDI extends SellerAgentBDI{
	
	@AgentBody
	public synchronized void agentBody() {
		
		//product=new Banana();
		super.agentBody();
		
		
	}

}
