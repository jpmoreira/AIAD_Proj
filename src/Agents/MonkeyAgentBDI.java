package Agents;

import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import Products.Banana;

@Agent
public class MonkeyAgentBDI extends BuyerAgentBDI {

	
	
	@AgentBody
	public synchronized void agentBody() {
		product=Banana.product();
		super.agentBody();
	}
}
